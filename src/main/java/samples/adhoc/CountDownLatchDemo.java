package samples.adhoc;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);

        int i = 0;
        while (countDownLatch.getCount() > 0) {
            System.out.println("Hi " + i + " | Latch count: " + countDownLatch.getCount());
            countDownLatch.countDown();
            i++;
        }

        System.out.println("Latch count reached zero. Done!");
    }
}
