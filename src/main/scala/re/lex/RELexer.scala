package re.lex

import scup.Token
import slex.LexSpec

/**
 * The kinds of tokens in the regular expression language.
 *
 *   LETTER   a lowercase letter 'a'-'z' (the text is the letter)
 *   ID       a name bound by let: [A-Z][a-z]*
 *   LET, IN  the keywords "let" and "in"
 *   EPSILON  the symbol @
 *   ...and one kind for each punctuation symbol.
 */
enum TokenKind:
  case LETTER, ID, LET, IN, EPSILON,
       BAR, STAR, PLUS, QUESTION, DOT, EQ,
       OPAREN, CPAREN, OBRACKET, CBRACKET

/**
 * A lexer for the regular expression language, driven by the re.slex
 * spec file.  Provided in full.  Whitespace and both comment forms
 * (// and block comments) are skipped.
 */
object RELexer:

  /** The spec-driven rule table (loaded once per JVM). */
  val spec: LexSpec[TokenKind] = LexSpec.load[TokenKind]("re.slex")

  def tokenize(source: String): IndexedSeq[Token[TokenKind]] =
    spec.tokenize(source)
