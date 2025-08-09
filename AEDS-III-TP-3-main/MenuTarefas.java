import java.util.ArrayList;
import java.util.Scanner;

public class MenuTarefas {


    ArquivoTarefa arquivoTarefa;
    private static Scanner console = new Scanner(System.in);
    ArquivoCategoria arquivoCategoria = new ArquivoCategoria("categorias.db", 5);
    ArquivoRotulo arquivoRotulo = new ArquivoRotulo("rotulos.db", 5);

    public MenuTarefas() throws Exception {
        arquivoTarefa = new ArquivoTarefa("Tarefas.db", 5);
    }

    public void menu() throws Exception {

        int opcao;
        do {

            System.out.println("KUTOVA TAREFAS 1.0");
            System.out.println("-------");
            System.out.println("\n> Início > Tarefas");
            System.out.println("1 - Incluir");
            System.out.println("2 - Alterar");
            System.out.println("3 - Excluir");
            System.out.println("4 - Buscar");
            System.out.println("5 - Mostrar");
            System.out.println("0 - Voltar");

            System.out.print("Opção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    incluirTarefa();
                    break;
                case 2:
                    arquivoTarefa.update();
                    break;
                case 3:
                    arquivoTarefa.delete();
                    break;
                case 4:
                    ControleTarefas.buscarTarefas();
                    break;
                case 5:
                    ControleTarefas.mostrarTarefas();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }

        } while (opcao != 0);
    }

    public void teste() {
    }

    public void incluirTarefa() throws Exception {
        String nome;
        int dataCriacao = 0;
        int dataConclusao = 0;
        String status;
        int prioridade;
        int idCategoria;
        ArrayList<Integer> idRotulo = new ArrayList<Integer>(0);

        boolean dadosCompletos = false;

        System.out.println("\nInclusão de tarefa");
        do {
            System.out.print("\nNome da tarefa (min. de 5 letras): ");
            nome = console.nextLine();

            System.out.print("\rData de Criação (em apenas um número(exemplo: 01012001 para 01/01/2001): ");
            String entrada = console.nextLine();


            System.out.print("\rData de Conclusão (em apenas um número(exemplo: 01012001 para 01/01/2001): ");
            entrada = console.nextLine();


            System.out.print("\rStatus (pendente, concluído...): ");
            status = console.nextLine();

            System.out.print("\rPrioridade (1,2,3...): ");
            prioridade = Integer.parseInt(console.nextLine());

            if (nome.length() >= 5 || nome.length() == 0)
                dadosCompletos = true;
            else
                System.err.println("O nome da tarefa deve ter no mínimo 5 caracteres.");
        } while (!dadosCompletos);

        if (nome.length() == 0)
            return;

        System.out.println();
        System.out.println("Por favor, selecione a categoria da tarefa: ");
        System.out.println();
        idCategoria = ControleCategorias.mostraCategorias2();

        System.out.println("Confirma a inclusão da tarefa? (S/N) ");
        char resp = console.nextLine().charAt(0);
        if (resp == 'S' || resp == 's') {
            try {

                Tarefa t = new Tarefa(nome, dataCriacao, dataConclusao, status, prioridade, idCategoria, idRotulo);
                arquivoTarefa.create(t);
                System.out.println(t.toString());
                System.out.println("Tarefa criada com sucesso.");
            } catch (Exception e) {
                System.out.println("Erro do sistema. Não foi possível criar a Tarefa!");
            }
        }
    }

}
