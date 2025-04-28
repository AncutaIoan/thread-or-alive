/*
6. H2O Generator
Goal: Combine threads printing H (hydrogen) and O (oxygen) in the correct ratio to form water molecules.

Concepts: Grouping threads, constraint satisfaction
Tools: Condition

*/
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class H2OGeneratorCondition {
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private int hydrogenCount = 0;
    private int oxygenCount = 0;

    public H2OGeneratorCondition() {}

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        lock.lock();
        try {
            while (hydrogenCount == 2) {
                condition.await();
            }
            releaseHydrogen.run();
            hydrogenCount++;
            if (hydrogenCount == 2 && oxygenCount == 1) {
                reset();
            }
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        lock.lock();
        try {
            while (oxygenCount == 1) {
                condition.await();
            }
            releaseOxygen.run();
            oxygenCount++;
            if (hydrogenCount == 2 && oxygenCount == 1) {
                reset();
            }
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private void reset() {
        hydrogenCount = 0;
        oxygenCount = 0;
    }

    public static void main(String[] args) {
        H2OGeneratorCondition h2o = new H2OGeneratorCondition();

        Runnable hydrogen = () -> System.out.print("H");
        Runnable oxygen = () -> System.out.print("O");

        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                try {
                    h2o.hydrogen(hydrogen);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    h2o.oxygen(oxygen);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
}
