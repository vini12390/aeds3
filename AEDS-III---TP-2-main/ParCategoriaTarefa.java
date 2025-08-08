import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParCategoriaTarefa implements RegistroArvoreBMais<ParCategoriaTarefa>, Comparable<ParCategoriaTarefa> {

    private int idCategoria;
    public int idTarefa;
    private static final short TAMANHO = 8;

    public ParCategoriaTarefa() {
        this(-1, -1);
    }

    public ParCategoriaTarefa(int n1) {
        this(n1, -1);
    }

    public ParCategoriaTarefa(int n1, int n2) {
        this.idCategoria = n1;
        this.idTarefa = n2;
    }

    @Override
    public ParCategoriaTarefa clone() {
        return new ParCategoriaTarefa(this.idCategoria, this.idTarefa);
    }

    public short size() {
        return TAMANHO;
    }

    @Override
    public int compareTo(ParCategoriaTarefa a) {
        if (this.idCategoria != a.idCategoria)
            return Integer.compare(this.idCategoria, a.idCategoria);
        else
            return this.idTarefa == -1 ? 0 : Integer.compare(this.idTarefa, a.idTarefa);
    }

    @Override
    public String toString() {
        return String.format("%3d", this.idCategoria) + ";" + String.format("%-3d", this.idTarefa);
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.idCategoria);
        dos.writeInt(this.idTarefa);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.idCategoria = dis.readInt();
        this.idTarefa = dis.readInt();
    }

}