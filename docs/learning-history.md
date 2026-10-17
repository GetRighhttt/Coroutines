# How this repository grew

I started this repository to learn Kotlin coroutines by writing small examples and notes. Each topic
captures a question I wanted to answer: how suspension works, how coroutine lifetimes are owned, how
flows deliver values, and what happens when work is cancelled or fails.

The repository has two kinds of material:

- Kotlin files contain runnable examples that make one behavior visible.
- Markdown notes keep the explanations and comparisons readable without hiding them inside source
  files.

The cleanup keeps that progression. It moves prose notes into `docs/topics`, gives them normal
Markdown names, and keeps examples that show a concept even when the code is intentionally small.
The current pass corrects examples that could hang, outlive their owner, or describe behavior the
code does not show. Package names now match the topic names, so new examples have a predictable
place to live.

Git history keeps the earlier wording and experiments. Use `git log --follow` on a topic file when
you want to see how an explanation changed over time.
