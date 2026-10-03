package nfa

import java.io.File

/**
 * Reads a .nfa file into an NFA.  Provided in full.
 *
 * The file format, exactly what re.NFA.toString writes: the number of
 * states n, the accept state, and then one line per state listing its
 * out-edges by label, each label followed by its target states:
 *
 *   7
 *   6
 *   0 a:(1) ;
 *   1 @:(2,3,6) ;
 *   2 b:(4) ;
 *   ...
 */
object NFAReader:

  def read(file: File): NFA =
    val src = scala.io.Source.fromFile(file)
    try parse(src.mkString) finally src.close()

  def parse(text: String): NFA =
    val lines = text.linesIterator.map(_.trim).filter(_.nonEmpty).toVector
    require(lines.size >= 2, "a .nfa file starts with the state count and the accept state")
    val n      = lines(0).toInt
    val accept = lines(1).toInt
    val rows   = lines.drop(2)
    require(rows.size == n, s"expected $n state lines, found ${rows.size}")
    val edge = raw"([a-z@]):\(([0-9,\s]*)\)".r
    val edges = rows.zipWithIndex.map { (row, i) =>
      require(row.split("\\s+")(0) == i.toString, s"line for state $i begins '$row'")
      edge.findAllMatchIn(row).map { m =>
        m.group(1)(0) ->
          m.group(2).split("[,\\s]+").toList.filter(_.nonEmpty).map(_.toInt)
      }.toList.groupMapReduce(_._1)(_._2)(_ ++ _)
    }
    NFA(n, accept, edges)
