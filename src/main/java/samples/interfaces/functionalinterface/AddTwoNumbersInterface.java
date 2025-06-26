package samples.interfaces.functionalinterface;

@FunctionalInterface
public interface AddTwoNumbersInterface {
    int add(int a, int b);

    default void printResult(int result) {
        System.out.println("Addition result " + result);
    }

}
