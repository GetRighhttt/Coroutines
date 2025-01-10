What are the best practices for Coroutines?

1. Inject Dispatchers to enable testing
2. Suspend functions should be safe to call from the main thread
3. ViewModels should create Coroutines instead of exposing suspend functions
   to perform business logic
4. The data and business layer should expose Suspend Functions or Flows
5. Avoid GlobalScope; Use your own custom scope instead if need application usage
6. Make Coroutines Cancellable