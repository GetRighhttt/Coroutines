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
 * Builds cold flows from values, collections, and suspending functions.
 *
 * Intermediate operators transform emissions; terminal operators such as collect start the work.
 *
 * @author Stefan Bayne
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

fun fetchUserMessages(): List<String> {
    return listOf("Here we are!", "It's time to party!", "Don't wait up!", "It's going down today!")
}

fun fetchUserNumbers(): MutableList<String> {
    return mutableListOf("1234", "5678")
}

// Repeat the same message list as a single formatted emission.
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
            .map { it.code } // UTF-16 code unit
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
        makeFlow().collect { println(it) }
        buildFlow().collect { println(it) }
        buildNumberFlow().collect()

        flowOf(2, 4, 6, 8)
            .collect { println(it) } // 2, 4, 6, 8

        emptyFlow<Int>().collect { println(it) } // No emissions

        // A suspending function can also be converted to a flow.
        val tempList = suspend {
            listOf(1, 2, 3, 4)
                .map { it * 10 }
                .filter { it >= 30 }

        }
        tempList.asFlow().onEach { println(it) }.collect() // [30, 40]

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
            .onEach { println(it) } // x to [30, 40]
            .collect()
    }

}
