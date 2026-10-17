package coroutines.cancellation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

/**
 * Shows cooperative cancellation, suspending cleanup, and parent-child cancellation.
 *
 * Cancellation takes effect at checks such as yield or isActive. A cancelling job completes
 * only after its children and cleanup finish.
 *
 * @author Stefan Bayne
 */

fun main() = runBlocking {

    println("Start of main program: ${Thread.currentThread().name}")

    // yield checks cancellation and gives other coroutines a chance to run.
    val job: Job = launch {
        for (i in 0..500) {
            print(" $i ")
            yield()
        }
    }

    delay(2)
    job.cancelAndJoin()

    print("\n")

    // NonCancellable allows short suspending cleanup after cancellation.

    val job2: Job = launch(Dispatchers.Default) {
        try {
            for (i in 0..500) {
                if (!isActive) {
                    break
                }
                print(" $i ")
            }
        } catch (e: CancellationException) {
            println("Exception caught safely.")
        } finally {
            withContext(NonCancellable) {
                println()
                println("job: I'm running finally")
                delay(1000L)
                println("job: And I've just delayed for 1 sec because I'm non-cancellable")
            }
        }
    }

    delay(5)
    job2.cancel(CancellationException("My own cancellation message."))
    job2.join()

    // SupervisorJob isolates failures of its direct children.
    val customCoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // Cancelling a parent also cancels its children.
    println("About to cancel coroutine...")
    val newJob = customCoroutineScope.launch {

        val firstJob = launch {
            delay(200L)
            println("First child: I'm sleeping .. Second One canceled.")
        }
        firstJob.cancelAndJoin()
        println("First child: I've finished")
         val secondJob = launch {
            delay(200L)
            println("Second child: I'm sleeping .. First One canceled.")
        }
        secondJob.cancelAndJoin()

        delay(1000L)
        println("Second child: I've finished")
    }
    println("Cancelled job...")
    newJob.cancelAndJoin()

    println("\nEnd of main program: ${Thread.currentThread().name}")

}
