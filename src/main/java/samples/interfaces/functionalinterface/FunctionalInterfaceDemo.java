package samples.interfaces.functionalinterface;

public class FunctionalInterfaceDemo {
    public static void main(String args[]) {
        PrintInterface lambdaPrinter = number -> number + 5;

        PrintInterface anonymousPrinter = new PrintInterface() {
            @Override
            public int print(int number) {
                System.out.println(number + " from inside interface");
                return number;
            }
        };

        AddTwoNumbersInterface adder = new AddTwoNumbersInterface() {
            @Override
            public int add(int a, int b) {
                return a + b;
            }
        };

        int result = lambdaPrinter.print(1);
        System.out.println("Lambda result: " + result);

        result = anonymousPrinter.print(2);
        System.out.println("Anonymous class result: " + result);

        result = adder.add(10, 10);
        System.out.println("Addition result: " + result);
    }
}
