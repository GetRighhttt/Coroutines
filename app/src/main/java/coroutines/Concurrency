What is Concurrency?

- CONCURRENCY is when multiple tasks run simultaneously while acting on a SINGLE core processing unit.
Usually when working with THREADS, when tasks run in parallel, they do so on MULTIPLE core processing units.
Concurrency alleviates that by running on a single processing unit, yet still allowing multiple tasks to run at the
same time, but not necessarily in parallel.

Multithreading Examples(Main types):
Sequential -> happening in a sequence.
Parallel -> happening simultaneously with multiple core processing units.
Concurrent -> happening when one or more processing units can start, stop, run, etc. on a single
core; when one or more processing units can oscillate states on a single core.

What is Structured Concurrency?

- Structured Concurrency is essentially a hierarchy of coroutines that aims to improve the readability
and ease of use when working with asynchronous tasks and programming. It's the idea that new coroutines
can only be launched within a Coroutine Scope which determines the lifetime of the coroutine. It's
when a parent coroutine has child coroutines, and there exists a relationship between the two that
 has certain constraints. It's a parent coroutine, and child coroutines that inherit the context of
 the parent coroutine, which aids in cancellation, development, and structure of coroutines.
 Example: Exceptions with coroutines propagate up from the child to the parent which is why they
 must be handled properly.

Here are the rules that constraints that arise when using Structured Concurrency:
A. Children inherit context from their parent;
B. If a child coroutine raises an error, the parent coroutine also has an error;
C. When a parent is canceled, it's children are also canceled; and
D. A parent coroutine suspends until all of its children have finished their tasks.