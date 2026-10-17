package coroutines.flows.cold

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Shows a cold flow built with flow and collected with collect or launchIn.
 *
 * The producer emits values; the collector receives them. Each collection starts the producer
 * again. By default, emission and collection run sequentially in the collecting coroutine.
 *
 * @author Stefan Bayne
 */

suspend fun main() {

    // Collection starts the cold producer.
    runBlocking {
        launch {
            flow {
                delay(1000)
                emit(1)
                delay(2000)
                emit(2)
                delay(3000)
                emit(3)
                delay(4000)
                emit(4)
                delay(1000)
            }.collect {
                println("Received: $it")
            }
            println("All flows cleared in the first example...")
            delay(2000)
        }
    }
    // launchIn starts collection in the supplied scope.
    runBlocking {
        flow {
            delay(1000)
            emit(1)
            delay(2000)
            emit(2)
            delay(3000)
            emit(3)
            delay(4000)
            emit(4)
            delay(1000)
        }.onEach {
            println("Received: $it")
        }.catch { throw CancellationException() } // Converts an upstream failure into cancellation.
            .launchIn(scope = CoroutineScope(Dispatchers.IO))
        delay(2000)
    }

    val numberFlow: Flow<Int> = flow {
        (0..10).forEach {
            println("Sending $it")
            emit(it)
            if (it == 11) throw RuntimeException() // Unreachable: the range ends at 10.
        }
    }

    try {
        numberFlow.collect {
            println("Received: $it")
        }
    } catch (e: InterruptedException) {
        println("${e.message}")
    }
}
