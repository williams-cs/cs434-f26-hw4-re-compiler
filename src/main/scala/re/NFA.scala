package re

import java.io.{FileWriter, IOException, PrintWriter}
import scala.collection.mutable

/**
 * A class to represent an NFA.  You should create an NFA from a
 * regular expression using this class.  States are represented
 * as numbers (with 0 always being the start state).  Transitions
 * can be labeled with 'a' - 'z', or '@' to indicate an epsilon
 * transition.
 *
 * The constructor for NFA creates an automaton containing only
 * state 0 and no transitions.  The other NFA methods permit you
 * to add new states and edges to the NFA, as well as to print out
 * an NFA in a form suitable for the NFASimulator (or for viewing with
 * dot).
 */
class NFA:

  private var acceptState = -1

  /** states(i) maps each edge label to the targets of i's out-edges */
  private val states = mutable.ArrayBuffer[mutable.LinkedHashMap[Char, mutable.ArrayBuffer[Int]]]()

  // Create the NFA with state 0.
  states += mutable.LinkedHashMap()

  /**
   * Set the accepting state for the NFA.  Must be a previously
   * allocated state.
   */
  def setAcceptState(state: Int): Unit =
    Util.assertTrue(0 <= state && state < states.size, "Bad State: " + state)
    acceptState = state

  /**
   * Add a transition to the NFA.
   *
   * @param start  must be a valid state in the NFA
   * @param end    must be a valid state in the NFA
   * @param c      the character on the edge.  'a' <= c <= 'z', or c == '@' for epsilon
   */
  def addTransition(start: Int, end: Int, c: Char): Unit =
    Util.assertTrue(0 <= start && start < states.size, "Bad State: " + start)
    Util.assertTrue(0 <= end && end < states.size, "Bad State: " + end)
    Util.assertTrue('a' <= c && c <= 'z' || c == '@', "Bad symbol: '" + c + "'")
    states(start).getOrElseUpdate(c, mutable.ArrayBuffer()) += end

  /**
   * Add a new state to the NFA and return its id number.
   */
  def newState(): Int =
    val n = states.size
    states += mutable.LinkedHashMap()
    n

  /**
   * Return a String representing the NFA in a way that can be
   * read in by <tt>nfa.NFASimulator</tt>.
   */
  override def toString: String =
    val result = new StringBuilder
    result ++= states.size + "\n"
    result ++= acceptState + "\n"
    for i <- states.indices do
      result ++= i + " "
      for (c, targets) <- states(i) do
        result ++= c + ":(" + targets.mkString(",") + ") "
      result ++= ";\n"
    result.toString

  /**
   * Creates a file <tt>nfa.dot</tt> containing a graphical description
   * of the NFA.  That file can then be processed with the <tt>dot</tt>
   * utility:  dot -Tpdf < nfa.dot > nfa.pdf
   */
  def printDot(): Unit =
    val result = new StringBuilder
    result ++= "digraph G {\n    rankdir=LR;\n"
    for i <- states.indices do
      if acceptState == i then
        result ++= "  S" + i + "[label=\"" + i + "\", peripheries=2,regular=true];\n"
      else
        result ++= "  S" + i + "[label=\"" + i + "\",regular=true];\n"
    for i <- states.indices; (c, targets) <- states(i); j <- targets do
      result ++= "  S" + i + " -> S" + j + "[label=\"" + c + "\"];\n"
    result ++= "}\n"
    try
      val p = new PrintWriter(new FileWriter("nfa.dot"))
      p.println(result.toString)
      p.close()
    catch
      case e: IOException => println(e)
