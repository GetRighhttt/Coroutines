package coroutines.exceptions

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

/**
 * Shows catching failures inside a coroutine and the scope types used to handle them.
 *
 * coroutineScope cancels sibling work when a child fails. supervisorScope isolates child
 * failures, but those failures still need handling.
 *
 * @author Stefan Bayne
 */

fun main() {
    runBlocking {
        launch {
            delay(2000)
            try {
                throw Exception("Here is the exception")
            } catch (e: Exception) {
                ensureActive()
                e.printStackTrace()
            }
            delay(2000)
            println("Coroutine finished with regular exception catching")
        }

        // Both examples catch their failures locally, so this handler is not invoked.
        val handler = CoroutineExceptionHandler{ _, throwable ->
            throwable.printStackTrace()
        }
        launch(handler) {
            delay(2000)
            try {
                throw Exception("Here is the exception")
            } catch (e: Exception) {
                ensureActive()
                e.printStackTrace()
            }
            delay(2000)
            println("Coroutine finished using exception handler")
        }

        // These empty blocks show the two scope APIs without launching child work.
        coroutineScope {}
        supervisorScope {}

    }
}

