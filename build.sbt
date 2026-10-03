name := "recompiler"

scalaVersion := "3.3.6"

libraryDependencies += "org.scalameta" %% "munit" % "1.1.1" % Test

Compile / run / mainClass := Some("re.Main")

// PipelineTests runs re.Main in a fresh JVM per test; forking gives
// the test JVM the real project classpath to launch it with.
Test / fork := true

// Spec files: assemble the .slex/.scup action blocks at build time
// (Path A; see project/SpecGen.scala), and keep the editor wiring
// current on every sbt load.
Compile / sourceGenerators += Def.task {
  scup.SpecGen.generateAll(
    baseDirectory.value, (Compile / sourceManaged).value / "specgen")
}.taskValue

Global / onLoad := (Global / onLoad).value.andThen { s =>
  scup.VscodeSetup(new java.io.File(".")); s
}

// Spec files also ride in the jar as resources, so an assembled jar
// (or a grader running it from another directory) is self-contained.
Compile / unmanagedResources ++= (baseDirectory.value * ("*.slex" | "*.scup")).get
