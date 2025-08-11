import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;


public class Tarefa implements Registro {
    private int id;
    private String nome;
    private int dataDeCriacao;
    private int dataDeConclusao;
    private String status;
    private int Prioridade;
    private int idCategoria;
    public ArrayList<Integer> idRotulo;
    private String key = "S$YZ&33TsXBoBFmM55^2k@&3DrgJBNsw"; // Chave de criptografia

    public Tarefa() {
        this.id = -1;  // Definindo o valor default para o id
        this.nome = "";
        this.dataDeCriacao = 0;
        this.dataDeConclusao = 0;
        this.status = "";
        this.Prioridade = 0;
        this.idCategoria = -1;
        this.idRotulo = new ArrayList<Integer>(0);  // Inicializando a lista idRotulo
    }

    public Tarefa(String nome, int dataDeCriacao, int dataDeConclusao, String status, int Prioridade, int idCategoria, ArrayList<Integer> idRotulo) {
        this.id = id;
        this.nome = nome;
        this.dataDeCriacao = dataDeCriacao;
        this.dataDeConclusao = dataDeConclusao;
        this.status = status;
        this.Prioridade = Prioridade;
        this.idCategoria = idCategoria;
        this.idRotulo = idRotulo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDataDeCriacao() {
        return dataDeCriacao;
    }

    public void setDataDeCriacao(int dataDeCriacao) {
        this.dataDeCriacao = dataDeCriacao;
    }

    public int getDataDeConclusao() {
        return dataDeConclusao;
    }

    public void setDataDeConclusao(int dataDeConclusao) {
        this.dataDeConclusao = dataDeConclusao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPrioridade() {
        return Prioridade;
    }

    public void setPrioridade(int Prioridade) {
        this.Prioridade = Prioridade;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    // Método para serializar e criptografar os dados
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Serializa os dados
        dos.writeInt(getId());
        dos.writeUTF(getNome());
        dos.writeInt(getDataDeCriacao());
        dos.writeInt(getDataDeConclusao());
        dos.writeUTF(getStatus());
        dos.writeInt(getPrioridade());
        dos.writeInt(getIdCategoria());

        byte[] dados = baos.toByteArray();

        // Criptografa os dados
        byte[] dadosCriptografados = Criptografia.cifrar(dados, key);
        return dadosCriptografados;
    }

    // Método para descriptografar e desserializar os dados
    public void fromByteArray(byte[] ba) throws Exception {
        // Descriptografa os dados
        ba = Criptografia.decifrar(ba, key);

        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        // Preenche o objeto com os dados descriptografados
        setId(dis.readInt());
        setNome(dis.readUTF());
        setDataDeCriacao(dis.readInt());
        setDataDeConclusao(dis.readInt());
        setStatus(dis.readUTF());
        setPrioridade(dis.readInt());
        setIdCategoria(dis.readInt());
    }

    public String toString() {
        return "ID: " + getId() + " | Nome: " + getNome() + 
               " | Data de Criação: " + getDataDeCriacao() + 
               " | Data de Conclusão: " + getDataDeConclusao() + 
               " | Status: " + getStatus() + 
               " | Prioridade: " + getPrioridade();
    }

    public boolean existIdRotulo(int idRotulo) {
        return this.idRotulo.contains(idRotulo);
    }
}
