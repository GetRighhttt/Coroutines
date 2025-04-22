package coroutines.flows.practical_examples

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
 * @author Stefan Bayne
 */

fun main() {

    runBlocking {
        /**
         * combine() - forms pairs from elements. When using combine, every new element replaces its
         * predecessor. Combine also will emit until both flows are closed. Combine is mostly used
         * when needing to actively observe two sources of changes. Good for when we need to have
         * elements emitted whenever a change occurs.
         *
         * zip() - makes pairs of both flows. Need to specify a function to determine how the elements
         * are paired together. Each element can only be a part of one pair, thus meaning it needs to
         * wait for its pair to be complete before emitting. Elements without a pair are lost. Zip
         * is used when we need to pair to elements from two different streams.
         *
         * combine emits updates as soon as streams change while zip waits for pairs to complete.
         */

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

        /**
         * 1 + one
         * 2 + one
         * 3 + one
         * 3 + two
         * 4 + two
         * 5 + two
         * 5 + three
         * 5 + four
         * 5 + five
         * combine method completed
         * 1 to one
         * 2 to two
         * 3 to three
         * 4 to four
         * 5 to five
         * zip method completed
         */

        /**
         * flatmap methods are used to flatten() different lists into a single list
         *
         * flatmapLatest - cancels the previous flow created by the block and maps the result to
         * an entire new flow; cancels the child flow created for the block.
         *
         * flatmapConcat - does the task of transforming, serial concatenating, and flattening
         * and is a shortcut for both map() and flattenConcat() methods. So it maps the flows, then
         * concatenates them together into a single ordered list.
         *
         * flatmapMerge - map() and flatterMerge() methods are used here to first transform the flow
         * concurrent flows that should be collected. flatmapMerge() doesn't block the initial value
         * so the list does not appear ordered.
         *
         * flatmapLatest() = good for the newest event
         * flatmapConcat() = good for processing new flows in an ordered list
         * flatmapMerge() = good for just merging code and emitting values.
         */
    }
}