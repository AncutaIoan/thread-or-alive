/*
7. Dining Philosophers
Goal: Avoid deadlock while multiple threads (philosophers) try to pick up shared resources (chopsticks).

Concepts: Deadlock prevention, resource hierarchy
Tools: ReentrantLock, Semaphore, tryLock()
 */
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophersTryLock {
    private final Lock[] chopsticks = new ReentrantLock[5];

    public DiningPhilosophersTryLock() {
        for (int i = 0; i < 5; i++) {
            chopsticks[i] = new ReentrantLock();
        }
    }

    public void wantsToEat(int philosopher,
                           Runnable pickLeftChopstick,
                           Runnable pickRightChopstick,
                           Runnable eat,
                           Runnable putLeftChopstick,
                           Runnable putRightChopstick) throws InterruptedException {
        Lock left = chopsticks[philosopher];
        Lock right = chopsticks[(philosopher + 1) % 5];

        while (true) {
            if (left.tryLock()) {
                if (right.tryLock()) {
                    // Successfully picked both
                    pickLeftChopstick.run();
                    pickRightChopstick.run();
                    eat.run();
                    putRightChopstick.run();
                    putLeftChopstick.run();
                    right.unlock();
                    left.unlock();
                    break;
                } else {
                    // Failed to pick right, release left and retry
                    left.unlock();
                }
            }
            // Short sleep to avoid busy waiting
            Thread.sleep(1);
        }
    }

    public static void main(String[] args) {
        DiningPhilosophersTryLock philosophers = new DiningPhilosophersTryLock();

        for (int i = 0; i < 5; i++) {
            int id = i;
            new Thread(() -> {
                try {
                    philosophers.wantsToEat(id,
                            () -> System.out.print(id + " picks left, "),
                            () -> System.out.print(id + " picks right, "),
                            () -> System.out.print(id + " eats, "),
                            () -> System.out.print(id + " puts left, "),
                            () -> System.out.print(id + " puts right. "));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
}
