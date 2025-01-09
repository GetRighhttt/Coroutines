package coroutines.continuation

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

/**
 * @author Stefan Bayne
 */

@OptIn(DelicateCoroutinesApi::class)
fun main() {

    // code to be called when resuming a suspension
    fun showContinuation() = GlobalScope.launch {
        Continuation<Int>(Dispatchers.Default) { continuation ->
            println("Inside a continuation right now... ${continuation.isSuccess}")
        }.resume(1)
    }
    runBlocking {
        launch {
            println("starting coroutine...")
            println("delaying...")
            delay(1000)
            showContinuation()
        }
        launch {
            delay(2000L)
            println("new coroutine")
            println("delaying...")
            delay(1000)
            showContinuation()
        }
    }

    /**
     * starting coroutine...
     * delaying...
     * Inside a continuation right now... true
     * new coroutine
     * delaying...
     * Inside a continuation right now... true
     */
}