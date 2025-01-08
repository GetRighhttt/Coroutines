package coroutines.coroutinebuilders

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking

/**
 * @author Stefan Bayne
 */

/**
 * When we want to use a coroutine inside a suspending function, we use CoroutineScope.
 * Coroutine Scopes allow us also to cancel multiple coroutines at the same time.
 *
 * A CoroutineScope is just an interface that holds a reference to a CoroutineContext.
 *
 * A CoroutineContext is a context that holds all the meta data about the current
 * coroutine.
 * Data like name, job, dispatcher, etc. of that current coroutine. So when we declare a coroutine, we
 * can actually pass more than just a Dispatcher to a a scope.
 *
 * Ex:
 * withContext(Dispatchers.IO + CoroutineName("") + Job() + CoroutineExceptionHandler {_,_ -> e. printStackTrace()})
 *
 *So essentially, a CoroutineScope is a wrapper for a CoroutineContext which is a wrapper for the
 * Dispatcher, Job, Name, State, etc. of the coroutine.
 *
 **/

fun main() {

    runBlocking {

        val handler = CoroutineExceptionHandler
        val context = coroutineContext[Job]
        val coroutineContextEx = CoroutineScope(
            Dispatchers.Main
                    + CoroutineName("first_coroutine")
                    + Job()
        )
        print(coroutineContextEx)
    }
    /**
     * CoroutineScope(coroutineContext=[CoroutineName(first_coroutine), JobImpl{Active}@17ed40e0, Dispatchers.Main[missing, cause=java.lang.RuntimeException: Stub!]])
     */
}