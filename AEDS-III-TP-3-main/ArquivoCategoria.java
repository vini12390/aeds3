import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;



public class ArquivoCategoria extends Arquivo<Categoria> {

    static ArvoreBMais<ParNomeID> arvoreBMais;
    RandomAccessFile arquivo;
    static Scanner console = new Scanner(System.in);
    // ArquivoTarefa arquivoTarefa;

    public ArquivoCategoria() throws Exception {
        super("categorias.db", Categoria.class.getConstructor());
        arquivo = new RandomAccessFile("categorias.db", "rw");

    }

    public ArquivoCategoria(String nomeArquivo, int ordemArvore) throws Exception {
        super(nomeArquivo, Categoria.class.getConstructor());
        arquivo = new RandomAccessFile("categorias.db", "rw");
        arvoreBMais = new ArvoreBMais<>(ParNomeID.class.getConstructor(), ordemArvore, "ParNomeId.db");
        // arquivoTarefa = new ArquivoTarefa("tarefas.db", 5); // Inicializa
        // ArquivoTarefa
    }


    @Override
    public int create(Categoria categoria) throws Exception {
        int id = super.create(categoria); // Usar o método da classe pai
        boolean arvoreSucesso = arvoreBMais.create(new ParNomeID(categoria.nome, id));
        if (!arvoreSucesso) {
            // Tratamento se a inserção na árvore falhar
            System.err.println("Erro ao inserir na Árvore B+.");
        }
        return id;
    }



    public void update() throws Exception {
        int idCategoria;
        Categoria C;
        boolean sucesso;
        System.out.println("Você deseja alterar alguma categoria? (S/N): ");
        char caracter = console.nextLine().charAt(0);
        if (caracter == 'S' || caracter == 's') {
            idCategoria = ControleCategorias.mostraCategorias2();
            C = super.read(idCategoria);

            if (C != null) {
                System.out.println("Categoria atual: " + C.nome);
                System.out.print("Digite o novo nome da categoria: ");
                String novoNome = console.nextLine();
                String nome = C.nome;

                // Atualiza o nome da categoria
                C.nome = novoNome;
                System.out.println(C.toString());

                // Atualiza o registro no arquivo
                sucesso = super.update(C);
                arvoreBMais.delete(new ParNomeID(nome, idCategoria));
                arvoreBMais.create(new ParNomeID(novoNome, idCategoria));

                if (sucesso == true) {
                    System.out.println("atualizado com sucesso");
                } else {
                    System.out.println("Erro ao atualizar");
                }
            }
        }
    }


    public void delete() throws Exception {
        int idCategoria = 0;
        String nomeCategoria = "";
        System.out.println("Você realmente deseja apagar alguma categoria? (S/N) ");
        char caracter = console.nextLine().trim().toUpperCase().charAt(0);
        if (caracter == 'S') {
            idCategoria = ControleCategorias.mostraCategorias2();

            System.out.println(idCategoria);
            ArquivoTarefa arquivoTarefa = new ArquivoTarefa("Tarefas.db", 5);
            int quant = arquivoTarefa.qntTarefaCategoria(idCategoria);

            if (quant != 0) {
                System.out.println("Não é possivel apagar a categoria, pois ela possui tarefas associadas");
            } else {
                boolean resp = super.delete(idCategoria);
                System.out.println("RESP = " + resp);
                if (resp == true) {
                    ArrayList<ParNomeID> ict = arvoreBMais.read(null);
                    for (int i = 0; i < ict.size(); i++) {
                        if (ict.get(i).getIdCategoria() == idCategoria) {
                            System.out.println("Categoria: " + nomeCategoria);
                        }
                    }

                    resp = arvoreBMais.delete(new ParNomeID(nomeCategoria, -1));
                    if (resp == true) {
                        System.out.println("Categoria apagada com sucesso");
                    } else {
                        System.out.println("Erro ao apagar a categoria");
                    }
                }
            }
        }

    }

}
