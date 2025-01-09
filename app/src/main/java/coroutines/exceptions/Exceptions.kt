package coroutines.exceptions

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

/**
 * @author Stefan Bayne
 */

/**
 * How do Coroutines handle exceptions? What happens when they fail?
 *
 * Exceptions bubble up to parent scope, and if not handled there, the app will crash.
 * You must always use try-catch INSIDE the coroutine scope so it won't kill your application.
 *
 * Must ensure to wrap api calls in try-catch blocks so the exceptions wont propagate up to the
 * parent scope and kill the application.
 */

fun main() {
    runBlocking {
        // using try-catch
        launch {
            delay(2000)
            try {
                throw Exception("Here is the exception")
            } catch (e: Exception) {
                ensureActive()
                e.printStackTrace()
            }
            delay(2000)
            println("Coroutine finished with regular exception catching") // will still get printed
        }

        /**
         * We can also use handlers to handle exceptions.
         *
         * Will handle exception and will cancel all the children if not in try-catch.
         *
         * Not encouraged to use this approach.
         */
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
            println("Coroutine finished using exception handler") // will still get printed
        }

        /**
         * SupervisorJob will ensure all jobs will not cancel if one coroutine fails.
         * Very useful when creating your own CoroutineScopes.
         *
         * coroutineScope -> This function is designed for parallel decomposition of
         * work. When any child coroutine in this scope fails, this scope fails and all the rest of
         * the children are cancelled (for a different behavior see supervisorScope).
         *
         * supervisorScope -> Creates a CoroutineScope with SupervisorJob and calls the specified
         * suspend block with this scope. The provided scope inherits its coroutineContext from the
         * outer scope, but overrides context's Job with SupervisorJob. Unlike coroutineScope,
         * a failure of a child does not cause this scope to fail and does not affect its other
         * children, so a custom policy for handling failures of its children can be implemented.
         */
        coroutineScope {}
        supervisorScope {}

        /**
         *
         * In the end:
         * * Exceptions propagate from child to parent. Suspending functions (including coroutine
         * scope functions) throw exceptions, and asynchronous coroutine builders propagate
         * exceptions to their parents via the scope.
         *
         * * To stop exception propagation, you can catch exceptions from suspending functions before
         * they reach coroutine builders, or catch exceptions from scope functions.
         *
         * * SupervisorJob is a special kind of job that ignores all exceptions in its children.
         * It is used to prevent exceptions from canceling all the children of a scope.
         *
         * * SupervisorJob should not be used as a builder argument as it does not change the fact
         * that the parent uses a regular Job.
         *
         * * supervisorScope is a coroutine scope function that uses a SupervisorJob instead of a
         * regular Job. It ignores exceptions from its children.
         *
         * * withContext(SupervisorJob()) is not a good replacement for supervisorScope because
         * SupervisorJob is not inherited, and withContext always uses a regular Job.
         *
         * * When calling await on Deferred, it should return the value if the coroutine finished
         * successfully, or it should throw an exception if the coroutine ended with an exception.
         *
         * * CoroutineExceptionHandler is a context that can be used to define default behavior for
         * all exceptions in a coroutine.
         */
    }
}


