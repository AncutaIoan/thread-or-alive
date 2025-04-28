/*
7. Dining Philosophers
Goal: Avoid deadlock while multiple threads (philosophers) try to pick up shared resources (chopsticks).

Concepts: Deadlock prevention, resource hierarchy
Tools: ReentrantLock, Semaphore, tryLock()
 */
import java.util.concurrent.Semaphore;

public class DiningPhilosophersSemaphore {
    private final Semaphore[] chopsticks = new Semaphore[5];
    private final Semaphore maxSeats = new Semaphore(4); // Only 4 allowed at once

    public DiningPhilosophersSemaphore() {
        for (int i = 0; i < 5; i++) {
            chopsticks[i] = new Semaphore(1);
        }
    }

    public void wantsToEat(int philosopher,
                           Runnable pickLeftChopstick,
                           Runnable pickRightChopstick,
                           Runnable eat,
                           Runnable putLeftChopstick,
                           Runnable putRightChopstick) throws InterruptedException {
        maxSeats.acquire(); // Try to sit

        chopsticks[philosopher].acquire();
        chopsticks[(philosopher + 1) % 5].acquire();

        pickLeftChopstick.run();
        pickRightChopstick.run();
        eat.run();
        putRightChopstick.run();
        putLeftChopstick.run();

        chopsticks[philosopher].release();
        chopsticks[(philosopher + 1) % 5].release();

        maxSeats.release(); // Leave table
    }

    public static void main(String[] args) {
        DiningPhilosophersSemaphore philosophers = new DiningPhilosophersSemaphore();

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
