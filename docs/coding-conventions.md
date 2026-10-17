# Coding conventions

This repository uses four spaces for indentation, UpperCamelCase for classes and type aliases, and
lowerCamelCase for functions and properties. Constants use uppercase names when they are shared
values. Imports are explicit so each example shows the APIs it uses.

Examples keep their small top-level `main` functions because they are learning entries, not a single
production feature. A scope should have a visible owner. Use `runBlocking` for a standalone example
when it gives the example a lifetime; Android code should use the lifecycle or ViewModel scope that
owns the work.

Comments explain behavior that is easy to miss, such as cancellation points, dispatcher choice, or
the difference between cold and hot flows. They do not repeat the name of the function below them.
