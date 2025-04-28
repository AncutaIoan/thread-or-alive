/*
6. H2O Generator
Goal: Combine threads printing H (hydrogen) and O (oxygen) in the correct ratio to form water molecules.

Concepts: Grouping threads, constraint satisfaction
Tools: Semaphore, CyclicBarrier, Condition
*/
import java.util.concurrent.Semaphore;

public class H2OGeneratorSemaphore {
    private Semaphore hydrogen = new Semaphore(2);
    private Semaphore oxygen = new Semaphore(1);
    private Semaphore ready = new Semaphore(0);

    public H2OGeneratorSemaphore() {}

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        hydrogen.acquire();
        releaseHydrogen.run();
        ready.release();
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        oxygen.acquire();
        ready.acquire(2); // Wait for two hydrogens
        releaseOxygen.run();
        hydrogen.release(2); // Reset for next water molecule
        oxygen.release(1);
    }

    public static void main(String[] args) {
        H2OGeneratorSemaphore h2o = new H2OGeneratorSemaphore();

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
