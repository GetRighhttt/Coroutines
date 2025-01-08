package coroutines.practical_examples

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.runBlocking

/**
 * @author Stefan Bayne
 */

/**
 * Here we are going to go over Flow operators which is a key benefit to using Flows.
 */
fun main() {

    runBlocking {
        /**
         * First we are going to go over some Flow operators that focus on combining emissions together:
         * combine, zip, merge.
         *
         * combine() - forms pairs from elements. When using combine, every new elements replaces its'
         * predecessor. Combine also will emit until both flows are closed. Combine is mostly used
         * when needing to actively observe two sources of changes. Good for when we need to have
         * elements emitted whenever a change occurs.
         *
         * zip() - makes pairs of both flows. Need to specify a function to determine how the elements
         * are paired together. Each element can only be apart of one pair thus meaning it needs to
         * wait for it's pair to be complete before emitting. Elements without a pair are lost. Zip
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

        // combine
        numberFlow.combine(textFlow) { number, text ->
            println("$number + $text")
        }.collect()

        // zip
        numberFlow.zip(textFlow) { number, text ->
            println("$number to $text")
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
         * 1 to one
         * 2 to two
         * 3 to three
         * 4 to four
         * 5 to five
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
         * and then merge them to a single list. The concurrency parameter controls the number of
         * concurrent flows that should be collected. flatmapMerge() doesn't block the initial value
         * so the list do not appear ordered.
         *
         * flatmapLatest() = good for newest event
         * flatmapConcat() = good for processing new flows in an ordered list
         * flatmapMerge() = good for just merging code together and emitting values.
         */
    }
}