What is Reactive Programming?
- A paradigm based on functional programming in which we model our logic as a set of operations in
a data stream. Reactive programming is largely based on observing some data, and performing some
operations based on the data returned. They aim to minimize the number of threads through intelligent
scheduling that is abstracted away from the programmer.

Reactive programming is relying on asynchronous programming to complete tasks and deliver information
to otherwise static content, which is essentially the Observable design pattern; observe some data
and perform operations on said data.

Four principles:
- Responsive: ensuring a system is responsive or responds regularly
- Resilient: resilient to failures by way of: delegation, replication, containment and isolation.
- Elastic: stretches in response to work load; scalable.
- Message-Driven: communicating via asynchronous message passing. You can continue work while
messages are passed.

The difference between coroutines and reactive programming?
- Coroutines provide a structured (structured concurrency) way to write sequential-looking asynchronous
code in an imperative manner.
- Reactive Programming emphasizes handling data as streams of events and reacting to changes in a
declarative manner, often using complex operators to manage data flow through an application.