package tests;

import java.text.DecimalFormat;

import transactions.*;
import exceptions.*;

public class Test5 {
    public static boolean call(int testPoints) throws Exception {
        System.out.println("---------------------------------------- TEST 05 ----------------------------------------");
        Chain chain = new Chain("PRUEBANUMERO5");
        System.out.println("Initializing chain with key: 'PRUEBANUMERO5' .................................... TEST OK");
        chain.addTransaction("WH", "2024-08-12 21:44:04", 100.00);
        chain.addTransaction("WH", "2024-08-13 21:44:04", 200.00);
        chain.addTransaction("WH", "2024-08-14 21:44:04", 110.51);
        chain.addTransaction("DE", "2024-08-15 21:44:04", 2500.33);
        chain.addTransaction("DE", "2024-08-16 21:44:04", 2000.60);
        chain.addTransaction("WH", "2024-08-16 21:44:04", 777.77);
        System.out.println("Adding several transactions (6).................................................. TEST OK");
        if(chain.isValid()){
            System.out.println("Verifying chain validity. It should be valid .................................... TEST OK");
        }else {
            System.out.println("Verifying chain validity. It should be valid ................................ TEST FAILED");
            return fail();
        }

        DecimalFormat df = new DecimalFormat("#.##");      
        double balance = Double.valueOf(df.format(chain.getBalance()));
        if(balance == 3312.65){
            System.out.println("Verifying chain balance. It should be 3312.65 .................................. TEST OK");
        }else{
            System.out.println("Verifying chain balance. It should be 3312.65 .............................. TEST FAILED");
            return fail();
        }

      int targetTransactionIndex = 3; 
      try{
        chain.transactionAt(targetTransactionIndex).setDate("2024-08-25");
      } catch(NodeInvalidException e){
        System.out.println("todo controlado");
      }
      if(chain.isValid()){
          System.out.println("Verifying chain validity after manual alteration. It should be invalid ...... TEST FAILED");
          return fail();
      }else{
          System.out.println("Verifying chain validity after manual alteration. It should be invalid .......... TEST OK");
      }

      Node inconsistentNode = chain.firstInconsistency();
      if (inconsistentNode.getKey().equals(chain.transactionAt(targetTransactionIndex).getKey())) {
          System.out.println("Checking inconsistent transaction found. It should be the " + (targetTransactionIndex + 1) + "th ............. TEST OK");
      } else {
          System.out.println("Checking inconsistent transaction found. It should be the " + (targetTransactionIndex + 1) + "th ......... TEST FAILED");
          return fail();
      }

        String inconsistentField = chain.findInconsistentField(inconsistentNode);
        if(inconsistentField.equals("DATE")){
            System.out.println("Validating inconsistent field name. It should be DATE ........................... TEST OK");
        } else {
            System.out.println("Validating inconsistent field name. It should be DATE ....................... TEST FAILED");
            return fail();
        }

        return success(testPoints);
    }

    private static boolean success(int testPoints) {
        System.out.println("TEST PASSED ..................................................................... +" + testPoints + " pts");
        return true;
    }

    private static boolean fail(){
        System.out.println("TEST FAILED ...................................................................... +0 pts");
        return false;

    }
}
