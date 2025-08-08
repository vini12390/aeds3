import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

public class Arquivo<T extends Registro> {
    private static int header = 4;
    private static RandomAccessFile file;
    private String fileName = "";
    Constructor<T> constructor;
    static HashExtensivel<ParIDEndereco> indiceDireto;

    public Arquivo(String fn, Constructor<T> constructor) throws Exception {
        this.fileName = fn;
        this.constructor = constructor;
        file = new RandomAccessFile(fileName, "rw");
        if (file.length() < header) {
            file.seek(0);
            file.writeInt(0);
        }
        indiceDireto = new HashExtensivel<>(
			ParIDEndereco.getConstructor(),
			3,
			fileName + ".hash_d.db",
			fileName + ".hash_c.db"
		);
    }

    public int create(T object) throws Exception {
        file.seek(0);
        int lastId = file.readInt();
        lastId++;

        object.setId(lastId);
        file.seek(0);
        file.writeInt(lastId);

        byte[] ba = object.toByteArray();
        short sizeObject = (short) ba.length;

        long endereco = file.length();
        file.seek(endereco);
        file.write(' ');
        file.writeShort(sizeObject);
        file.write(ba);

        indiceDireto.create(new ParIDEndereco(object.getId(), endereco));
        return object.getId();
    }

    public T read(int id) throws Exception {
        T object = constructor.newInstance();

        ParIDEndereco pie = indiceDireto.read(id);
        
        long address = -1;
        if (pie != null)
            address = pie.getEndereco();

        if (address != -1) {
			file.seek(address);

            byte lapide = file.readByte();
			short tamanhoRegistro = file.readShort();
			byte[] registro = new byte[tamanhoRegistro];

			file.read(registro);

			object.fromByteArray(registro);
		} else  {
            System.out.println("Não há tarefas.");
        }

		return object;
    }

    public boolean delete(int id) throws Exception {        
        ParIDEndereco pie = indiceDireto.read(id);
        long address = pie.getEndereco();

        if (address != -1) {
            file.seek(address);
            file.writeByte('*');

            indiceDireto.delete(id);
            return true;
        } else {
            return false;
        }

    }

    public boolean update(T objAlterado) throws Exception {
        ParIDEndereco pie = indiceDireto.read(objAlterado.getId());
        long enderecoAntigo = pie.getEndereco();
    
        if (enderecoAntigo == -1) {
            return false;
        }
    
        
        file.seek(enderecoAntigo);
        byte lapide = file.readByte();
        short tamanhoAntigo = file.readShort();
        byte[] registroAntigo = new byte[tamanhoAntigo];
        file.read(registroAntigo);
    
        byte[] registroAlterado = objAlterado.toByteArray();
        short tamanhoNovo = (short) registroAlterado.length;
    
        if (tamanhoNovo <= tamanhoAntigo) {
            file.seek(enderecoAntigo + 3);
            file.write(registroAlterado);
        } else {
            file.seek(enderecoAntigo);
            file.writeByte('*');
    
            long enderecoNovo = file.length();
            file.seek(enderecoNovo);
            file.writeByte(' ');
            file.writeShort(tamanhoNovo);
            file.write(registroAlterado);
    
            indiceDireto.update(new ParIDEndereco(objAlterado.getId(), enderecoNovo));
        }
    
        return true;
    }

    public void clear() throws Exception {
        file.setLength(header);
        file.seek(0);
        file.writeInt(0);
    }
}