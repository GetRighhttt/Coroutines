package coroutines.coroutinebuilders

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Compares launch, which returns a Job, with async, which returns a Deferred result.
 *
 * join waits for completion; await also returns the result. runBlocking blocks the calling
 * thread until its block and children finish.
 *
 * @author Stefan Bayne
 */
fun main() = runBlocking {

    println("Start of main program: ${Thread.currentThread().name}")

    // launch returns a Job for waiting or cancellation.
    val job: Job = launch(Dispatchers.IO) {
        println("Start of launch worker thread: ${Thread.currentThread().name}")
        delay(1000)
        println("End of launch worker thread: ${Thread.currentThread().name}")
    }
    job.join()

    // async returns a Deferred<Int>; await retrieves its result.
    val jobDeferred: Deferred<Int> = async(Dispatchers.Default) {
        println("Start of async worker thread: ${Thread.currentThread().name}")
        delay(1000)
        println("End of async worker thread: ${Thread.currentThread().name}")
        22
    }

    val numberFromAsync = jobDeferred.await()
    println("The value from the async deferred job is: $numberFromAsync !")

    println("End of main program: ${Thread.currentThread().name}")

}
