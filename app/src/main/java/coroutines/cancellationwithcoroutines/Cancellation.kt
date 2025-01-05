package coroutines.cancellationwithcoroutines

import kotlinx.coroutines.*

/**
 * @author Stefan Bayne
 */

/**
 * * Why cancel a coroutine?
 *  *
 *  * Result no longer needed;
 *  * Taking too long to perform a tasks.
 *  * Etc.
 *  *
 *  * As soon as you begin using coroutines in your project, you must always consider cancellation because
 *  * coroutine cancellation can happen at any time, and we do not have control over it.
 */
fun main() = runBlocking {

    println("Start of main program: ${Thread.currentThread().name}")

    /**
     * Coroutines typically must be cooperative to cancel when not using the cancel() method on a
     * coroutine.
     *
     * Cooperative?
     * Must invoke/use a suspending function that checks for cancellation - delay(), withContext(),
     * yield, etc. in the coroutine.
     *
     * Whenever we have blocking functions in our coroutines, we must actively check if the coroutine
     * isActive, running, etc. in order to know when or if we should cancel it.
     *
     * We can also use the boolean isActive flag to check if the cancellation status of the coroutine.
     *
     * while(isActive) continue...or yield(), etc.
     *
     * isActive: This property on CoroutineScope or Job indicates whether the coroutine is active.
     * ensureActive(): This function throws a CancellationException if the coroutine is cancelled.
     * yield(): This function checks for cancellation and suspends the coroutine if it's cancelled.
     */
    val job: Job = launch {
        for (i in 0..500) {
            print(" $i ")
            yield()
        }
    }

    delay(7)
    job.cancelAndJoin() // cancels the coroutine.

    print("\n")

    val job2: Job = launch(Dispatchers.Default) {
        // try catch block for cancellation exception handling with coroutines.
        try {
            for (i in 0..500) {
                if (!isActive) {
                    break
                } // another way to make coroutine cooperative
                print(" $i ")
            }
        } catch (e: CancellationException) {
            println("Exception caught safely.")
        } finally {
            withContext(NonCancellable) {
                println("job: I'm running finally")
                delay(1000L)
                println("job: And I've just delayed for 1 sec because I'm non-cancellable")
            }
        }
    }

    delay(5)
    job2.cancel("My own crash message.")
    job2.join()

    println("\nEnd of main program: ${Thread.currentThread().name}")

    /**
     * Creating your own coroutine scope with SupervisorJob() ensures that when handling large transactions
     * or large operations, if one of the coroutine fails, it won't cancel the others, or the whole scope.
     * We typically would create an Application() class in Android, and create this there so we could
     * have this scope live as long as the application is running.
     */
    val customCoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    /**
     * When executing suspendable code inside of a finally block in a try-catch, you should always use
     * withContext(NonCancellable). It's used to clean up resources.
     */
}