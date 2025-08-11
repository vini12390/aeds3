import java.util.ArrayList;

public class ControleTarefas extends ArquivoTarefa{


    public ControleTarefas(String nomeArquivo, int ordemArvore) throws Exception {
        super(nomeArquivo, ordemArvore);
    }

    public static void buscarTarefas() throws Exception {
        String entrada;

        System.out.println("Você deseja pesquisar alguma tarefa? (S/N)");
        char caracter = console.nextLine().trim().toUpperCase().charAt(0); // Melhorado para ser case insensitive

        if (caracter == 'S') {
            System.out.println("Mostrando as tarefas existentes...");
            mostrarTarefas(); // Presumo que você tenha esse método implementado

            System.out.println("Entre com o nome da tarefa a ser retornada: ");
            entrada = console.nextLine();

            ArrayList<ParCategoriaTarefa> ict = arvoreCT.read(null); // Certifique-se de que arvoreBMais não retorne
            // null

            if (ict != null) {
                Tarefa tarefa = null;
                for (ParCategoriaTarefa parCategoriaTarefa : ict) {
                    tarefa = arqTarefas.read(parCategoriaTarefa.idTarefa); // Lendo a tarefa a partir do ID da categoria
                    if (tarefa != null && tarefa.getNome().equals(entrada)) { // Simplificado
                        System.out.println(tarefa.toString());
                    }
                }
            } else {
                System.out.println("Nenhuma tarefa encontrada.");
            }
        }
    }

    public static void mostrarTarefas() throws Exception {
        System.out.println("Você deseja mostrar as tarefas de todas as categorias? (S/N)");
        char resp = console.nextLine().charAt(0);
        ArrayList<ParCategoriaTarefa> ict = arvoreCT.read(null);
        if (resp == 'S' || resp == 's') {

            System.out.println("\nResposta: ");

            for (int i = 0; i < ict.size(); i++) {
                Tarefa tarefa = arqTarefas.read(ict.get(i).idTarefa);
                if (tarefa != null) {
                    System.out.println(tarefa.toString() + " ");
                    System.out.println();
                }
            }
        } else {
            System.out.println("Selecione a categoria desejada: ");
            int idCategoria = ControleCategorias.mostraCategorias2();
            for (int i = 0; i < ict.size(); i++) {

                Tarefa tarefa = arqTarefas.read(ict.get(i).idTarefa);
                if ((tarefa != null) && (tarefa.getIdCategoria() == idCategoria)) {
                    System.out.println(tarefa.toString() + " ");
                    System.out.println();
                }
            }
        }
    }

}
