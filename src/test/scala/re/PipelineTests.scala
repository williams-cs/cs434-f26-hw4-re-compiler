package re

import java.io.File
import java.nio.file.Files
import nfa.NFAReader

/**
 * End-to-end tests, run exactly as the autograder runs them: for each
 * tests/X.re, run re.Main on it in a fresh JVM inside a scratch
 * directory, read the one file X.nfa it must write there, and run
 * the NFA simulator on every string in tests/X.re.expected, whose
 * lines are the simulator's expected output ("ab: yes", ": no" for
 * the empty string, ...).
 *
 * These all fail until re.Main builds and writes the NFA.
 */
class PipelineTests extends munit.FunSuite {

  val testDir = new File("tests")

  /** Run re.Main on reFile in a scratch directory; return the NFA it wrote. */
  def compile(reFile: File): nfa.NFA = {
    val dir = Files.createTempDirectory("recompiler").toFile
    Files.copy(reFile.toPath, new File(dir, reFile.getName).toPath)
    val java = new File(System.getProperty("java.home"), "bin/java").getPath
    val proc = new ProcessBuilder(java, "-cp", System.getProperty("java.class.path"),
                                  "re.Main", reFile.getName)
      .directory(dir).redirectErrorStream(true).start()
    val output = new String(proc.getInputStream.readAllBytes())
    proc.waitFor()
    val want = reFile.getName.stripSuffix(".re") + ".nfa"
    val wrote = dir.listFiles.map(_.getName).filter(_.endsWith(".nfa")).toList
    assertEquals(wrote, List(want),
      s"re.Main must write exactly one file, $want\nre.Main output:\n$output")
    NFAReader.read(new File(dir, want))
  }

  for (reFile <- Option(testDir.listFiles).getOrElse(Array.empty[File]).sortBy(_.getName)
       if reFile.getName.endsWith(".re")) {
    test(reFile.getName) {
      val expected = scala.io.Source.fromFile(reFile.getPath + ".expected")
        .getLines().filter(_.nonEmpty).toList
      val nfa = compile(reFile)
      val actual = expected.map { line =>
        val s = line.substring(0, line.lastIndexOf(": "))
        s + ": " + (if (nfa.accepts(s)) "yes" else "no")
      }
      assertEquals(actual, expected)
    }
  }

  test("the test directory is present") {
    assert(testDir.isDirectory, s"no ${testDir.getAbsolutePath}")
  }
}
