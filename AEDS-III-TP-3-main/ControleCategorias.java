import java.util.ArrayList;

public class ControleCategorias extends ArquivoCategoria {
    public ControleCategorias() throws Exception {
    }

    public void mostraCategorias() throws Exception {
        short tam;
        byte[] b;
        byte lapide;
        arquivo.seek(4);

        while (arquivo.getFilePointer() < arquivo.length()) {
            lapide = arquivo.readByte(); // Lê a lápide (indicador de remoção)
            tam = arquivo.readShort(); // Lê o tamanho do registro

            if (lapide != 0) { // Verifica se o registro é válido (não removido)

                b = new byte[tam];
                arquivo.read(b);
                Categoria c = new Categoria();
                c.fromByteArray(b);
                System.out.println(c.id + " - " + c.nome);
            } else {
                // Caso o registro tenha sido removido, pula para o próximo
                arquivo.seek(arquivo.getFilePointer() + tam);
            }
        }
    }


    public static int mostraCategorias2() throws Exception {
        int idCategoria = -1;
        String nomeCategoria;
        ArrayList<ParNomeID> list = arvoreBMais.read(null);

        for (ParNomeID item : list) {
            System.out.println(" - " + item.getNome());
        }
        System.out.print("-> ");

        nomeCategoria = console.nextLine();

        for (ParNomeID item : list) {
            if (nomeCategoria.equals(item.getNome())) {
                idCategoria = item.getIdCategoria();
                break;
            }
        }
        return idCategoria;
    }



    public static void mostraCategorias3() throws Exception {
        int idCategoria = -1;
        String nomeCategoria;
        ArrayList<ParNomeID> list = arvoreBMais.read(null);

        System.out.println("\n TODAS AS CATEGORIAS \n" );
        for (ParNomeID item : list) {
            System.out.println(" -> " + item.getNome());
        }
        System.out.println();
    }
}
