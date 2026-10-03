package re

/**
 * Small utilities: assertions and optional debug output.
 */
object Util:

  /** Fail with message if test is false.  Use this liberally. */
  def assertTrue(test: Boolean, message: String): Unit =
    if !test then throw new IllegalStateException(message)

  /** Set to true (the -d command-line flag) to enable debug output. */
  var debug = false

  /** Print a message, but only when debugging is enabled. */
  def debug(message: String): Unit =
    if debug then println(s"[$message]")
