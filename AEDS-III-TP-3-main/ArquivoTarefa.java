


import java.io.*;
import java.text.Normalizer;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ArquivoTarefa extends Arquivo<Tarefa> {

    static Arquivo<Tarefa> arqTarefas;

    static Scanner console = new Scanner(System.in);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    ArquivoCategoria arquivoCategoria = new ArquivoCategoria("categorias.db", 5);
    static ArvoreBMais<ParCategoriaTarefa> arvoreCT;
    ArvoreBMais<RotuloTarefa> arvoreRT;
    ArvoreBMais<TarefaRotulo> arvoreTR;
    static ArvoreBMais<ParNomeIDTarefa> arvoreBMais;


    ArquivoRotulo arquivoRotulo = new ArquivoRotulo("rotulos.db", 5);
    ArrayList<String> stopWords = new ArrayList<String>();
    ListaInvertida listaInvertida;


    public ArquivoTarefa(String nomeArquivo, int ordemArvore) throws Exception {
        super(nomeArquivo, Tarefa.class.getConstructor());
        arqTarefas = new Arquivo<>(nomeArquivo, Tarefa.class.getConstructor());

        // inicializacao das arvores
        arvoreCT = new ArvoreBMais<>(ParCategoriaTarefa.class.getConstructor(), 5, "CategoriaTarefa.db");
        arvoreRT = new ArvoreBMais<>(RotuloTarefa.class.getConstructor(), 5, "RotuloTarefa.db");
        arvoreTR = new ArvoreBMais<>(TarefaRotulo.class.getConstructor(), 5, "TarefaRotulo.db");
        arvoreBMais = new ArvoreBMais<>(ParNomeIDTarefa.class.getConstructor(), ordemArvore,
                "ParNomeIDTarefa.db");

        // carregar stop-words
        gerarLista();
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

                        Tarefa testeTarefa = new Tarefa(tarefa.getId(), nome, dataCriacao, dataConclusao, status,
                                prioridade, tarefa.getIdCategoria());
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

    public int qntTarefaCategoria(int idCategoria) throws Exception {
        int n = 0;
        ArrayList<ParCategoriaTarefa> ict = arvoreCT.read(null);
        for (int i = 0; i < ict.size(); i++) {
            Tarefa tarefa = arqTarefas.read(ict.get(i).idTarefa);
            if ((tarefa != null) && (tarefa.getIdCategoria() == idCategoria)) {
                n++;
            }
        }
        return n;
    }
    public int qntTarefaRotulo(int idRotulo) throws Exception {
        int n = 0;
        ArrayList<RotuloTarefa> ict = arvoreRT.read(null);
        for (int i = 0; i < ict.size(); i++) {
            Tarefa tarefa = arqTarefas.read(ict.get(i).idTarefa);
            if ((tarefa != null) && (tarefa.existIdRotulo(idRotulo))) {
                n++;
            }
        }
        return n;
    }

    /** Carregar arquivo de stop-words */
    public void gerarLista() {
        File arquivo = new File("stopWords.txt");

        try {
            // Verifica se o arquivo existe
            if (!arquivo.exists()) {
                // Cria um novo arquivo vazio automaticamente
                if (arquivo.createNewFile()) {
                    System.out.println("Arquivo 'stopWords.txt' não encontrado. Um novo arquivo vazio foi criado.");
                } else {
                    System.err.println("Erro ao tentar criar o arquivo 'stopWords.txt'.");
                    return; // Sai do método em caso de falha ao criar o arquivo
                }
            }

            // Carregar o arquivo se ele existe
            try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    stopWords.add(linha.trim());
                }
                System.out.println("Stop-words carregadas com sucesso.");
            }

        } catch (IOException e) {
            System.err.println("Erro ao carregar ou criar o arquivo 'stopWords.txt': " + e.getMessage());
        }
    }

    public String[] removerStopWords(String[] nomeTarefa) {
        ArrayList<String> aux = new ArrayList<>();

        for (String palavra : nomeTarefa) {
            if (!stopWords.contains(palavra)) {
                aux.add(palavra);
            }
        }
        return aux.toArray(new String[0]);
    }

    public static float calcularFrequencia(String[] words, String word) {
        // Conta o número de vezes que a palavra aparece no array
        int count = 0;
        for (String w : words) {
            if (w.equals(word)) {
                count++;
            }
        }
        // Calcula a frequência
        float frequency = (float) count / words.length;
        return frequency;
    }

    public int selecionaTarefa() throws Exception {
        int idTarefa = -1;
        String nomeTarefa;
        ArrayList<ParNomeIDTarefa> list = arvoreBMais.read(null);

        for (ParNomeIDTarefa item : list) {
            System.out.println(" - " + item.nomeTarefa);
        }
        System.out.print("-> ");

        nomeTarefa = console.nextLine();

        for (ParNomeIDTarefa item : list) {
            if (nomeTarefa.equals(item.nomeTarefa)) {
                idTarefa = item.idTarefa;
                break; // Encontrou a rotulo, sair do loop
            }
        }
        return idTarefa;
    }

    public void adicionarLista() throws Exception {

        ArrayList<ParCategoriaTarefa> ict = arvoreCT.read(null);

        if (ict != null) { // Verifica se a lista retornada não é nula
            for (ParCategoriaTarefa e : ict) {
                if (e != null) { // Verifica se a categoria não é nula
                    // Lê a tarefa correspondente com base no ID
                    Tarefa tarefa = arqTarefas.read(e.idTarefa);

                    if (tarefa != null) {
                        // Adiciona a tarefa à lista com o ID da tarefa
                        adicionarLista(tarefa, tarefa.getId());
                    }
                }
            }
        } else {
            System.out.println("Erro");
        }
    }

    public String removerAcentos(String texto) {
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        // Remover caracteres não ASCII (acentos e cedilha)
        return textoNormalizado.replaceAll("[^\\p{ASCII}]", "");
    }

    public void adicionarLista(Tarefa tarefa, int idTarefa) throws Exception {
        float frequencia;
        int i = 0;
        String nomeTarefa = removerAcentos(tarefa.getNome());
        String[] vetNome = nomeTarefa.split(" ");
        vetNome = removerStopWords(vetNome);

        // Conjunto para verificar se a palavra já foi processada
        HashSet<String> palavrasProcessadas = new HashSet<>();

        while (i < vetNome.length) {
            String palavraAtual = vetNome[i];

            // Verifica se a palavra já foi processada
            if (!palavrasProcessadas.contains(palavraAtual)) {
                frequencia = calcularFrequencia(vetNome, palavraAtual);
                listaInvertida.create(palavraAtual, new ElementoLista(idTarefa, frequencia));
                // listaInvertida.print();

                // Adiciona a palavra ao conjunto de palavras processadas
                palavrasProcessadas.add(palavraAtual);
            }
            i++;
        }
        listaInvertida.incrementaEntidades();
    }

    public static ArrayList<ElementoLista> idSomados(ArrayList<ElementoLista> arrayList) {
        // Mapa para armazenar a soma dos valores por ID
        Map<Integer, Double> somaPorId = new HashMap<>();

        // Percorre o arrayList e acumula os valores com o mesmo ID
        for (ElementoLista elemento : arrayList) {
            int id = elemento.getId();
            double valor = elemento.getFrequencia(); // Corrigido para acessar o método getter

            somaPorId.put(id, somaPorId.getOrDefault(id, 0.0) + valor);
        }

        // Converte o mapa para um ArrayList de ElementoLista com a soma dos valores
        ArrayList<ElementoLista> resultado = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : somaPorId.entrySet()) {
            resultado.add(new ElementoLista(entry.getKey(), entry.getValue().floatValue()));
        }

        return resultado;
    }

    public void imprimirLista(ArrayList<ElementoLista> arrayList) throws Exception {
        Tarefa tarefa;
        System.out.println("Tarefas retornadas: \n");

        for (ElementoLista e : arrayList) {
            tarefa = arqTarefas.read(e.getId());
            System.out.println(tarefa.toString());
        }
    }

    public void pesquisaPorTermo(String chave) throws Exception {
        listaInvertida = new ListaInvertida(4, "dicionario.listainv.db", "blocos.listainv.db");
        adicionarLista();

        String aux = removerAcentos(chave);
        String[] vet = aux.split(" ");
        vet = removerStopWords(vet);
        float tf;
        int numeroEntidades = listaInvertida.numeroEntidades();
        ArrayList<ElementoLista> arrayList = new ArrayList<>();

        for (int i = 0; i < vet.length; i++) {
            ElementoLista[] teste = listaInvertida.read(vet[i]);

            tf = numeroEntidades / teste.length;

            for (int j = 0; j < teste.length; j++) {
                teste[j].setFrequencia(teste[j].getFrequencia() * tf);
                arrayList.add(teste[j]);
            }
        }
        arrayList = idSomados(arrayList);
        arrayList.sort(Comparator.comparing(ElementoLista::getFrequencia).reversed());

        imprimirLista(arrayList);

        listaInvertida.DeletarArquivos();
    }

}
