package coroutines.flows.practicalexamples

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.runBlocking

/**
 * Compares combine, which uses the latest value from each source, with zip, which pairs
 * values one-to-one. Timing can change the exact output of combine.
 *
 * @author Stefan Bayne
 */
fun main() {

    runBlocking {

        val numberFlow = flowOf(1, 2, 3, 4, 5).onEach {
            delay(500L)
        }

        val textFlow = flowOf("one", "two", "three", "four", "five").onEach {
            delay(1000L)
        }

        // combine + other operators
        numberFlow.combine(textFlow) { number, text ->
            println("$number + $text")
        }
            .flowOn(Dispatchers.IO)
            .catch { e -> e.printStackTrace() }
            .onCompletion {
                delay(1000L)
                println("combine method completed")
            }.collect()

        // zip + other operators
        numberFlow.zip(textFlow) { number, text ->
            println("$number to $text")
        }
            .flowOn(Dispatchers.IO)
            .catch { e -> e.printStackTrace() }
            .onCompletion {
                delay(1000L)
                println("zip method completed")
            }.collect()

    }
}
