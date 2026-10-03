package re.parser

import scup.GrammarSpec
import re.ast.*
import re.lex.RELexer

/**
 * The parser for the regular expression language, driven by the
 * re.scup spec file -- which is where your grammar work goes.  This
 * object is the host: it loads the grammar against re.slex's token
 * binding and runs the parse.  A malformed input raises scup's
 * ParseError.
 *
 * When you add let-bindings, have re.scup's actions build your own
 * intermediate tree and add a resolve pass here, raising
 * re.error.REError for an unbound name (see the notes at the top of
 * re.scup).
 */
object REParser:

  private lazy val g = GrammarSpec.load("re.scup", RELexer.spec.binding)

  /** Parse a whole regular expression from source text. */
  def parse(source: String): RENode =
    g.parseAs[RENode](RELexer.tokenize(source))
