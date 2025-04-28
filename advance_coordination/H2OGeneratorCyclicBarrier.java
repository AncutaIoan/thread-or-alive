/*
6. H2O Generator
Goal: Combine threads printing H (hydrogen) and O (oxygen) in the correct ratio to form water molecules.

Concepts: Grouping threads, constraint satisfaction
Tools: CyclicBarrier
*/

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class H2OGeneratorCyclicBarrier {
    private CyclicBarrier barrier = new CyclicBarrier(3);

    private Semaphore hydrogen = new Semaphore(2);
    private Semaphore oxygen = new Semaphore(1);

    public H2OGeneratorCyclicBarrier() {}

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        hydrogen.acquire();
        try {
            releaseHydrogen.run();
            barrier.await(); // Wait until 3 threads (2 H + 1 O) arrive
        } catch (BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }
        hydrogen.release();
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        oxygen.acquire();
        try {
            releaseOxygen.run();
            barrier.await();
        } catch (BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }
        oxygen.release();
    }

    public static void main(String[] args) {
        H2OGeneratorCyclicBarrier h2o = new H2OGeneratorCyclicBarrier();

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
