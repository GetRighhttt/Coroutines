package coroutines.continuation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

/** Shows how a continuation resumes suspended work on a chosen context. */
fun main() {
    runBlocking {
        fun showContinuation() = launch(Dispatchers.Default) {
        Continuation<Int>(Dispatchers.Default) { continuation ->
            println("Inside a continuation right now... ${continuation.isSuccess}")
        }.resume(1)
        }

        launch {
            println("starting coroutine...")
            println("delaying...")
            delay(1000)
            showContinuation().join()
        }
        launch {
            delay(2000L)
            println("new coroutine")
            println("delaying...")
            delay(1000)
            showContinuation().join()
        }
    }
}
