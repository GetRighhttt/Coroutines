What are Flow Operators?

- Operators are methods within the reactive paradigm that allow you to manipulate and transform flow(
stream of data emitted over time). These operators can enable you to finish, start, filter, map,
combine, etc. to get your desired output.

There are 3 types of operators that can be performed on Flows:
1. Terminal Operators - terminates a flow and ensures completion
2. Intermediate Operators - operators that are performed in between the flow emission and terminal
operators
3. Lifecycle Operators - operators that perform operations on the lifecycle of the flow

Flow operators also take into account upstream and downstream when considering flow processing.
Flow Processing - the flow of how operators work together when chained. Certain operators work for
upstream however most are considered downstream operators.

Here is a link that list most of all the operators used:
https://medium.com/@arshamjafari85/mastering-flow-operators-in-kotlin-a-comprehensive-guide-192ac58e7104
https://medium.com/@engineermuse/mastering-your-flow-a-guide-to-kotlin-flow-terminal-operators-01535cfb0f85