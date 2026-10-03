package re

import re.ast.RENode
import re.error.REError
import re.parser.REParser
import scup.ParseError
import slex.LexError

object Main:

  def main(args: Array[String]): Unit =
    // -d anywhere on the command line turns on Util.debug output
    val (flags, files) = args.partition(_ == "-d")
    Util.debug = flags.nonEmpty

    if files.isEmpty then
      println("No file given.")
    else
      val fileName = files(0)
      Util.debug(s"Processing $fileName...")
      try
        val source = scala.io.Source.fromFile(fileName).mkString
        val re: RENode = REParser.parse(source)

        // TODO: print re with your PrettyPrint.

        // TODO: build an NFA from re with your NFABuilder, then write
        // it out with the provided helper:
        //
        //   writeNfaFile(fileName, nfa)

      catch
        case e: LexError   => println(e.getMessage)
        case e: ParseError => println(e.getMessage)
        case e: REError    => println(e.getMessage)

  /**
   * Write nfa to <base>.nfa in the current directory, so running on
   * ex1.re produces ex1.nfa -- exactly the naming the autograder
   * expects.  Provided in full; call it once your NFABuilder works.
   */
  def writeNfaFile(inputFileName: String, nfa: NFA): Unit =
    val base = new java.io.File(inputFileName).getName.stripSuffix(".re")
    val out = new java.io.PrintWriter(base + ".nfa")
    out.print(nfa.toString)
    out.close()
