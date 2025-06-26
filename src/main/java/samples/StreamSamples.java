package samples;

import java.util.Arrays;

public class StreamSamples {
    public static void main(String args[]) {
        Object[] integerArray = new Object[]{1l, 2, 4, 5};
        boolean allInteger = Arrays.stream(integerArray).allMatch(x -> (x instanceof Integer));
        System.out.println(allInteger);
    }
}
