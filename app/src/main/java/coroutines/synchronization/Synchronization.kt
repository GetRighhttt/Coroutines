package coroutines.synchronization

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


/**
 * @author Stefan Bayne
 */

/**
 * Synchronization - coordinating how shared resources are controlled when executing multiple
 * threads or processes.
 *
 * Handling shared resources is essential to avoid "Race Conditions" which is when multiple threads
 * read and write to the same field.
 * Whenever you have a MUTABLE field that you change inside of multiple coroutines where the coroutines
 * could also run on different threads, you have to consider race conditions and synchronization. When
 * running the same scenario on the MAIN thread, this can't happen because this thread happens
 * sequentially therefore only processing one thing at a time.
 *
 * Race Conditions usually occur when dealing with mutability fields and not immutable fields, which
 * highlights why immutability is preferred.
 */
@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
fun main() {

    /**
     * synchronized{} makes sure the code inside can only be called sequentially as long as the same
     * lock is shared. Meaning that even if multiple coroutines access the resources, it will happen
     * in sequence. This is also the common approach for normal threads.
     *
     * mutex = Mutual Exclusion Lock for coroutines; is a synchronization primitive that ensures only one
     * coroutine can access a share resource at a time by using a key and lock, and when one has that
     * lock, it has exclusive access. It's like when one thread has the key to the door, it is the
     * only one that can enter.
     */
    var count = 0
    var count2 = 0
    val lock = Any() // used for synchronized() and mutex()
    val mutex = Mutex()

    runBlocking {
        /**
         * synchronized{}
         */
        launch(Dispatchers.Default) {
            (1..100_000).map {
                launch(Dispatchers.Default) {
                    synchronized(lock) {
                        count++
                    }
                }
            }.joinAll() // -> suspends until all coroutines are complete at the same time
            println(count)
        }

        /**
         * mutex{} - withLock() uses coroutines so is the synchronization of synchronized()
         */
        launch(Dispatchers.Default) {
            (1..100_000).map {
                launch(Dispatchers.Default) {
                    mutex.withLock(lock) { // or lock()
                        count2++
                    }
                }
            }.joinAll() // -> suspends until all coroutines are complete at the same time
            println(count2)
        }

        /**
         * We can also manually lock the mutex, but the difference is using withLock() will make use
         * of a try/finally block whereas the mutex.lock() will not.
         * withLock() calls the "unlock" method in any case whereas mutex.lock() does not ensure that
         * which can lead to deadlocks.
         *
         * Deadlocks happen when both threads are waiting for each other to finish at the same time.
         */
        launch(Dispatchers.Default) {
            (1..100_000).map {
                launch(Dispatchers.Default) {
                    mutex.lock()
                        count++
                    mutex.unlock()
                }
            }.joinAll() // -> suspends until all coroutines are complete at the same time
            println(count)
        }

        /**
         * Will produce a deadlock. You should really only wrap mutable fields in lock methods in
         * small cases.
         */
        launch(Dispatchers.Default) {
            (1..100_000).map {
                launch(Dispatchers.Default) {
                    mutex.withLock { // both blocks are awaiting the key
                        mutex.withLock {
                            count++
                        }
                    }
                }
            }.joinAll() // -> suspends until all coroutines are complete at the same time
            println(count)
        }
    }
    /**
     * Also, concurrent data structures are only synchronized per read or write instruction, so we
     * need to use a mutex in order to synchronize the data structures.
     *
     * Also, we must remember that if things happen in sequence, then race conditions cannot happen, so
     * we can make use of coroutine contexts to achieve this. This is called the SingleThreadDispatcher.
     */

    // ensures only one thread is executed at a time
    runBlocking {
        var count1 = 0
        launch(Dispatchers.Default.limitedParallelism(1)) {
            count1++
        }
    }
}