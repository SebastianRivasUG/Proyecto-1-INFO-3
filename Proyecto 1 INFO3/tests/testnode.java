package tests;

import transactions.Node;
import exceptions.NodeInvalidException;

import java.io.*;

public class testnode{

  private String type;
  private String date;
  private double amount;
  private String key;

  BufferedReader tec = new BufferedReader(new InputStreamReader(System.in));

  public void pedirDatos() throws Exception {
    System.out.print("Ingrese tipo de transacción: ");
    this.type = tec.readLine();
    System.out.print("\n Ingrese tipo de transacción: ");
    this.date = tec.readLine();
    System.out.print("\n Ingrese tipo de transacción: ");
    String amountS = tec.readLine();
    this.amount = Double.parseDouble(amountS);
    System.out.print("\n String inicial (PRUEBA) : asdiuashdi38337asckn");
    this.key = "asdiuashdi38337asckn";

    Node test = new Node(this.type, this.date, this.amount, this.key);

    String newKey = test.generateNewKey(this.key);
  
    System.out.println("clave nueva gen: " + newKey);
  }
  
  public static void main(String[] prueba) throws Exception{
    testnode testN = new testnode();
    testN.pedirDatos();
  }
  
}