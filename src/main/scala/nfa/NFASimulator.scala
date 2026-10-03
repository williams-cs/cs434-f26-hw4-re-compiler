package nfa

import java.io.File

/**
 * The NFA simulator: read the NFA named by the first command-line
 * argument and print yes/no for each remaining argument.  Provided in
 * full.  Run it as
 *
 *   sbt "runMain nfa.NFASimulator ex1.nfa ab cow abccccc a"
 */
object NFASimulator:

  def main(args: Array[String]): Unit =
    if args.length == 0 then println("usage: runMain nfa.NFASimulator <file.nfa> <string> ...")
    else
      val nfa = NFAReader.read(new File(args(0)))
      for s <- args.drop(1) do
        println(s + ": " + (if nfa.accepts(s) then "yes" else "no"))
