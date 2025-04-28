/*
8. Cigarette Smokers Problem
Goal: Classic OS synchronization problem dealing with conditional resource availability.

Concepts: Complex signaling, conditional waits
Tools: Condition, Lock, wait/notify
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class CigaretteSmokers {
    private final Lock lock = new ReentrantLock();
    private final Condition smokerTobacco = lock.newCondition();
    private final Condition smokerPaper = lock.newCondition();
    private final Condition smokerMatches = lock.newCondition();
    private final Condition agent = lock.newCondition();

    private boolean isTobacco = false;
    private boolean isPaper = false;
    private boolean isMatches = false;

    private final Random random = new Random();

    public void start() {
        new Thread(this::agent).start();
        new Thread(this::smokerWithTobacco).start();
        new Thread(this::smokerWithPaper).start();
        new Thread(this::smokerWithMatches).start();
    }

    private void agent() {
        while (true) {
            lock.lock();
            try {
                while (isTobacco || isPaper || isMatches) {
                    agent.await(); // Wait until table is cleared
                }
                int choice = random.nextInt(3);
                switch (choice) {
                    case 0:
                        // Place paper + matches → smoker with tobacco should act
                        isPaper = true;
                        isMatches = true;
                        System.out.println("[Agent] puts Paper and Matches");
                        smokerTobacco.signal();
                        break;
                    case 1:
                        // Place tobacco + matches → smoker with paper should act
                        isTobacco = true;
                        isMatches = true;
                        System.out.println("[Agent] puts Tobacco and Matches");
                        smokerPaper.signal();
                        break;
                    case 2:
                        // Place tobacco + paper → smoker with matches should act
                        isTobacco = true;
                        isPaper = true;
                        System.out.println("[Agent] puts Tobacco and Paper");
                        smokerMatches.signal();
                        break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }

            try {
                Thread.sleep(500); // Let smoker act
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void smokerWithTobacco() {
        while (true) {
            lock.lock();
            try {
                while (!(isPaper && isMatches)) {
                    smokerTobacco.await();
                }
                // Got what he needs
                System.out.println("[Smoker-Tobacco] makes and smokes a cigarette!");
                isPaper = false;
                isMatches = false;
                agent.signal(); // Tell agent to put new items
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }

            try {
                Thread.sleep(1000); // Smoking time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void smokerWithPaper() {
        while (true) {
            lock.lock();
            try {
                while (!(isTobacco && isMatches)) {
                    smokerPaper.await();
                }
                System.out.println("[Smoker-Paper] makes and smokes a cigarette!");
                isTobacco = false;
                isMatches = false;
                agent.signal();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void smokerWithMatches() {
        while (true) {
            lock.lock();
            try {
                while (!(isTobacco && isPaper)) {
                    smokerMatches.await();
                }
                System.out.println("[Smoker-Matches] makes and smokes a cigarette!");
                isTobacco = false;
                isPaper = false;
                agent.signal();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        new CigaretteSmokers().start();
    }
}
