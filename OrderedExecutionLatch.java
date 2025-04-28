import java.util.concurrent.CountDownLatch;

public class OrderedExecutionLatch {
    private CountDownLatch latch1 = new CountDownLatch(1);
    private CountDownLatch latch2 = new CountDownLatch(1);

    public void first() {
        System.out.println("First");
        latch1.countDown();  // Allow second() to proceed
    }

    public void second() {
        try {
            latch1.await();  // Wait for first() to finish
            System.out.println("Second");
            latch2.countDown();  // Allow third() to proceed
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void third() {
        try {
            latch2.await();  // Wait for second() to finish
            System.out.println("Third");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        OrderedExecutionLatch order = new OrderedExecutionLatch();

        Thread t1 = new Thread(order::first);
        Thread t2 = new Thread(order::second);
        Thread t3 = new Thread(order::third);

        t3.start();
        t2.start();
        t1.start();
    }
}
