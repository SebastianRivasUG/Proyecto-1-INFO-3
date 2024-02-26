package transactions;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import exceptions.NodeInvalidException;

/* This class represents a single transaction. Each transaction will be 
* linked to the previous one in order to maintain consistency. Please modify
* only the specified methods
*/
public class Node {
    private static final int AMOUNT_LENGTH = 20;
    private static final int CHAR_UPPER_LIMIT = 126;
    private static final String dateRegex = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}";
    private static final Pattern pattern = Pattern.compile(dateRegex);

    private String type;
    private String date;
    private double amount;
    private String key;
    private Node previousNode;

    public Node(String type, String date, double amount, Node previousNode) throws Exception{
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.previousNode = previousNode;

        this.validateFields();
        this.link(previousNode.key);
    }

    public Node(String type, String date, double amount, String key) throws Exception{
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.previousNode = null;

        this.validateFields();
        this.link(key);
    }

    public Node getPreviousNode(){
        return this.previousNode;
    }

    public String getKey(){
        return this.key;
    }

    public String getType(){
        return this.type;
    }

    public String getDate(){
        return this.date;
    }

    public double getAmount(){
        return this.amount;
    }

    public double getBalance(){
        if(this.previousNode == null){
            if(this.type.equals("DE")){
                return this.amount;
            }else{
                return - this.amount;
            }
        }


        if(this.type.equals("DE")){
            return this.amount + this.previousNode.getBalance();
        }else{
            return this.previousNode.getBalance() - this.amount;
        }
    }

    public void setAmount(double amount){
        this.amount = amount;
    }

    public void setType(String type) throws Exception{
        this.type = type;
        this.validateFields();
    }

    public void setDate(String date) throws Exception{
        this.date = date;
        this.validateFields();
    }

    public boolean isValid(String chainKey){
        // TODO: Implement this function. Return true if this and all previous nodes are valid.
        // Otherwise, return false. HINT: Try regenerating the node key with the current values.
      
        // Verificar si el nodo actual es nulo
        if (this.previousNode == null) {
          // Validar la clave del nodo actual con la clave de la cadena
          return this.key.equals(chainKey);
        } else {
          
          String oldKey = this.previousNode.getKey();
          String encodeKey = generateNewKey(oldKey);
          if(!encodeKey.equals(this.key)){
            return false;
          } 
          
          return this.previousNode.isValid(chainKey);

          // Validar el nodo anterior recursivamente
        }
    }

    public String findInconsistency(String chainString){
        // TODO: Implement this function. Navigate trhough the chain and look for a
        // Node that is inconsistent. Then return which field was modified. Possible values are
        // 'TYPE', 'DATE', and 'AMOUNT'. If no inconsistency is found, return an empty string
      
      //Dada la chainString del nodo anterior hacemos lo siguiente: 
      //1. separamos en pedazos la Key actual del nodo siguiendo las reglas de como esta distribuida de 2 espacios para Type, 19 para date y 21 para amount
      //2.generamos los pedazos de la Key esperada con los metodos de encode y la chainstring
      //3. ya teniendo todos los pedazos, los comparamos y buscamos inconsistencias.
      if (this.previousNode == null) {
        String typeNode = this.key.substring(0,2);
        String dateNode = this.key.substring(2,21);
        String amountNode = this.key.substring(21);

        String typeEncode = encodeString(this.getType(),chainString);
        String dateEncode = encodeString(this.getDate(),chainString);
        String amountEncode = encodeDouble(this.getAmount(),chainString);

        if(!typeNode.equals(typeEncode)){
          return "TYPE";
        } else if(!dateNode.equals(dateEncode)){
          return "DATE";
        } else if(!amountNode.equals(amountEncode)){
          return "AMOUNT";
        } else {
          return "";
        }
      } 
      // Validar la clave del nodo actual con la clave generada
      String typeNode = this.key.substring(0,2);
      String dateNode = this.key.substring(2,21);
      String amountNode = this.key.substring(21);

      String typeEncode = encodeString(this.getType(),this.previousNode.getKey());
      String dateEncode = encodeString(this.getDate(),this.previousNode.getKey());
      String amountEncode = encodeDouble(this.getAmount(),this.previousNode.getKey());
      

      if(!typeNode.equals(typeEncode)){
        return "TYPE";
      } else if(!dateNode.equals(dateEncode)){
        return "DATE";
      } else if(!amountNode.equals(amountEncode)){
        return "AMOUNT";
      } else {
        return "";
      }
    }


    private void validateFields() throws Exception {
        if(! this.type.equals("DE") && ! this.type.equals("WH")){
            throw new NodeInvalidException("Invalid Transaction Type: " + this.type);
        }

        Matcher matcher = pattern.matcher(this.date);
        

        if(! matcher.matches()){
            throw new NodeInvalidException("Invalid Date: " + this.date);
        }
    }

    void link(String key) {
        this.key = this.generateNewKey(key);
    }

    public String generateNewKey(String oldKey){
      
      // TODO: Implement this function. To implement just follow these instructions:
      //Solo se siguen las instrucciones
      // 1. Encode the type using the old key
      String typeEncode = encodeString(this.getType(),oldKey);
      // 2. Encode the date using the old key
      String dateEncode = encodeString(this.getDate(),oldKey);
      // 3. Encode the amount using the oldKey
      String amountEncode = encodeDouble(this.getAmount(),oldKey);
      
      String concatEncodeKey = typeEncode + dateEncode + amountEncode;
      
      return concatEncodeKey;     
      // The new key to be returned is the concatenation of the encoded type, date and amount 
    }

    private String encodeDouble(double number, String key){
        String string =  Double.toString(number);
        String paddedString = String.format("%" + AMOUNT_LENGTH + "s", string);  

        return this.encodeString(paddedString, key);
    }

    private String encodeString(String string, String key){
        char[] chars = string.toCharArray();
        char[] newChars = new char[chars.length];
        int keyIndex = 0;

        for(int i = 0; i < chars.length; i++){
            char c = chars[i];
            int nextKeyIndex = this.getNextKeyIndex(key, keyIndex);
            int newChar = c % key.charAt(keyIndex) + key.charAt(nextKeyIndex);

            if(newChar > CHAR_UPPER_LIMIT){
                newChar = newChar % CHAR_UPPER_LIMIT;
            }
            if(newChar < 35){
                newChar = newChar + 35;
            }
            newChars[i] = (char)newChar;
            keyIndex = nextKeyIndex;
        }

        return new String(newChars);
    }

    private int getNextKeyIndex(String key, int currentKeyIndex){
        if(key.length() == (currentKeyIndex + 1)){
            return 0;
        }

        return currentKeyIndex + 1;
    }
}
