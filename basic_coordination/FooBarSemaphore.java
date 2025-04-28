/*
2. FooBar Alternation
Goal: Alternate between two threads printing "foo" and "bar" to produce output like foobarfoobar....

Concepts: Alternating thread execution
Tools: Semaphore, AtomicBoolean
*/
import java.util.concurrent.Semaphore;

public class FooBarSemaphore {
    private int n;
    private Semaphore fooSem = new Semaphore(1); // Start with foo
    private Semaphore barSem = new Semaphore(0); // bar must wait

    public FooBarSemaphore(int n) {
        this.n = n;
    }

    public void foo() {
        for (int i = 0; i < n; i++) {
            try {
                fooSem.acquire();
                System.out.print("foo");
                barSem.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void bar() {
        for (int i = 0; i < n; i++) {
            try {
                barSem.acquire();
                System.out.print("bar");
                fooSem.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        FooBarSemaphore fooBar = new FooBarSemaphore(5);

        Thread t1 = new Thread(fooBar::foo);
        Thread t2 = new Thread(fooBar::bar);

        t1.start();
        t2.start();
    }
}
