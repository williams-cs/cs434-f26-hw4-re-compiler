# RE Compiler

Translate regular expressions into NFAs.

The lexer for the regular expression language is provided in full
(the `re.slex` spec at the root of this repository).  Your job,
described in the HW 4 handout:

1. Complete the grammar in `re.scup` (the stub parses a single
   letter); `src/main/scala/re/parser/REParser.scala` is the provided
   host that loads it, and is where your let-binding resolve pass
   goes.
2. Design the AST hierarchy in `src/main/scala/re/ast/RENode.scala`.
3. Write `PrettyPrint` to print the AST back out.
4. Write an `NFABuilder` that translates an `RENode` into an `NFA`
   (see `src/main/scala/re/NFA.scala`) using Thompson's construction.

The NFA simulator is provided in full in `src/main/scala/nfa/`.

# To Compile:

    sbt compile

# To Run:

    sbt "run ex1.re"

# To Test:

    sbt test

Some provided tests fail until your grammar handles the full language.
`re.PipelineTests` runs the whole compiler on every `tests/X.re` (as
the autograder does) and checks the simulator's answers against
`tests/X.re.expected`; those fail until `re.Main` writes the NFA.

# To simulate a generated NFA:

    sbt "runMain nfa.NFASimulator ex1.nfa ab cow abccccc"
