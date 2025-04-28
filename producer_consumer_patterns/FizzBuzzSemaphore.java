/*
5. FizzBuzz with Threads
Goal: Four threads print Fizz, Buzz, FizzBuzz, or the number, depending on divisibility.

Concepts: Conditional thread execution
Tools: Semaphore, Condition, shared counter
*/
import java.util.concurrent.Semaphore;

public class FizzBuzzSemaphore {
    private int n;
    private int current = 1;
    private Semaphore fizz = new Semaphore(0);
    private Semaphore buzz = new Semaphore(0);
    private Semaphore fizzbuzz = new Semaphore(0);
    private Semaphore number = new Semaphore(1); // start with number thread

    public FizzBuzzSemaphore(int n) {
        this.n = n;
    }

    public void fizz() {
        while (true) {
            try {
                fizz.acquire();
                if (current > n) break;
                System.out.println("Fizz");
                next();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void buzz() {
        while (true) {
            try {
                buzz.acquire();
                if (current > n) break;
                System.out.println("Buzz");
                next();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void fizzbuzz() {
        while (true) {
            try {
                fizzbuzz.acquire();
                if (current > n) break;
                System.out.println("FizzBuzz");
                next();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void number() {
        while (true) {
            try {
                number.acquire();
                if (current > n) {
                    // Release others to prevent deadlock
                    fizz.release();
                    buzz.release();
                    fizzbuzz.release();
                    break;
                }
                System.out.println(current);
                next();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void next() {
        current++;
        if (current > n) {
            fizz.release();
            buzz.release();
            fizzbuzz.release();
            number.release();
        } else if (current % 3 == 0 && current % 5 == 0) {
            fizzbuzz.release();
        } else if (current % 3 == 0) {
            fizz.release();
        } else if (current % 5 == 0) {
            buzz.release();
        } else {
            number.release();
        }
    }

    public static void main(String[] args) {
        FizzBuzzSemaphore fb = new FizzBuzzSemaphore(15);

        new Thread(fb::fizz).start();
        new Thread(fb::buzz).start();
        new Thread(fb::fizzbuzz).start();
        new Thread(fb::number).start();
    }
}
