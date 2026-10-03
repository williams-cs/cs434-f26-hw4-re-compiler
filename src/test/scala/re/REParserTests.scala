package re

import re.ast.*
import re.error.*
import re.lex.{RELexer, TokenKind}
import re.parser.REParser

/**
 * Sample unit tests.  The first group passes with the starter; the
 * second group fails until you complete the parser and PrettyPrint.
 * Add your own tests as you go -- especially for your NFABuilder,
 * using the `accepts` helper below.  PipelineTests runs the whole
 * compiler on the files in tests/.
 *
 * Run these with `sbt test`.
 */
class REParserTests extends munit.FunSuite {

  /*----------------------------- helper ------------------------------*/

  /** Does the NFA that nfaText describes (in NFA.toString's format)
    * accept s?  Runs the provided simulator in package nfa. */
  def accepts(nfaText: String, s: String): Boolean =
    nfa.NFAReader.parse(nfaText).accepts(s)

  /*------------------ these pass with the starter --------------------*/

  test("The lexer tokenizes operators and keywords") {
    val toks = RELexer.tokenize("let X = a.b* in X|@")
    assertEquals(toks.map(_.kind).toList,
      List(TokenKind.LET, TokenKind.ID, TokenKind.EQ, TokenKind.LETTER,
           TokenKind.DOT, TokenKind.LETTER, TokenKind.STAR, TokenKind.IN,
           TokenKind.ID, TokenKind.BAR, TokenKind.EPSILON))
  }

  test("The stub parser accepts one letter") {
    REParser.parse("a")
  }

  test("NFAs print in simulator format") {
    val nfa = new NFA()
    val s1 = nfa.newState()
    nfa.addTransition(0, s1, 'a')
    nfa.setAcceptState(s1)
    assertEquals(nfa.toString, "2\n1\n0 a:(1) ;\n1 ;\n")
  }

  /*------------- these fail until you finish the TODOs ---------------*/

  test("Concatenation and alternation parse") {
    REParser.parse("a.(b|c)*")
  }

  test("Let-bindings parse") {
    REParser.parse("let X = a in X.X")
  }

  test("PrettyPrint is fully parenthesized") {
    assertEquals(PrettyPrint.print(REParser.parse("a.b")), "(a.b)")
    assertEquals(PrettyPrint.print(REParser.parse("a|@")), "(a|@)")
  }

  // Once you have an NFABuilder, test it here with the accepts
  // helper, e.g.:
  //
  //   test("Thompson construction: a.(b|c)*") {
  //     val nfa = NFABuilder.build(REParser.parse("a.(b|c)*")).toString
  //     assert(accepts(nfa, "abccccc"))
  //     assert(!accepts(nfa, "cow"))
  //   }
}
