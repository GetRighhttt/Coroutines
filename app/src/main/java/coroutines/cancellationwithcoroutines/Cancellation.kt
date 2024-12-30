package coroutines.cancellationwithcoroutines

import kotlinx.coroutines.*

/**
 * Why cancel a coroutine?
 *
 * Result no longer needed;
 * Taking too long to perform a tasks.
 * Etc.
 *
 * As soon as you begin using coroutines in your project, you must always consider cancellation because
 * coroutine cancellation can happen at any time, and we do not have control over it.
 */

fun main() = runBlocking {

    println("Start of main program: ${Thread.currentThread().name}")

    /**
     * Coroutines must be cooperative to cancel.
     *
     * Cooperative?
     * Must invoke/use a suspending function that checks for cancellation - delay(), withContext(), etc.
     * in the coroutine.
     *
     * Whenever we have blocking functions in our coroutines, we must actively check if the coroutine
     * isActive, running, etc. in order to know when or if we should cancel it.
     *
     * We can also use the boolean isActive flag to check if the cancellation status of the coroutine.
     *
     * while(isActive) continue...
     * or more recently ensureActive(), or yield() -> ensure active isn't a suspending function whereas
     * yield() is.
     *
     * yield() voluntarily lets other threads do some work.
     *
     * Essentially we want to ensure the coroutine is active in various points when working with
     * multipel coroutines
     */
    val job: Job = launch {
        for (i in 0..500) {
            print(" $i ")
            /*
            Yields the current thread or thread pool of the current coroutine dispatcher so other coroutines
            can run.
            Usually ends up speeding functionality.
             */
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

}