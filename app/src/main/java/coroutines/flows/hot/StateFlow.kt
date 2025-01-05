package coroutines.flows.hot

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * @author Stefan Bayne
 */

/**
 * Hot Flows - emits values without collectors nonstop.
 *
 * StateFlow - is a state-holder observable flow that emits the current and new state updates to
 * its collectors. Cold flows can be converted to stateFlows using stateIn() method.
 *
 * state - value that can change over time.
 *
 * In Android, StateFlow is a great fit for classes that need to maintain an observable mutable state.
 * StateFlow must be passed an initial value at first.
 */

@OptIn(DelicateCoroutinesApi::class)
fun main() {
    val stateFlow = MutableStateFlow(0)

    stateFlow.onEach {
        println("Value is $it")
    }.launchIn(GlobalScope)

    GlobalScope.launch {
        repeat(10) {
            delay(500L)
            stateFlow.value = it
        }
        stateFlow.onEach {
            println("Value from collector 2 is $it")
        }.launchIn(GlobalScope)
    }

}