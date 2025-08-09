import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

public class Tarefa implements Registro {
    private int id;
    private String nome;
    private int dataDeCriacao;
    private int dataDeConclusao;
    private String status;
    private int Prioridade;
    private int idCategoria;
    HashExtensivel<ParNomeID> indiceIndiretoNomeId;
    public ArrayList<Integer> idRotulo;

    public Tarefa() {
        this(-1, "", 0, 0, "", 0, -1, new ArrayList<Integer>(0));
    }

    public Tarefa( String nome, int dataDeCriacao, int dataDeConclusao, String status, int Prioridade, int idCategoria, ArrayList<Integer> idRotulo) {
        this.id = id;
        this.nome = nome;
        this.dataDeCriacao = dataDeCriacao;
        this.dataDeConclusao = dataDeConclusao;
        this.status = status;
        this.Prioridade = Prioridade;
        this.idCategoria = idCategoria;
        this.idRotulo = idRotulo;
    }


    public Tarefa(int id, String nome, int dataCriacao, int dataConclusao, String status, String prioridade, int idCategoria) {
    }

    public Tarefa(int i, String s, int i1, int i2, String s1, int i3, int i4, ArrayList<Integer> integers) {
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

    public void setIdCategoria(int idCategoria){ this.idCategoria = idCategoria;}

    public int getIdCategoria(){return idCategoria;}

    public void setPrioridade(int Prioridade) {
        this.Prioridade = Prioridade;
    }

    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(getId());
        dos.writeUTF(getNome());
        dos.writeInt(getDataDeCriacao());
        dos.writeInt(getDataDeConclusao());
        dos.writeUTF(getStatus());
        dos.writeInt(getPrioridade());
        dos.writeInt(getIdCategoria());

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        setId(dis.readInt());
        setNome(dis.readUTF());
        setDataDeCriacao(dis.readInt());
        setDataDeConclusao(dis.readInt());
        setStatus(dis.readUTF());
        setPrioridade(dis.readInt());
        setIdCategoria(dis.readInt());
    }

    public String toString() {
        return "ID: " + getId() + " | Nome: " + getNome() + " | Data de Criação: " + getDataDeCriacao() + " | Data de Conclusão: " + getDataDeConclusao() + " | Status: " + getStatus() + " | Prioridade: " + getPrioridade();
    }

    public boolean existIdRotulo(int idRotulo) {
        boolean resposta = false;

        for (int i = 0; i < this.idRotulo.size(); i++) {
            if (this.idRotulo.get(i) == idRotulo)
                resposta = true;
        }

        return resposta;
    }



}