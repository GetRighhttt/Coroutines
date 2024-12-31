package coroutines.synchronization

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Synchronization - coordinating how shared resources are controlled when executing multiple
 * threads or processes.
 *
 * Handling shared resources is essential to avoid "Race Conditions" which is when multiple threads
 * read and write to the same field.
 * Whenever you have a MUTABLE field that you change inside of multiple coroutines where the coroutines
 * could also run on different threads, you have to consider race conditions and synchronization.
 *
 * Race Conditions usually occur when dealing with mutability fields and not immutable fields, which
 * highlights why immutability is preferred.
 */
@OptIn(InternalCoroutinesApi::class)
fun main() {

    /**
     * synchronized{} makes sure the code inside can only be called sequentially as long as the same
     * lock is shared. Meaning that even if multiple coroutines access the resources, it will happen
     * in sequence.
     *
     * mutex = Mutual Exclusion Lock for coroutines; is a synchronization primitive that ensures only one
     * coroutine can access a share resource at a time by using a key and lock, and when one has that
     * lock, it has exclusive access.
     */
    var count = 0
    val lock = Any()
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
                        count++
                    }
                }
            }.joinAll() // -> suspends until all coroutines are complete at the same time
            println(count)
        }

    }
}