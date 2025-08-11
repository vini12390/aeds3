import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;



public class Rotulo implements Registro {

    /** ATRIBUTOS */
    private int id;         // id do Rótulo
    private String nome;  // Rótulo
    private String key = "S$YZ&33TsXBoBFmM55^2k@&3DrgJBNsw"; // Chave de criptografia

    /** Construtor sem parâmetros */
    public Rotulo() {}

    public Rotulo(String nome) {
        this.nome = nome;
    }

    public Rotulo(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    /** Define um id 'i' pro nome */
    public void setId(int i) {
        id = i;
    }

    /** Retorna o id do nome */
    public int getId() {
        return id;
    }

    /** Define um id 'i' pro nome */
    public void set(String i) {
        nome = i;
    }

    /** Retorna o id do nome */
    public String get() {
        return nome;
    }

    /** Parser para String */
    public String toString() {
        return this.id + " " + this.nome;
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

    public void print() {
        System.out.println(id + " - " + nome);
    }

    public void printId() {
        System.out.print(id);
    }

    public void printNome() {
        System.out.print(nome);
    }
}