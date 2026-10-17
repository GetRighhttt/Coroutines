package coroutines.coroutinebuilders

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking

/**
 * Prints a scope containing a context, name, and job.
 *
 * The explicit Job replaces the inherited job, so it has an independent lifetime.
 */
fun main() {
    runBlocking {
        val coroutineContextEx = CoroutineScope(
            coroutineContext + CoroutineName("first_coroutine") + Job()
        )
        print(coroutineContextEx)
    }
}
