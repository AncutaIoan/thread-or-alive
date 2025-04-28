/*
4. Bounded Buffer
Goal: Implement a producer-consumer system with a limited buffer.

Concepts: Mutual exclusion, blocking queues
Tools: BlockingQueue, Semaphore, synchronized + wait/notify
*/

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BoundedBufferBlockingQueue {
    private BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5); // buffer size 5

    public void produce(int item) {
        try {
            queue.put(item);  // blocks if queue is full
            System.out.println("Produced: " + item);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void consume() {
        try {
            int item = queue.take();  // blocks if queue is empty
            System.out.println("Consumed: " + item);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        BoundedBufferBlockingQueue buffer = new BoundedBufferBlockingQueue();

        Runnable producer = () -> {
            for (int i = 0; i < 10; i++) {
                buffer.produce(i);
            }
        };

        Runnable consumer = () -> {
            for (int i = 0; i < 10; i++) {
                buffer.consume();
            }
        };

        new Thread(producer).start();
        new Thread(consumer).start();
    }
}
