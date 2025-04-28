/*
3. ZeroEvenOdd
Goal: Print a sequence like 010203... using three threads (zero, even, odd).

Concepts: Multi-thread sequencing
Tools: Semaphore, shared state
*/
import java.util.concurrent.Semaphore;

public class ZeroEvenOddSemaphore {
    private int n;
    private Semaphore zeroSem = new Semaphore(1);  // zero starts
    private Semaphore oddSem = new Semaphore(0);
    private Semaphore evenSem = new Semaphore(0);

    public ZeroEvenOddSemaphore(int n) {
        this.n = n;
    }

    public void zero() {
        for (int i = 1; i <= n; i++) {
            try {
                zeroSem.acquire();
                System.out.print(0);
                if (i % 2 == 0) {
                    evenSem.release();
                } else {
                    oddSem.release();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void even() {
        for (int i = 2; i <= n; i += 2) {
            try {
                evenSem.acquire();
                System.out.print(i);
                zeroSem.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void odd() {
        for (int i = 1; i <= n; i += 2) {
            try {
                oddSem.acquire();
                System.out.print(i);
                zeroSem.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        ZeroEvenOddSemaphore zeroEvenOdd = new ZeroEvenOddSemaphore(5);

        Thread t0 = new Thread(zeroEvenOdd::zero);
        Thread t1 = new Thread(zeroEvenOdd::even);
        Thread t2 = new Thread(zeroEvenOdd::odd);

        t0.start();
        t1.start();
        t2.start();
    }
}
