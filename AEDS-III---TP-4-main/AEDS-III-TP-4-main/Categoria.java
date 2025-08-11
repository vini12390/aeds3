import java.io.*;


public class Categoria implements Registro {

    public int id;
    public String nome;
    private String key = "S$YZ&33TsXBoBFmM55^2k@&3DrgJBNsw"; // Chave de criptografia

    public Categoria() {
        this(-1, "");
    }

    public Categoria(String n) {
        this(-1, n);
    }

    public Categoria(int i, String n) {
        this.id = i;
        this.nome = n;
    }

    public int getIdCategoria() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return "\nID..: " + this.id +
               "\nNome: " + this.nome;
    }

    /** @return Um array de bytes do objeto em questão */
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        byte[] dados = baos.toByteArray();
        
        // Criptografar os dados
        byte[] dadosCriptografados = Criptografia.cifrar(dados, key);
        return dadosCriptografados;
    }

    /** Preenche o objeto de acordo com um array de bytes lido
     * @param byteArray 'byte[ ]' Array de bytes
     */
    public void fromByteArray(byte[] byteArray) throws Exception {
        // Descriptografar os dados
        byteArray = Criptografia.decifrar(byteArray, key);
        
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        DataInputStream dis = new DataInputStream(bais);
        
        // Ler os dados descriptografados
        this.id = dis.readInt();
        this.nome = dis.readUTF();
    }
}
