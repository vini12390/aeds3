import java.io.*;
import java.text.Normalizer;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ArquivoTarefa extends Arquivo<Tarefa> {

    static Arquivo<Tarefa> arqTarefas;

    static Scanner console = new Scanner(System.in);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    ArquivoCategoria arquivoCategoria = new ArquivoCategoria("dados/categorias.db", 5); // Alterado para salvar na pasta dados
    static ArvoreBMais<ParCategoriaTarefa> arvoreCT;
    ArvoreBMais<RotuloTarefa> arvoreRT;
    ArvoreBMais<TarefaRotulo> arvoreTR;
    static ArvoreBMais<ParNomeIDTarefa> arvoreBMais;

    // Arquivo para rótulos
    ArquivoRotulo arquivoRotulo = new ArquivoRotulo("dados/rotulos.db", 5); // Alterado para salvar na pasta dados
    ArrayList<String> stopWords = new ArrayList<String>();
    ListaInvertida listaInvertida;


    public ArquivoTarefa(String nomeArquivo, int ordemArvore) throws Exception {
        super(nomeArquivo, Tarefa.class.getConstructor());
        arqTarefas = new Arquivo<>(nomeArquivo, Tarefa.class.getConstructor());

        // Inicialização das árvores com seus respectivos arquivos .db na pasta dados
        arvoreCT = new ArvoreBMais<>(ParCategoriaTarefa.class.getConstructor(), 5, "dados/CategoriaTarefa.db"); // Alterado para pasta dados
        arvoreRT = new ArvoreBMais<>(RotuloTarefa.class.getConstructor(), 5, "dados/RotuloTarefa.db"); // Alterado para pasta dados
        arvoreTR = new ArvoreBMais<>(TarefaRotulo.class.getConstructor(), 5, "dados/TarefaRotulo.db"); // Alterado para pasta dados
        arvoreBMais = new ArvoreBMais<>(ParNomeIDTarefa.class.getConstructor(), ordemArvore, "dados/ParNomeIDTarefa.db"); // Alterado para pasta dados

        // Carregar stop-words
        
    }

    @Override
    public int create(Tarefa tarefa) throws Exception {
        int id = arqTarefas.create(tarefa);
        arvoreCT.create(new ParCategoriaTarefa(tarefa.getIdCategoria(), tarefa.getId()));
        return id;
    }

    public Tarefa read(int idCategoria) throws Exception {
        ArrayList<ParCategoriaTarefa> ict = arvoreCT.read(new ParCategoriaTarefa(idCategoria, -1));
        for (int i = 0; i < ict.size(); i++) {
            System.out.println(ict.get(i).toString());
        }
        System.out.println("Resposta: ");
        System.out.println();

        if (ict.isEmpty()) {
            System.out.println("Categoria não encontrada");
            return null;
        }

        Tarefa tarefa = null;
        for (int i = 0; i < ict.size(); i++) {
            if (tarefa != null) {
                tarefa = arqTarefas.read(ict.get(i).idTarefa);
                System.out.println(tarefa.toString() + " ");
                System.out.println();
            }
        }

        return tarefa;
    }

    public void delete() throws Exception {
        boolean resp = false;
        String entrada;
        System.out.println("Você realmente deseja apagar alguma tarefa? (S/N)");
        char caracter = console.nextLine().charAt(0);
        if (caracter == 'S' || caracter == 's') {
            ControleTarefas.mostrarTarefas();
            System.out.println("Entre com o nome da tarefa a ser deletada: ");
            entrada = console.nextLine();
            ArrayList<ParCategoriaTarefa> ict = arvoreCT.read(null);
            Tarefa tarefa = null;
            for (int i = 0; i < ict.size(); i++) {
                tarefa = arqTarefas.read(ict.get(i).idTarefa);
                if (tarefa != null) {
                    if (tarefa.getNome().equals(entrada)) {
                        int idTarefa = tarefa.getId();
                        resp = arqTarefas.delete(idTarefa);
                    }
                }
            }
            if (resp == true) {
                System.out.println("Tarefa apagada com sucesso!");
            }
        }
    }

    public int qntTarefaRotulo(int idRotulo) throws Exception {
        // Vamos procurar todas as TarefaRotulo associadas ao idRotulo
        ArrayList<TarefaRotulo> tarefasRotulo = arvoreTR.read(new TarefaRotulo(idRotulo, -1)); // Supondo que o método read pega tarefas associadas ao rótulo
        return tarefasRotulo.size(); // Retorna a quantidade de tarefas associadas ao rótulo
    }
    

    public void update() throws Exception {
        String entrada;
        boolean resp = false;
        System.out.println("Você deseja atualizar alguma tarefa? (S/N)");
        char caracter = console.nextLine().trim().toUpperCase().charAt(0);

        if (caracter == 'S') {
            System.out.println("Mostrando as tarefas existentes...");
            ControleTarefas.mostrarTarefas(); // Presumo que você tenha esse método implementado

            System.out.println("Entre com o nome da tarefa a ser atualizada: ");
            entrada = console.nextLine();

            // Certifique-se de que 'arvoreBMais' não retorne null
            ArrayList<ParCategoriaTarefa> ict = arvoreCT.read(null);

            if (ict != null) {
                for (ParCategoriaTarefa categoriaTarefa : ict) {
                    Tarefa tarefa = arqTarefas.read(categoriaTarefa.idTarefa); // Lendo a tarefa a partir do ID da
                    // categoria

                    if (tarefa != null && tarefa.getNome().equals(entrada)) {
                        System.out.println("Entre com o novo nome da tarefa: ");
                        String nome = console.nextLine();
                        System.out.println("Entre com a nova data de criação da tarefa: ");
                        int dataCriacao = Integer.parseInt(console.nextLine());
                        System.out.println("Entre com a nova data de conclusão da tarefa (exemplo: 01012001 para 01/01/2001): ");
                        int dataConclusao = Integer.parseInt(console.nextLine());
                        System.out.println("Entre com o novo status da tarefa: ");
                        String status = console.nextLine();
                        System.out.println("Entre com a nova prioridade da tarefa: ");
                        String prioridade = console.nextLine();
                        System.out.println("Entre com a nova categoria da tarefa:");
                        tarefa.setIdCategoria(ControleCategorias.mostraCategorias2());

                        Tarefa testeTarefa = new Tarefa();
                        resp = arqTarefas.update(testeTarefa);
                        // Atribuindo resp como true após a atualização

                        if (resp) {
                            System.out.println("Tarefa atualizada com sucesso!");
                        }
                    }
                }
            } else {
                System.out.println("Nenhuma tarefa encontrada.");
            }
        }
    }

    public int qntTarefaCategoria(int idCategoria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'qntTarefaCategoria'");
    }

    public int selecionaTarefa() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selecionaTarefa'");
    }

    // Métodos restantes seguem o mesmo padrão, alterando apenas o caminho dos arquivos .db para dentro da pasta "dados"

    // O resto do código permanece igual, apenas alterando o caminho dos arquivos para 'dados/'
}
