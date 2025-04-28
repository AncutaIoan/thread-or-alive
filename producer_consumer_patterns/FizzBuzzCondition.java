/*
5. FizzBuzz with Threads
Goal: Four threads print Fizz, Buzz, FizzBuzz, or the number, depending on divisibility.

Concepts: Conditional thread execution
Tools: Semaphore, Condition, shared counter
*/

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FizzBuzzCondition {
    private int n;
    private int current = 1;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public FizzBuzzCondition(int n) {
        this.n = n;
    }

    public void fizz() {
        while (true) {
            lock.lock();
            try {
                while (current <= n && !(current % 3 == 0 && current % 5 != 0)) {
                    condition.await();
                }
                if (current > n) break;
                System.out.println("Fizz");
                current++;
                condition.signalAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    public void buzz() {
        while (true) {
            lock.lock();
            try {
                while (current <= n && !(current % 5 == 0 && current % 3 != 0)) {
                    condition.await();
                }
                if (current > n) break;
                System.out.println("Buzz");
                current++;
                condition.signalAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    public void fizzbuzz() {
        while (true) {
            lock.lock();
            try {
                while (current <= n && !(current % 15 == 0)) {
                    condition.await();
                }
                if (current > n) break;
                System.out.println("FizzBuzz");
                current++;
                condition.signalAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    public void number() {
        while (true) {
            lock.lock();
            try {
                while (current <= n && (current % 3 == 0 || current % 5 == 0)) {
                    condition.await();
                }
                if (current > n) break;
                System.out.println(current);
                current++;
                condition.signalAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        FizzBuzzCondition fb = new FizzBuzzCondition(15);

        new Thread(fb::fizz).start();
        new Thread(fb::buzz).start();
        new Thread(fb::fizzbuzz).start();
        new Thread(fb::number).start();
    }
}
