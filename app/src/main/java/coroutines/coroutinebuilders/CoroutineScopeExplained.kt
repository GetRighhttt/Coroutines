package coroutines.coroutinebuilders

/*
When we want to use a coroutine inside a suspending function, we use CoroutineScope.
Coroutine Scopes allow us also to cancel multiple coroutines at the same time.

A CoroutineScope is just an interface that holds a reference to a CoroutineContext.
A CoroutineContext is a context that holds all the meta data about the current
coroutine.
Data like name, job, dispatcher, etc.
 */