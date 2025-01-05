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
 * @author Stefan Bayne
 */

/**
 *
 * Flows allow us to achieve Reactive Programming whereas Coroutines are not considered reactive
 * because we do not have a subscription/publish (sub/pub) functional chain in place.
 * "Reactive programming is largely based on observing some data, and performing some
 * operations based on the data returned."
 *
 * Flows are streams of data that can be computed asynchronously that allow us to have multiple
 * return values unlike regular suspend functions, and help us to achieve reactive programming.
 *
 * "A cold, asynchronous stream and is an implementation of the Observable design pattern."
 * Cold = starts when user subscribes to it, starts anew for each new subscriber.
 * Hot = continuously emits results.
 *
 * Observable design pattern has two methods - subscribe() and publish() btw.
 * With Flows emit() = subscribe() and collect() = publish().
 * Producers(data sources/repositories) emit data to the flow, that can then be collected(UI layer) by the UI.
 *
 * Flows are suspending but not concurrent, are cold, and use backpressure: resisting flow of data.
 *
 * Flow = emit/collect (sub/pub) and are cold.
 */

suspend fun main() {

    /**
     * Flows first launch with a value source that fires these emissions.
     *
     * It then needs a collector/terminal operator to collect the values. The values will not show
     * if not collected properly.
     */
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
    /**
     * We can also have intermediate or optional methods that perform before a flow is emitted, and
     * can also use the launchIn() method to reduce having to explicitly launch the flow.
     */
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
        }.onEach { // intermediate method
            println("Received: $it")
        }.catch { throw CancellationException() } // cancels after first emission and continues
            .launchIn(scope = CoroutineScope(Dispatchers.IO))
        delay(2000)
    }

    /**
     * We can also construct flows using flow builders. For example:
     */
    val numberFlow: Flow<Int> = flow {
        (0..10).forEach {
            println("Sending $it")
            emit(it) // subscribes
            if (it == 11) throw RuntimeException() // throws exception if reaches 10.
        }
    }

    try {
        numberFlow.collect {// publishes
            println("Received: $it")
        }
    } catch (e: InterruptedException) {
        println("${e.message}")
    }
}
