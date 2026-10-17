package coroutines.basics

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Shows continuation resumption and delay.
 *
 * These suspendCoroutine blocks resume immediately. delay provides the actual suspension.
 *
 * @author Stefan Bayne
 */

fun main() {
    runBlocking {
        println("Starting...")

        suspendCoroutine { continuation ->
            println("This is the first suspension block.")
            continuation.resume(Unit)
        }

        delay(1000)
        println("Continuing after first suspended Block. Now will suspend again.")
        delay(1000)

        val printContinuingString = suspendCoroutine { continuation ->
            continuation.resume("This is the second suspension block.")
        }

        delay(1000)

        println(printContinuingString)

        delay(2000)
        println("Finishing")
    }
}
