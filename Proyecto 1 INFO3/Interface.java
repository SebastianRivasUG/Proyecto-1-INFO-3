import java.util.Scanner;
import java.util.LinkedList;
import transactions.*;
import exceptions.*;
import java.io.*;

public class Interface {
    private static LinkedList<Chain> chains = new LinkedList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static BufferedReader tec = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws NodeInvalidException{
        while (true) {
            printMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    createNewChain();
                    break;
                case 2:
                    viewAllChains();
                    break;
                case 3:
                    addTransactionToChain();
                    break;
                case 4:
                    getBalanceOfChain();
                    break;
                case 5:
                    listAllTransactions();
                    break;
                case 6:
                    verifyChainValidity();
                    break;
                case 0:
                    System.out.println("\nSaliendo del programa...");
                    System.out.println("¡Gracias por usar!");
                    System.out.println("**********************************************************");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n**********************************************************");
        System.out.println("-------------------------- Menú --------------------------");
        System.out.println("1. Crear una nueva cadena");
        System.out.println("2. Ver todas las cadenas");
        System.out.println("3. Agregar una transacción a una cadena existente");
        System.out.println("4. Obtener el balance de la cadena");
        System.out.println("5. Listar todas las transacciones ordenadas de una cadena");
        System.out.println("6. Verificar si una cadena es válida");
        System.out.println("0. Salir");
        System.out.println("----------------------------------------------------------");
    }

    private static int getUserChoice() {
      while(true){
          try {
              System.out.print("Ingrese el número de la opción deseada: ");
              return scanner.nextInt();
          } catch (Exception e){
              System.out.println("\nError, Ingrese únicamente un número");
              System.out.println("Números válidos: 1, 2, 3, 4, 5, 6, 0\n");
              scanner.nextLine();
          }
      }
        
    }

    private static void createNewChain() {
        System.out.print("Porfavor ingrese la Chain Key para la nueva cadena: ");
        String chainKey = scanner.next();
        Chain newChain = new Chain(chainKey);
        chains.add(newChain);
        System.out.println("Nueva cadena creada exitosamente.\n");
    }

    private static void viewAllChains() {
        System.out.println("\n------------------- Todas las Cadenas --------------------");
        for (int i = 1; i <= chains.size(); i++) {
            System.out.println("Cadena " + (i) + ": ");
            System.out.print(chains.get(i-1).toString());
            System.out.print("\n");
        }
        System.out.println("\n----------------------------------------------------------\n\n");
    }

    private static void addTransactionToChain() {
        try {
            viewAllChains();

            System.out.print("Seleccione el número de la cadena a la que desea agregar la transacción: ");
            int chainIndex = scanner.nextInt() - 1;

            if (chainIndex < 0 || chainIndex >= chains.size()) {
                System.out.println("Cadena no válida.");
                return;
            }

            Chain selectedChain = chains.get(chainIndex);
            String typeNode = getUserInput("Indique el tipo de transacción (DE/WH): ");
            String dateNode = getUserInput("\n Indique la fecha de la transacción (AAAA-MM-DD HH:mm:ss): ");
            
            Double amount = getDoubleInput("Indique el monto de la transacción (#.##): ");

            selectedChain.addTransaction(typeNode, dateNode, amount);
            if(selectedChain.isValid()){
              System.out.println("Transacción agregada exitosamente a la cadena seleccionada.\n");
            } 

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String getUserInput(String prompt) throws Exception{
        System.out.print(prompt);
        return tec.readLine().trim();
    }

    private static Double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.next());
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");
            }
        }
    }


    private static void getBalanceOfChain() {
        viewAllChains();
        System.out.print("Seleccione el número de la cadena de la que desea obtener el balance: ");
        int chainIndex = scanner.nextInt() - 1;
        if (chainIndex >= 0 && chainIndex < chains.size()) {
            Chain selectedChain = chains.get(chainIndex);
            System.out.println("Balance de la cadena seleccionada: " + selectedChain.getBalance() + "\n");

        } else {
            System.out.println("Cadena no válida.");
        }  
    }

    private static void listAllTransactions() {
        viewAllChains();
        System.out.print("Seleccione el número de la cadena de la que desea listar las transacciones: ");
        int chainIndex = scanner.nextInt() - 1;
        if (chainIndex < 0 || chainIndex >= chains.size()) {
            System.out.println("Cadena no válida.");
            return;
        }

        Chain selectedChain = chains.get(chainIndex);
        int sizeOfTheChain = selectedChain.getSize();
        System.out.println("------------------- Transacciones de la Cadena --------------------");
        for (int i = 0; i < sizeOfTheChain; i++){
          System.out.println("Transacción " + (i+1) + ": " + selectedChain.transactionAt(i));
        }
    }

    private static void verifyChainValidity() {
        viewAllChains();
        System.out.print("Seleccione el número de la cadena que desea verificar: ");
        int chainIndex = scanner.nextInt() - 1;
        if (chainIndex < 0 || chainIndex >= chains.size()) {
            System.out.println("Cadena no válida.");
            return;
        }

        Chain selectedChain = chains.get(chainIndex);

        System.out.println("La cadena seleccionada es " + selectedChain.isValid() + ".");
    }
}
