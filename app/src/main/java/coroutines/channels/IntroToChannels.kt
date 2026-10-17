package coroutines.channels

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.channels.Channel

/**
 * Shows how coroutines exchange values through a channel.
 *
 * A channel keeps values in order. Producers call [Channel.send] and consumers call
 * [Channel.receive]. The producer closes the channel after it has sent every value.
 */
fun main() = runBlocking {
    val basket = Channel<String>()

    launch { // coroutine1
        val fruits = listOf("Apple", "Orange")
        for (fruit in fruits) {
            println("coroutine1: Sending $fruit")
            basket.send(fruit)
        }
        basket.close()
    }

    launch { // coroutine2
        for (fruit in basket) {
            delay(100)
            println("coroutine2: Received $fruit")
        }
    }
}
