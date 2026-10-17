package coroutines.synchronization

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Compares JVM locks, coroutine mutexes, and limited dispatcher parallelism.
 *
 * Protect a shared read-modify-write operation with one consistent locking strategy.
 * These examples mix locks for count, so that counter can still have a race.
 *
 * @author Stefan Bayne
 */
fun main() {

    // synchronized blocks a waiting thread; Mutex suspends a waiting coroutine.
    var count = 0
    var count2 = 0
    val lock = Any()
    val mutex = Mutex()

    runBlocking {
        // All workers use the same JVM lock.
        launch(Dispatchers.Default) {
            (1..100_000).map {
                launch(Dispatchers.Default) {
                    synchronized(lock) {
                        count++
                    }
                }
            }.joinAll()
            println(count)
        }

        // All workers use the same mutex.
        launch(Dispatchers.Default) {
            (1..100_000).map {
                launch(Dispatchers.Default) {
                    mutex.withLock {
                        count2++
                    }
                }
            }.joinAll()
            println(count2)
        }

        // Manual locking requires finally to release the mutex if the block fails.
        launch(Dispatchers.Default) {
            (1..100_000).map {
                launch(Dispatchers.Default) {
                    mutex.lock()
                    try {
                        count++
                    } finally {
                        mutex.unlock()
                    }
                }
            }.joinAll()
            println(count)
        }

        // Acquiring this non-reentrant mutex twice in one coroutine would deadlock.
    }
    // limitedParallelism(1) runs one task at a time; coroutines can interleave at suspension.

    runBlocking {
        var count1 = 0
        launch(Dispatchers.Default.limitedParallelism(1)) {
            count1++
        }
    }
}
