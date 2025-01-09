package coroutines.cancellationwithcoroutines

import android.provider.Settings.Global
import kotlinx.coroutines.*

/**
 * @author Stefan Bayne
 */

/**
 * Why cancel a coroutine?
 *
 *  * Result no longer needed;
 *  * Taking too long to perform a tasks. Etc.
 *
 *  * As soon as you begin using coroutines in your project, you must always consider cancellation because
 *  * coroutine cancellation can happen at any time, and we do not have control over it.
 *
 *  How does coroutine cancellation work?
 *
 *  Coroutines have certain check points where they check to see if the coroutine is cancelled or
 *  not, and must be checked with a coroutineContext method(ensureActive(), isActive(), etc.).
 *
 *  All of the suspend functions in coroutines are cancellable. They check for cancellation of coroutine,
 *  and throw CancellationException if it is cancelled. So yield(), delay(), coroutine support in
 *  ROOM, Retrofit, Ktor, etc. all check for cancellation. However, if a created coroutine is working in a
 *  computation and there isn't a check for cancellation, then cancellation cannot happen. This same
 *  problem occurs when catching a CancellationException and not rethrowing it.
 *
 *  Example of delay method checking for cancellation:
 */

//    public suspend fun delay1(timeMillis: Long) {
//        if (timeMillis <= 0) return // don't delay
//        return suspendCancellableCoroutine sc@{ cont: CancellableContinuation<Unit> ->
//            // if timeMillis == Long.MAX_VALUE then just wait forever like awaitCancellation, don't schedule.
//            if (timeMillis < Long.MAX_VALUE) {
//                cont.context.delay.scheduleResumeAfterDelay(timeMillis, cont)
//            }
//        }
//    }

@OptIn(DelicateCoroutinesApi::class)
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
     * In between blocking calls in our coroutines, we must have a check for cancellation; i.e if
     * isActive, ensureActive(), running, etc. in order to know when or if we should cancel it.
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

    delay(2)
    job.cancelAndJoin() // cancels the coroutine.

    print("\n")

    /**
     * When executing suspendable code inside of a finally block in a try-catch, you should always use
     * withContext(NonCancellable). It's used to clean up resources.
     */

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
                println()
                println("job: I'm running finally")
                delay(1000L)
                println("job: And I've just delayed for 1 sec because I'm non-cancellable")
            }
        }
    }

    delay(5)
    job2.cancel("My own crash message.")
    job2.join()

    /**
     * Creating your own coroutine scope with SupervisorJob() ensures that when handling large transactions
     * or large operations, if one of the coroutine fails, it won't cancel the others, or the whole scope.
     * We typically would create an Application() class in Android, and create this there so we could
     * have this scope live as long as the application is running.
     */
    val customCoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    /**
     * Internal workings of Coroutines:
     *
     * * If a job is cancelled, all of it's children are then cancelled which is the idea behind
     * * implementing structured concurrency.
     *
     *  * If we also cancel a coroutine scope, the same occurs; all of the children will be canceled.
     */
    println("About to cancel coroutine...")
    val newJob = launch {

        val firstJob = launch {
            delay(200L)
            println("First child: I'm sleeping .. Second One canceled.")
        }
        firstJob.cancelAndJoin() // cancels the first job but not the whole coroutine
        println("First child: I've finished")
         val secondJob = launch {
            delay(200L)
            println("Second child: I'm sleeping .. First One canceled.")
        }
        secondJob.cancelAndJoin() // cancels the second job but not the whole coroutine

        delay(1000L)
        println("Second child: I've finished")
    }
    println("Cancelled job...")
    newJob.cancelAndJoin() // cancels the entire coroutine

    println("\nEnd of main program: ${Thread.currentThread().name}")
}