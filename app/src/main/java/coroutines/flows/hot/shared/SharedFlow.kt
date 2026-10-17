package coroutines.flows.hot.shared

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Broadcasts values to two collectors through a SharedFlow.
 *
 * SharedFlow collection does not complete normally. These collectors keep runBlocking alive
 * after the producer finishes.
 */
fun main() = runBlocking {
    val sharedFlow = MutableSharedFlow<Int>()

    sharedFlow.onEach {
        println("Received 1: $it")
    }.launchIn(this)

    sharedFlow.onEach {
        println("Received 2: $it")
    }.launchIn(this)

    launch {
        repeat(10) {
            delay(1000)
            sharedFlow.emit(it)
        }
    }
}
