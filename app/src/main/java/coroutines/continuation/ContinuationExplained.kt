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

/**
 *
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
            println("continuing")
            showContinuation()
        }
        delay(1000L)
        showContinuation()
        println("I am resuming now...")
        delay(1000)
    }

    /**
     * starting coroutine...
     * continuing
     * Inside a continuation right now... true
     * I am resuming now...
     * Inside a continuation right now... true
     */
}