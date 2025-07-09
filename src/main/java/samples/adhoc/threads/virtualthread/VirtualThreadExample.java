package samples.adhoc.threads.virtualthread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;



public class VirtualThreadExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // startASingleVirtualThread();
        startManyVirtualThreadsViaCallablesAndPrint(10000, false);
        startManyPlatformThreadsViaCallablesAndPrint(10000, false);

    }

    private static void startManyVirtualThreadsViaCallablesAndPrint(int num, boolean print) throws ExecutionException, InterruptedException {
        long stime = System.currentTimeMillis();
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        List<Future<String>> futureList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            int taskId = i;
            Callable run = new SampleCallableWithSleep(taskId + "");
            Future<String> future = executor.submit(run);
            futureList.add(future);
        }

        for (Future<String> f : futureList) {
            if (print) {
                System.out.println(f.get());
            } else {
                f.get();
            }
        }
        System.out.println("Total time for virtual threads : " + (System.currentTimeMillis() - stime));
    }

    private static void startManyPlatformThreadsViaCallablesAndPrint(int num, boolean print) throws ExecutionException, InterruptedException {
        long stime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(1);

        List<Future<String>> futureList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            int taskId = i;
            Callable run = new SampleCallableWithSleep(taskId + "");
            Future<String> future = executor.submit(run);
            futureList.add(future);
        }

        for (Future<String> f : futureList) {
            if (print) {
                System.out.println(f.get());
            } else {
                f.get();
            }
        }
        System.out.println("Total time for platform threads : " + (System.currentTimeMillis() - stime));
    }

    private static void startASingleVirtualThread() {
        String taskId = "1";
        Thread.startVirtualThread(() -> {
            System.out.println("Started virtual thread " + taskId);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Finished virtual thread " + taskId);
        });
    }
}

/*
Absolutely. Here's a **detailed explanation** of **when to use virtual threads**, not just a summary — but with enough clarity for real-world application decisions.

---

## 🎯 **When to Use Virtual Threads (in Detail)**

Virtual threads (from Project Loom, Java 21+) are designed to **dramatically improve concurrency and simplify code** for applications that spend a lot of time waiting (i.e., are **I/O-bound**).

Let’s go use case by use case:

---

### ✅ 1. **When You Have a High Volume of Concurrent I/O-bound Tasks**

If your application needs to handle thousands (or even millions) of tasks that mostly wait on:

* **HTTP calls**
* **Database queries**
* **File I/O**
* **Socket communication**

…then virtual threads are a **perfect fit**.

**Why?**

* Virtual threads are **cheap to create** (\~KBs of stack memory vs \~MBs for platform threads).
* They don’t block carrier threads while waiting — freeing them for other work.
* You can write simple, blocking-style code (no complex async chains or callbacks).

📌 **Real-world examples**:

* Web servers or REST APIs handling thousands of clients (e.g., chat app).
* Web scrapers making massive parallel HTTP calls.
* Data processing pipelines that fetch from APIs or DBs.

---

### ✅ 2. **When You Want Simpler, Blocking-Style Code Without Sacrificing Scalability**

Before Loom, developers had to choose between:

* ✅ **Blocking code** — simple, but scales poorly.
* ⚠️ **Async/reactive code** — scales well, but complex and error-prone.

With virtual threads, you can now:

* Write readable `Thread.sleep()`, `db.query()`, or `httpClient.send()` code.
* Get performance that rivals (or exceeds) reactive code.

📌 **Ideal if you want to escape callback hell, reactive pipelines, or CompletableFuture chains.**

---

### ✅ 3. **When Tasks Are Independent and Don't Share State**

If your app consists of **lots of small, isolated tasks**, each with its own flow (like a request handler), virtual threads shine:

* No need to manage thread reuse.
* Each task can fail independently.
* JVM handles scheduling.

📌 **Example**: Background jobs, request processing, microservices.

---

### ✅ 4. **When Platform Thread Limits Are a Bottleneck**

Traditional Java apps can’t handle more than \~10,000–50,000 threads (due to OS thread/memory limits).

With virtual threads, you can scale to **millions of concurrent threads** — provided:

* Each task doesn’t use too much heap or native memory.
* Most threads are waiting (not doing heavy CPU work).

📌 **Ideal for server apps with unpredictable traffic peaks.**

---

## ⚠️ **When NOT to Use Virtual Threads**

While they’re powerful, virtual threads are **not a silver bullet**.

Avoid them in these cases:

---

### ❌ 1. **When Tasks Are CPU-Bound**

If your task spends most time in a CPU loop — e.g.:

```java
for (int i = 0; i < 1_000_000_000; i++) {
    sum += i * i;
}
```

…then virtual threads **won’t help** — they just add scheduling overhead.

Instead:

* Use a **bounded platform thread pool** (`Executors.newFixedThreadPool`)
* Control concurrency based on available CPU cores

📌 **Examples**: video encoding, number crunching, ML inference.

---

### ❌ 2. **When Dependencies Don’t Support Loom Safely**

Some older libraries:

* Use native blocking calls under the hood (e.g., old JDBC drivers)
* Aren’t virtual-thread-aware (might block carrier threads)

If you call such code from virtual threads, performance can still suffer.

📌 **Mitigation**: Use Loom-compatible drivers (like for PostgreSQL) or async libraries if needed.

---

### ❌ 3. **When You Need Real-time Guarantees or OS-Level Control**

Virtual threads are:

* **Scheduled by JVM**, not OS.
* Not suitable for apps needing **real-time latency**, strict **thread priority**, or **hardware affinity**.

📌 **Use case example**: high-frequency trading, real-time embedded systems.

---

## 🧠 Key Insight

> **Use virtual threads when your bottleneck is “waiting” rather than “working”.**

---

## 🧪 How to Know If Your App Will Benefit

Ask yourself:

* Does my app make **lots of blocking calls** (HTTP, DB)?
* Is my code full of `CompletableFuture`, `.thenCompose`, or callbacks just to make it non-blocking?
* Do I wish I could just write simple `call()` or `run()` methods for concurrent tasks?
* Does my current app **crash or slow down** with 5,000+ concurrent threads?

If yes — virtual threads are **likely** the right fit.

---

Would you like help converting an existing multi-threaded or reactive codebase to use virtual threads effectively?
 */