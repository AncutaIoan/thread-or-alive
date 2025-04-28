/*
7. Dining Philosophers
Goal: Avoid deadlock while multiple threads (philosophers) try to pick up shared resources (chopsticks).

Concepts: Deadlock prevention, resource hierarchy
Tools: ReentrantLock, Semaphore, tryLock()
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophersHierarchy {
    private final Lock[] chopsticks = new ReentrantLock[5];

    public DiningPhilosophersHierarchy() {
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
        int leftIndex = philosopher;
        int rightIndex = (philosopher + 1) % 5;

        int first = Math.min(leftIndex, rightIndex);
        int second = Math.max(leftIndex, rightIndex);

        chopsticks[first].lock();
        chopsticks[second].lock();

        pickLeftChopstick.run();
        pickRightChopstick.run();
        eat.run();
        putRightChopstick.run();
        putLeftChopstick.run();

        chopsticks[second].unlock();
        chopsticks[first].unlock();
    }

    public static void main(String[] args) {
        DiningPhilosophersHierarchy philosophers = new DiningPhilosophersHierarchy();

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
