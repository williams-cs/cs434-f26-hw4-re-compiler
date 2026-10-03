package re.ast

/**
 * The root of the AST hierarchy for regular expressions.
 *
 * TODO: design a reasonable, *minimal* collection of case classes for
 * regular expression ASTs.  Not every concrete syntactic form needs an
 * analog in the abstract syntax (for example, "[abc]" can be expressed
 * as an alternation, and let-bindings need not appear in the tree at
 * all if your parser substitutes them away).
 */
sealed abstract class RENode

/** The empty string, written @ */
case class REEmpty() extends RENode
