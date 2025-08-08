import java.io.*;
import java.util.Scanner;

public class Main {
    private static Arquivo<Tarefa> arqTarefas;

    public static void main(String[] args) {

       Scanner sc = new Scanner(System.in);


           int opcao;

           try {
               do {


                   System.out.println("KUTOVA TAREFAS 1.0");
                   System.out.println("-------");
                   System.out.println("\n> Início");
                   System.out.println("1 - Categorias");
                   System.out.println("2 - Tarefas");
                   System.out.println("0 - Sair");

                   System.out.print("Opção: ");
                   try {
                       opcao = Integer.valueOf(sc.nextLine());
                   } catch (NumberFormatException e) {
                       opcao = -1;
                   }

                   switch (opcao) {
                       case 1:
                           (new MenuCategoria()).menu();
                           break;
                       case 2:
                           (new MenuTarefas()).menu();
                           break;
                       case 0:
                           break;
                       default:
                           System.out.println("Opção inválida!");
                           break;
                   }

               } while (opcao != 0);

           }catch(Exception e){
           e.printStackTrace();
        }

       }




           }









