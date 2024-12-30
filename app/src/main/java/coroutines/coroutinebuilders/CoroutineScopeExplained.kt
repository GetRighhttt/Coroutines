package coroutines.coroutinebuilders

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

/*
When we want to use a coroutine inside a suspending function, we use CoroutineScope.
Coroutine Scopes allow us also to cancel multiple coroutines at the same time.

A CoroutineScope is just an interface that holds a reference to a CoroutineContext.
A CoroutineContext is a context that holds all the meta data about the current
coroutine.
Data like name, job, dispatcher, etc.
 */

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
    /*
    CoroutineScope(coroutineContext=[CoroutineName(first_coroutine), JobImpl{Active}@17ed40e0, Dispatchers.Main[missing, cause=java.lang.RuntimeException: Stub!]])
     */

    // we can pass multiple contexts to one scope if we need to
}