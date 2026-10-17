package coroutines.flows.hot.state

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Shows that a StateFlow keeps the latest value for new collectors.
 *
 * A StateFlow needs an initial value. The `runBlocking` scope owns the producer and collectors.
 */
fun main() = runBlocking {
    val stateFlow = MutableStateFlow(0)

    stateFlow.onEach {
        println("Value is $it")
    }.launchIn(this)

    launch {
        repeat(10) {
            delay(500L)
            stateFlow.value = it
        }
        stateFlow.onEach {
            println("Value from collector 2 is $it")
        }.launchIn(this@runBlocking)
    }

}
