What is Continuation in Coroutines?

Continuation - saves the state of the coroutine, and then manages the execution flow according to
callbacks; essentially saves coroutine state, and then determines when to resume execution. Suspend
functions accept a continuation parameter, and Continuation in itself is an interface with a
Coroutine Context.

- Continuation in Coroutines is really what coroutines are all about. Suspension and resuming later
on is the main idea behind how coroutines work. When a coroutine is suspended, that thread is free
for others coroutines to do work. Continuation doesn't have to occur on the same thread. We can use
Continuation in Coroutines to add on additional functionality to a coroutine when it is called to
resume execution.