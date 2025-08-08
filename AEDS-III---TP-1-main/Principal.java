import java.awt.image.AffineTransformOp;
import java.io.*;
import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Principal {
    public static void main(String[] args) {
        String nomeArquivo = "exemplo.dat"; // Nome do arquivo para o armazenamento

        try {
            File diretorio = new File("data");
            if (!diretorio.exists()) {
                diretorio.mkdir();
            }

            File arquivo = new File("data/" + nomeArquivo);
            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }

            Class<Tarefa> classe = Tarefa.class;
            Constructor<Tarefa> constructor = classe.getConstructor();
            Arquivo<Tarefa> arquivo1 = new Arquivo<>(nomeArquivo, constructor);

            Tarefa tarefa1 = new Tarefa("Estudar para a prova de AEDS 3", LocalDate.of(2024, 9, 16), LocalDate.of(2024, 9, 18), "Em Progresso", "1");
            Tarefa tarefa2 = new Tarefa("Fazer a prova de AEDS 3", LocalDate.of(2024, 9, 18), LocalDate.of(2024, 9, 18), "Pendente", "2");
            Tarefa tarefa3 = new Tarefa("Matricular-se para AEDS 3", LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 31), "Concluída", "3");

            int id1 = arquivo1.create(tarefa1);
            tarefa1.setID(id1);
            int id2 = arquivo1.create(tarefa2);
            tarefa2.setID(id2);
            int id3 = arquivo1.create(tarefa3);
            tarefa3.setID(id3);

            System.out.println(arquivo1.read(id3));
            System.out.println(arquivo1.read(id2));

            // Atualiza a tarefa1 com um nome maior
            tarefa1.setNome("Estudar para a prova de AEDS 3 para ir bem");
            arquivo1.update(tarefa1);

            // Verifica se a atualização foi bem-sucedida
            System.out.println(arquivo1.read(id1)); // Deve exibir a tarefa1 atualizada

            // Altera uma tarefa para um tamanho menor e exibe
            tarefa3.setNome("Já matriculado");
            arquivo1.update(tarefa3);
            System.out.println(arquivo1.read(id3));

            // Excluir uma tarefa e mostra que não existe mais
            arquivo1.delete(id2);
            Tarefa t = arquivo1.read(id2);
            if(t==null)
                System.out.println("Tarefa excluída");
            else
                System.out.println(t);

            arquivo1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




class Arquivo<T extends Registro> {
    final int tamCabecalho = 4;
    RandomAccessFile raf;
    String fileName;
    Constructor<T> constructor;
    IndiceDireto indiceDireto;

    public Arquivo(String fileName, Constructor<T> constructor) throws Exception {
        File file = new File("data/" + fileName);
        this.fileName = file.getPath();
        this.constructor = constructor;
        this.raf = new RandomAccessFile(this.fileName, "rw");
        this.indiceDireto = new IndiceDireto();

        if (file.length() < tamCabecalho) {
            raf.writeInt(0);
        }
    }

    public int create(T obj) throws Exception {
        raf.seek(0);
        int proximoID = raf.readInt() + 1;
        raf.seek(0);
        raf.writeInt(proximoID);
        obj.setID(proximoID);
        raf.seek(raf.length());
        long endereco = raf.getFilePointer();

        byte[] b = obj.toByteArray();
        raf.writeByte(' ');
        raf.writeShort(b.length);
        raf.write(b);

        indiceDireto.create(new ParIDEndereco(proximoID, endereco));

        return obj.getID();
    }


    public T read(int id) throws Exception {
        T obj;
        short tam;
        byte[] b;
        byte lapide;

        // Obtém a posição do objeto no arquivo usando o índice direto
        ParIDEndereco pid = indiceDireto.read(id);
        if (pid != null) {
            // Move o ponteiro do arquivo para o início do registro
            raf.seek(pid.getEndereco());
            // Cria uma nova instância do objeto
            obj = constructor.newInstance();
            // Lê o "lapide" (marcador de status)
            lapide = raf.readByte();
            // Lê o tamanho do registro
            tam = raf.readShort();
            // Lê os bytes do objeto
            b = new byte[tam];
            raf.read(b);

            // Verifica se o registro não foi excluído
            if (lapide == ' ') {
                // Converte os bytes lidos para o objeto
                obj.fromByteArray(b);
                // Verifica se o ID corresponde ao fornecido
                if (obj.getID() == id) {
                    return obj;
                }
            }
        }
        // Retorna null se o objeto não for encontrado
        return null;
    }
    public boolean delete(int id) throws Exception {
        T obj;
        short tam;
        byte[] b;
        byte lapide;
        long endereco;
        boolean excluido = false; // Variável para verificar se a exclusão foi bem-sucedida

        // Move o ponteiro do arquivo para o início dos registros
        raf.seek(tamCabecalho);

        // Percorre o arquivo para encontrar o registro com o ID a ser excluído
        while (raf.getFilePointer() < raf.length()) {
            obj = constructor.newInstance();
            endereco = raf.getFilePointer(); // Posição atual no arquivo
            lapide = raf.readByte(); // Lê o lapide
            tam = raf.readShort(); // Lê o tamanho do registro
            b = new byte[tam]; // Cria um array de bytes para o registro
            raf.read(b); // Lê os bytes do registro

            if (lapide == ' ') {
                obj.fromByteArray(b); // Converte os bytes em objeto
                if (obj.getID() == id) { // Verifica se o ID corresponde
                    raf.seek(endereco); // Move o ponteiro para o início do registro
                    raf.write('*'); // Marca o registro como excluído
                    indiceDireto.delete(id); // Remove o índice para este ID
                    excluido = true; // Marca a exclusão como bem-sucedida
                    break; // Interrompe o loop após a exclusão
                }
            }
        }

        // Retorna se a exclusão foi bem-sucedida ou não
        return excluido;
    }

    public boolean update(T novoObj) throws Exception {
        T obj;
        short tam;
        byte[] b;
        byte lapide;
        long endereco;
        boolean atualizado = false;

        // Move o ponteiro do arquivo para o início dos registros
        raf.seek(tamCabecalho);

        // Percorre o arquivo para encontrar o registro com o ID a ser atualizado
        while (raf.getFilePointer() < raf.length()) {
            obj = constructor.newInstance();
            endereco = raf.getFilePointer(); // Posição atual no arquivo
            lapide = raf.readByte(); // Lê o lapide
            tam = raf.readShort(); // Lê o tamanho do registro
            b = new byte[tam]; // Cria um array de bytes para o registro
            raf.read(b); // Lê os bytes do registro

            if (lapide == ' ') { // Verifica se o registro não está excluído
                obj.fromByteArray(b); // Converte os bytes em objeto
                if (obj.getID() == novoObj.getID()) { // Verifica se o ID corresponde
                    byte[] b2 = novoObj.toByteArray(); // Converte o novo objeto em bytes
                    short tam2 = (short) b2.length;

                    // Se o tamanho do novo objeto for menor ou igual ao tamanho do antigo
                    if (tam2 <= tam) {
                        raf.seek(endereco + 3); // Move o ponteiro para substituir o registro antigo
                        raf.write(b2); // Escreve o novo registro
                    } else {
                        // Marca o registro antigo como excluído
                        raf.seek(endereco);
                        raf.write('*');
                        // Adiciona o novo registro ao final do arquivo
                        raf.seek(raf.length());
                        raf.writeByte(' ');
                        raf.writeShort(tam2);
                        raf.write(b2);
                    }

                    // Atualiza o índice
                    indiceDireto.delete(novoObj.getID()); // Remove o antigo índice
                    indiceDireto.create(new ParIDEndereco(novoObj.getID(), raf.getFilePointer() - b2.length - 3));
                    atualizado = true; // Marca a atualização como bem-sucedida
                    break; // Interrompe o loop após a atualização
                }
            }
        }

        return atualizado;
    }
    public void close() throws IOException {
        raf.close();
    }
}








interface Registro{
    public int getID();
    public void setID(int n);
    public byte[] toByteArray() throws IOException;
    public void fromByteArray(byte[] ba) throws IOException;
    public short getByteArrayLength() throws Exception;


}

class Tarefa implements Registro {
    private String nome;
    private LocalDate dataDeCriacao;
    private LocalDate dataDeConclusao;
    private String status;
    private String prioridade;
    private int id;

    public Tarefa(String nome, LocalDate dataDeCriacao, LocalDate dataDeConclusao, String status, String prioridade) {
        this.nome = nome;
        this.dataDeCriacao = dataDeCriacao;
        this.dataDeConclusao = dataDeConclusao;
        this.status = status;
        this.prioridade = prioridade;
    }

    public Tarefa() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataDeCriacao() {
        return dataDeCriacao;
    }

    public void setDataDeCriacao(LocalDate dataDeCriacao) {
        this.dataDeCriacao = dataDeCriacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDate getDataDeConclusao() {
        return dataDeConclusao;
    }

    public void setDataDeConclusao(LocalDate dataDeConclusao) {
        this.dataDeConclusao = dataDeConclusao;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    @Override
    public int getID() {
        return id;
    }
    @Override
    public String toString() {
        return "Tarefa{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dataDeCriacao=" + dataDeCriacao +
                ", dataDeConclusao=" + dataDeConclusao +
                ", status='" + status + '\'' +
                ", prioridade='" + prioridade + '\'' +
                '}';
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.getID()); // Usando getID em vez de getId
        dos.writeUTF(this.getNome());
        dos.writeUTF(this.getDataDeCriacao().toString());
        dos.writeUTF(this.getDataDeConclusao().toString());
        dos.writeUTF(this.getStatus());
        dos.writeUTF(this.getPrioridade());

        dos.close();
        baos.close();

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.dataDeCriacao = LocalDate.parse(dis.readUTF());
        this.dataDeConclusao = LocalDate.parse(dis.readUTF());
        this.status = dis.readUTF();
        this.prioridade = dis.readUTF();

        dis.close();
        bais.close();
    }

    @Override
    public short getByteArrayLength() throws IOException {
        return (short) toByteArray().length;
    }
}

class ParIDEndereco implements Serializable {
    private int id;
    private long endereco;

    public ParIDEndereco(int id, long endereco) {
        this.id = id;
        this.endereco = endereco;
    }

    public int getId() {
        return id;
    }

    public long getEndereco() {
        return endereco;
    }
}

class IndiceDireto {
    private static final String INDEX_FILE = "data/index.dat";
    private List<ParIDEndereco> index;

    public IndiceDireto() {
        index = new ArrayList<>();
        loadIndex();
    }

    public void create(ParIDEndereco par) throws IOException {
        index.add(par);
        saveIndex();
    }

    public ParIDEndereco read(int id) {
        for (ParIDEndereco par : index) {
            if (par.getId() == id) {
                return par;
            }
        }
        return null;
    }

    public boolean delete(int id) throws IOException {
        ParIDEndereco toRemove = null;
        for (ParIDEndereco par : index) {
            if (par.getId() == id) {
                toRemove = par;
                break;
            }
        }
        if (toRemove != null) {
            index.remove(toRemove);
            saveIndex();
            return true;
        }
        return false;
    }

    private void loadIndex() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(INDEX_FILE))) {
            index = (List<ParIDEndereco>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File not found, initializing a new index
            index = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveIndex() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(INDEX_FILE))) {
            oos.writeObject(index);
        }
    }
}
