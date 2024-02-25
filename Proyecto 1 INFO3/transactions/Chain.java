package transactions;

import java.util.LinkedList;

import javax.swing.text.DefaultStyledDocument.ElementSpec;

/* This class represents a chain of transactions. Each transaction will be 
* linked to the previous one in order to maintain consistency. Please modify
* only the specified methods
*/
public class Chain {
    LinkedList<Node> transactions;
    private String chainKey;

    public Chain(String chainKey){
        this.chainKey = chainKey;
        this.transactions = new LinkedList<Node>();
    }

    public Node findByIndex(int index){
        if(index >= 0 && this.transactions.size() < index){
            this.transactions.get(index);
        }

        return null;
    }

    public Node transactionAt(int index){
        return this.transactions.get(index);
    }
    
    public Node findByKey(String key){
        for(int i = 0; i < this.transactions.size(); i++){
            Node node = this.transactions.get(i);

            if(node.getKey().equals(key)){
                return node;
            }
        }

        return null;
    }

    public double getBalance(){
        // TODO: Implement this function. Return the balance
        // of the chain only if it is valid. If it is invalid, return 0.0
        if (this.isValid()) {
            // Inicializamos el balance en 0.0
            double balance = 0.0;
            // Iteramos sobre la lista de transacciones para calcular el balance
            for (Node node : this.transactions) {
                if(node.getType().toUpperCase().equals("DE")){
                    balance += node.getAmount();
                }
                else if(node.getType().toUpperCase().equals("WH")){
                    balance -= node.getAmount();
                }
            }
            return balance;
        } 
        return 0.0;
    }

    public boolean isValid(){
      // Verificar si la cadena está vacía
      if (transactions.isEmpty()) {
          return false;
      }

      // Obtener la cadena de clave del primer nodo
      String chainKey = transactions.getFirst().generateNewKey(this.chainKey);

      // Validar cada nodo en la cadena
      for (Node node : transactions) {
          if (!node.isValid(chainKey)) {
              return false;
          }
      }

      // Todos los nodos son válidos
      return true;
    }

    public Node firstInconsistency() {
      if(transactions.isEmpty()){
        return null;
      }

      String firstKey = this.transactions.getFirst().generateNewKey(this.chainKey);
      
      for(Node nodo : transactions){
        if(!nodo.isValid(firstKey)){
          return nodo;
        }
      }
      return null;
      // TODO: Imeplement this function. Navigate through all nodes in the
      // chain, finding the first one that has an inconsistency, and return it.
      // If no node is found, return null

      // Obtenemos el último nodo de la lista de transacciones porque isValid() en Node.java es recursivo
      // Nos movemos de nodo en nodo hasta encontrar alguno cuya key sea inconsistente.
      // Usando un método recursivo parecido al de isValid() en Node.java
      //return lastNode.findInconsistentNode(this.chainKey);
    }

    public String findInconsistentField(Node node){
        return node.findInconsistency(this.chainKey);
    }

    public void addTransaction(String type, String date, double amount) throws Exception{
        if(this.transactions.size() == 0){
            this.transactions.add(new Node(type, date, amount, this.chainKey));
        }else{
            Node previousNode = this.transactions.getLast();
            this.transactions.add(new Node(type, date, amount, previousNode));
        }
    }
}
