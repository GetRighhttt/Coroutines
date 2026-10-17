package coroutines.basics

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

/**
 * Compares blocking a worker thread with suspending a coroutine.
 *
 * Thread.sleep blocks its thread; delay lets other coroutines run. runBlocking waits for its
 * children, and withContext runs a block in the supplied context and returns its result.
 *
 * @author Stefan Bayne
 */

fun main() = runBlocking {

    println("Starting on the main thread: ${Thread.currentThread().name}")

    thread {

        Thread.sleep(2000)
        println("Starting background thread: ${Thread.currentThread().name}")
        Thread.sleep(3000)
        println("Ending background thread: ${Thread.currentThread().name}")

    }

    // The child belongs to the enclosing runBlocking scope.
    launch {

        // delay suspends the coroutine without blocking its thread.
        delay(2000)

        // IO is intended for blocking I/O; these blocks only illustrate a context switch.
        withContext(Dispatchers.IO) {
            println("Starting Coroutine thread: ${Thread.currentThread().name}")
        }
        delay(1000)
        withContext(Dispatchers.IO) {
            println("Ending Coroutine thread: ${Thread.currentThread().name}")
        }
    }

    delay(1000)

    println("Ending on the main thread: ${Thread.currentThread().name}")
}
