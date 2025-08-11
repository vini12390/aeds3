import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class ParNomeID implements RegistroArvoreBMais<ParNomeID>, RegistroHashExtensivel<ParNomeID> {

    private String nome; // chave
    private int idCategoria;     // valor
    private short TAMANHO = 30;  // tamanho em bytes
    public ParNomeID() throws Exception {
        this("", -1);
    }

    public ParNomeID(String n) throws Exception {
        this(n, -1);
    }

    public String getNome(){
        return nome;
    }




    public int getIdCategoria() {
        return idCategoria;
    }

    public ParNomeID(String n, int i) throws Exception {
        if (n.getBytes().length > 26)
            throw new Exception("Nome extenso demais. Diminua o número de caracteres.");
        this.nome = n; // ID do Usuário
        this.idCategoria = i; // ID da Pergunta
    }


    @Override
    public ParNomeID clone() {
        try {
            return new ParNomeID(this.nome, this.idCategoria);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public short size() {
        return this.TAMANHO;
    }

    public int compareTo(ParNomeID a) {
        return transforma(this.nome).compareTo(transforma(a.nome));
    }

    public static String transforma(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("").toLowerCase();
    }

    public String toString() {
        return "("+this.nome + ";" + this.idCategoria+")";
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        byte[] vb = new byte[26];
        byte[] vbNome = this.nome.getBytes();
        int i = 0;
        while (i < vbNome.length) {
            vb[i] = vbNome[i];
            i++;
        }
        while (i < 26) {
            vb[i] = ' ';
            i++;
        }
        dos.write(vb);
        dos.writeInt(this.idCategoria);
        return baos.toByteArray();
    }


    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        byte[] vb = new byte[26];
        dis.read(vb);
        this.nome = (new String(vb)).trim();
        this.idCategoria = dis.readInt();
    }




}
