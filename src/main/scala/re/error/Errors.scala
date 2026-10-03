package re.error

/**
 * An exception class to indicate an error while processing regular
 * expressions, for example a regular expression that refers to an id
 * that is not defined.  (Lexical and syntax errors are the parsing
 * libraries' own exceptions: slex.LexError and scup.ParseError.)
 */
class REError(message: String) extends Error(message)
