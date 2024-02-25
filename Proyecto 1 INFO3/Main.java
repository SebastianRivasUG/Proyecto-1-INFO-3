import tests.*;

public class Main {
    public static void main(String[] args) throws Exception {
        int grade1 = Test1.call(25) ? 25 : 0;
        int grade2 = Test2.call(25) ? 25 : 0;
        int grade3 = Test3.call(25) ? 25 : 0;
        int grade4 = Test4.call(25) ? 25 : 0;
        int grade5 = Test5.call(25) ? 25 : 0;

        int finalGrade = grade1 + grade2 + grade3 + grade4 + grade5;

        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Final Grade ..................................................................... " + finalGrade + "/100");
    }
}