# Coroutines
Repo to Demo Coroutines in Kotlin

Threads vs Dispatchers - Coroutines

Threads
1. Threads
    1. An independent flow of execution.
    2. A single flow of execution in your program that allows you to call blocking code (ex: functions that could take a while to complete) without blocking code from other threads
    3. One thread can host a lot of child coroutines
    4. Those coroutines can still run independently
    5. We can have a single thread make two asynchronous calls with two asynchronous coroutines

2. Parallelism - executing something at the exact same time.
    1. In order to achieve true parallelism, you need a device or system with multiple cores.
        1. Example: Laptops with 8 efficiency cores, 6 performant.
        2. If you have an 8-core CPU, the machine can only execute 8 instructions at a time.
    2. Machines can execute multiple tasks at one time due to multiple reasons
        1. Some tasks don’t require many resources
        2. Some tasks have idle periods
        3. With multiple threads, we can have the illusion of parallelism because they execute so fast
            1. So realistically… Multithreading and Concurrency is all about using idle periods intelligently
3. Concurrency - when a system can work on multiple tasks at the same time, by working on parts of each tasks in an overlapping way
    1. It doesn’t mean everything is literally happening at the exact same instant (that’s parallelism). Instead, it’s about making progress on more than one thing without waiting for one to finish first.
    2. Could involve other CPU cores, but it isn’t required.
    3. The illusion of simultaneous work by juggling tasks at the same time very fast.
    4. Basically multitasking.
    5. Think of cooking food.
4. Asynchronous - the method of suspending/resuming tasks to make concurrency efficient
    1. Basically, don’t wait for the pasta to finish in order to start your spaghetti
    2. Asynchronous = “start this, keep going, I’ll deal with it when it’s ready.”
5. Synchronous - the method where one tasks must be complete in order to start another tasks.
    1. Basically, finish one user story for a project, then start another once that definition of done is complete.
    2. Synchronous = “finish this first, then move on.”

The Difference
1. Concurrency: The program can handle multiple things at once (like downloading a file while the UI is still responsive).
2. Parallelism: The program actually runs multiple things at the same instant (using multiple CPU cores).

Coroutine
A coroutine is:
	1.	An executable block of code (like a lightweight thread of work),
	2.	Plus its context (dispatcher, job, scope, etc.),
	3.	That can be suspended and resumed without blocking the underlying thread, or run in parallel.
1. Coroutines can either be asynchronous or synchronous.
2. It’s the tool we use to handle concurrency and parallelism with blocking threads.

Dispatchers
1. Dispatchers
    1. We use dispatchers to define the SIZE of the THREAD POOL and which thread the coroutine will run on.
    2. It’s responsible for having different configurations for which thread a coroutine can run on.
2. IO Dispatchers
    1. Larger thread pool than the Default and Main
    2. Uses a shared pool of on-demand created threads designed for blocking operations.
    3. At least 64 potential threads where code can execute
        1. This is one of the main reasons why we can make a lot of different network calls on background threads and why it is preferred vs. default, main, etc.
        2. We benefit from multiple distinct threads.
3. Default
    1. Only has as many threads as CPU cores
    2. Uses a shared pool of background threads to run heavy CPU-intensive tasks
    3. So if your phone only has four threads, there are only four threads on which the coroutine can run.
        1. This is the main reason why CPU operations are better to be called on the CPU threads.
        2. We can maximize the CPU threads as needed and in parallel
4. Unconfined
    1. Runs the coroutine in the current thread until the first suspension.
    2. Rarely used
    3. Basically, just continues on the thread that it was originally run on.

Main Safety
- Any suspend function should be safe to call from the main thread
- In suspending methods, the responsibility of switching and choosing the main thread lies within that  function
- Ex: using a get method that suspends code from an api.
- It is better to use 
- Switching dispatchers only makes sense if the call inside the withContext block is not a suspending function
    - Unless using XML when we need to update the main thread, because we can only update UI on the main thread

