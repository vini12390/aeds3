import java.util.Scanner;

public class MenuCategoria {

    ArquivoCategoria arqCategorias;
    private static Scanner console = new Scanner(System.in);

    public MenuCategoria() throws Exception {
        arqCategorias = new ArquivoCategoria("categorias.db", 5);
    }

    public void menu() throws Exception {

        int opcao;
        do {

            System.out.println("AEDsIII");
            System.out.println("-------");
            System.out.println("\n> Início > Categorias");
            System.out.println("1 - Incluir");
            System.out.println("2 - Alterar");
            System.out.println("3 - Excluir");
            System.out.println("4 - Mostrar");
            System.out.println("0 - Voltar");

            System.out.print("Opção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    incluirCategoria();
                    break;
                case 2:
                    arqCategorias.update();
                    break;
                case 3:
                    arqCategorias.delete();
                    break;
                case 4:
                    ControleCategorias.mostraCategorias3();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }

        } while (opcao != 0);
    }

    public void incluirCategoria() {
        String nome;
        boolean dadosCompletos = false;

        System.out.println("\nInclusão de categoria");
        do {
            System.out.print("\nNome da categoria (min. de 5 letras): ");
            nome = console.nextLine();
            if (nome.length() >= 5 || nome.length() == 0)
                dadosCompletos = true;
            else
                System.err.println("O nome da categoria deve ter no mínimo 5 caracteres.");
        } while (!dadosCompletos);

        if (nome.length() == 0)
            return;

        System.out.println("Confirma a inclusão da categoria? (S/N) ");
        char resp = console.nextLine().charAt(0);
        if (resp == 'S' || resp == 's') {
            try {
                Categoria c = new Categoria(nome);

                arqCategorias.create(c);
                System.out.println("Categoria criada com sucesso.");
            } catch (Exception e) {
                System.out.println("Erro do sistema. Não foi possível criar a categoria!");
            }
        }
    }

}
