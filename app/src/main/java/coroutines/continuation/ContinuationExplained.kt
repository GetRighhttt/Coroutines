package coroutines.continuation

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.Continuation

/**
 * @author Stefan Bayne
 */

/**
 *
 */

@OptIn(DelicateCoroutinesApi::class)
fun main() {

    // code to be called when resuming a suspension
    fun showSuspension() = GlobalScope.launch {
        val newContinuation = Continuation<Int>(Dispatchers.Default) { continuation ->
            println("Inside a continuation right now... ${continuation.isSuccess}")
        }
        println(newContinuation.context.isActive)
    }
    runBlocking {
        delay(2000L)
        println(showSuspension())
        println("I am resuming now...")
        delay(1000)
    }
}