package coroutines.exceptions

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

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
                coroutineContext.ensureActive()
                e.printStackTrace()
            }
            delay(2000)
            println("Coroutine finished") // will still get printed
        }


        /**
         * java.lang.Exception: Here is the exception
         * 	at coroutines.exceptions.ExceptionsKt$main$1$1.invokeSuspend(Exceptions.kt:21)
         * 	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
         * 	at kotlinx.coroutines.DispatchedTaskKt.resume(DispatchedTask.kt:234)
         * 	at kotlinx.coroutines.DispatchedTaskKt.dispatch(DispatchedTask.kt:166)
         * 	at kotlinx.coroutines.CancellableContinuationImpl.dispatchResume(CancellableContinuationImpl.kt:397)
         * 	at kotlinx.coroutines.CancellableContinuationImpl.resumeImpl(CancellableContinuationImpl.kt:431)
         * 	at kotlinx.coroutines.CancellableContinuationImpl.resumeImpl$default(CancellableContinuationImpl.kt:420)
         * 	at kotlinx.coroutines.CancellableContinuationImpl.resumeUndispatched(CancellableContinuationImpl.kt:518)
         * 	at kotlinx.coroutines.EventLoopImplBase$DelayedResumeTask.run(EventLoop.common.kt:500)
         * 	at kotlinx.coroutines.EventLoopImplBase.processNextEvent(EventLoop.common.kt:284)
         * 	at kotlinx.coroutines.BlockingCoroutine.joinBlocking(Builders.kt:85)
         * 	at kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking(Builders.kt:59)
         * 	at kotlinx.coroutines.BuildersKt.runBlocking(Unknown Source)
         * 	at kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking$default(Builders.kt:38)
         * 	at kotlinx.coroutines.BuildersKt.runBlocking$default(Unknown Source)
         * 	at coroutines.exceptions.ExceptionsKt.main(Exceptions.kt:17)
         * 	at coroutines.exceptions.ExceptionsKt.main(Exceptions.kt)
         *
         * Coroutine finished
         */

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
                coroutineContext.ensureActive()
                e.printStackTrace()
            }
            delay(2000)
            println("Coroutine finished") // will still get printed
        }

        /**
         * SupervisorJob will ensure all jobs will not cancel is one coroutine fails.
         */
    }
}


