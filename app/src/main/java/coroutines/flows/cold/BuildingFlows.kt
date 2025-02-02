package coroutines.flows.cold

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking


/**
 * @author Stefan Bayne
 */

/**
 * Flow builder takes a suspend function as a function parameter which allows us to call suspend
 * functions in it.
 *
 * public fun <T> flow (@BuilderInference block: suspend FlowCollector<T>.() -> Unit): Flow<T>
 */

fun makeFlow(): Flow<Int> = flow {
    repeat(10) {
        delay(400)
        emit(it)
    }
}
    .map { it * 3 }
    .filter { it % 2 == 0 }
    .catch {
        it.printStackTrace()
    } // 0 6 12 18 24

/**
 * Let's now create lists we can use to demonstrate passing in a stream or list of data, and
 * returning that data in a flow.
 */
fun fetchUserMessages(): List<String> {
    return listOf("Here we are!", "It's time to party!", "Don't wait up!", "It's going down today!")
}

fun fetchUserNumbers(): MutableList<String> {
    return mutableListOf("1234", "5678")
}

/**
 * Now we are going to use the lists created above, and demonstrate how we can use intermediary or
 * optional methods on those lists as they are being emitted to manipulate streams of data.
 */
fun buildFlow(): Flow<String> = flow {
    var count = 5
    while (count != 0) {
        val userMessages: List<String> = fetchUserMessages()
        delay(500)
        emit(userMessages.joinToString(" ").lowercase())
        count--
    }
}

fun buildNumberFlow(): Flow<String> = flow {
    var count = 1
    while (count != 2) {
        val userNumbers: List<String> = fetchUserNumbers()
        delay(400)
        emit(userNumbers
            .joinToString(" ")
            .toList()
            .map { it.code } // converts char to Number UTF-16
            .filter { it % 2 == 0 }
            .forEach {
                println(it)
            }
            .toString()
        )
        count++
    }
}

@OptIn(FlowPreview::class)
fun main() {

    runBlocking {
        // display flow data from lists we created
        makeFlow().collect { println(it) }
        buildFlow().collect { println(it) }
        buildNumberFlow().collect()

        // flowOf() - simple and we just define the values.
        flowOf(2, 4, 6, 8)
            .collect { println(it) } // 2468

        // emptyFlow() - flow with no values.
        emptyFlow<Int>().collect { println(it) } // nothing returned

        // asFlow() converts every iterable, iterators, or collections into flows
        val tempList = suspend {
            listOf(1, 2, 3, 4)
                .map { it * 10 }
                .filter { it >= 30 }

        }
        tempList.asFlow().onEach { println(it) }.collect() // 30, 40

        // we can also convert functions to flows
        val newFunction = suspend {
            delay(1000)
            "x"
        }
        newFunction.asFlow()
            .catch { a ->
               emit(a.message ?: "Exception thrown...")
            }
            .combine(tempList.asFlow()) { number, list ->
                "$number to $list"
            }
            .onEach { println(it) } // waits a sec, then prints x
            .collect()
    }

    /**
     * 0
     * 6
     * 12
     * 18
     * 24
     * here we are! it's time to party! don't wait up! it's going down today!
     * here we are! it's time to party! don't wait up! it's going down today!
     * here we are! it's time to party! don't wait up! it's going down today!
     * here we are! it's time to party! don't wait up! it's going down today!
     * here we are! it's time to party! don't wait up! it's going down today!
     * 50
     * 52
     * 32
     * 54
     * 56
     * 2
     * 4
     * 6
     * 8
     * [30, 40]
     * x to [30, 40]
     */
}