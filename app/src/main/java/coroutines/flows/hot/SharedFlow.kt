package coroutines.flows.hot

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * @author Stefan Bayne
 */

/**
 * Hot Flows - emits values without collectors nonstop.
 *
 * SharedFlow - a hot flow that emits a value to all the consumers that collect from it. The emit()
 * function from coroutines will emit emissions until all collectors have processed the emission.
 * So essentially, the emissions from a shared flow are indeed SHARED until all collectors have
 * the data they need.
 *
 * SharedFlows are really good for global tasks that need constant updates like listening to a users'
 * location and update the UI continuously, thus showing that the emissions can be shared between
 * as many collectors as possible.
 *
 * Cold flows can be converted to hot flows using sharedIn() method.
 */

@OptIn(DelicateCoroutinesApi::class)
fun main() {

    val sharedFlow = MutableSharedFlow<Int>()

    sharedFlow.onEach {
        println("Received 1: $it")
    }.launchIn(GlobalScope)

    sharedFlow.onEach {
        println("Received 2: $it")
    }.launchIn(GlobalScope)

    GlobalScope.launch {
        repeat(10) {
            delay(1000)
            sharedFlow.emit(it)
        }
    }
}