package nfa

/**
 * An NFA as read from a .nfa file written by re.NFA.toString: the
 * states are 0 until numStates, state 0 is the start state, and
 * edges(q) maps each edge label ('a'-'z', or '@' for epsilon) to the
 * states reachable from state q by an edge with that label.
 * Provided in full.
 *
 * epsClose and accepts are the set-of-states simulation from Lab 1
 * (Dragon Algorithm 3.22), over this format's a-z alphabet.
 */
case class NFA(numStates: Int, accept: Int,
               edges: IndexedSeq[Map[Char, List[Int]]]):

  /** The states reachable from state q by one edge labeled c. */
  def step(q: Int, c: Char): List[Int] = edges(q).getOrElse(c, Nil)

  /**
   * The epsilon-closure of states: every state reachable from a
   * state in `states` by following zero or more epsilon edges.
   */
  def epsClose(states: Set[Int]): Set[Int] =
    var closure = states
    var worklist = states.toList
    while worklist.nonEmpty do
      val q = worklist.head
      worklist = worklist.tail
      for r <- step(q, '@') if !closure.contains(r) do
        closure += r
        worklist = r :: worklist
    closure

  /**
   * Does this NFA accept s?  Start with the closure of {0}; for each
   * character of s, move every state in the set one step and close
   * again; accept if the final set contains the accept state.  A
   * character outside a-z has no transitions anywhere.
   */
  def accepts(s: String): Boolean =
    var current = epsClose(Set(0))
    for c <- s do
      val moved =
        if 'a' <= c && c <= 'z' then current.flatMap(step(_, c))
        else Set.empty[Int]
      current = epsClose(moved)
    current.contains(accept)
