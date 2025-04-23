# Concurrency Challenges Roadmap

This roadmap walks you through a series of concurrency problems, starting with simple coordination and moving toward advanced synchronization and real-world systems.

---

## 🤖 Phase 1: Basic Coordination

### 1. Print in Order
**Goal:** Three methods (`first()`, `second()`, `third()`) must be executed in order using three different threads.
- **Concepts:** Basic sequencing
- **Tools:** `CountDownLatch`, `Semaphore`, `synchronized` + `wait/notify`

### 2. FooBar Alternation
**Goal:** Alternate between two threads printing "foo" and "bar" to produce output like `foobarfoobar...`.
- **Concepts:** Alternating thread execution
- **Tools:** `Semaphore`, `AtomicBoolean`

### 3. ZeroEvenOdd
**Goal:** Print a sequence like `010203...` using three threads (zero, even, odd).
- **Concepts:** Multi-thread sequencing
- **Tools:** `Semaphore`, shared state

---

## 🌿 Phase 2: Producer-Consumer Patterns

### 4. Bounded Buffer
**Goal:** Implement a producer-consumer system with a limited buffer.
- **Concepts:** Mutual exclusion, blocking queues
- **Tools:** `BlockingQueue`, `Semaphore`, `synchronized` + `wait/notify`

### 5. FizzBuzz with Threads
**Goal:** Four threads print Fizz, Buzz, FizzBuzz, or the number, depending on divisibility.
- **Concepts:** Conditional thread execution
- **Tools:** `Semaphore`, `Condition`, shared counter

---

## 📈 Phase 3: Advanced Coordination

### 6. H2O Generator
**Goal:** Combine threads printing H (hydrogen) and O (oxygen) in the correct ratio to form water molecules.
- **Concepts:** Grouping threads, constraint satisfaction
- **Tools:** `Semaphore`, `CyclicBarrier`, `Condition`

### 7. Dining Philosophers
**Goal:** Avoid deadlock while multiple threads (philosophers) try to pick up shared resources (chopsticks).
- **Concepts:** Deadlock prevention, resource hierarchy
- **Tools:** `ReentrantLock`, `Semaphore`, `tryLock()`

### 8. Cigarette Smokers Problem
**Goal:** Classic OS synchronization problem dealing with conditional resource availability.
- **Concepts:** Complex signaling, conditional waits
- **Tools:** `Condition`, `Lock`, `wait/notify`

---

## 🌍 Phase 4: Real-World Inspired Challenges

### 9. Elevator System Simulation
**Goal:** Simulate elevator logic with pickup/dropoff queues and directional logic.
- **Concepts:** Shared state, event queues, state machine
- **Tools:** Threads, `BlockingQueue`, locks

### 10. Rate Limiter / Token Bucket
**Goal:** Restrict API access to N calls per second.
- **Concepts:** Timed synchronization, throttling
- **Tools:** `ScheduledExecutorService`, `Semaphore`, `AtomicInteger`

---

## ✅ How to Use This Roadmap
1. Start from the top and move down as your confidence grows.
2. For each problem:
   - Understand the requirement
   - Try solving it yourself
   - Use synchronization tools mindfully
   - Review and refactor for clarity
3. Don’t hesitate to revisit earlier ones for deeper understanding.

Happy threading! 🤖

