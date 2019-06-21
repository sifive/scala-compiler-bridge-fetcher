
ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "com.sifive"

lazy val root = (project in file("."))
  .settings(
    name := "scala-bridge-fetcher",
    libraryDependencies += "ch.epfl.scala" %% "bloop-backend" % "1.2.5",
    // Override because the needed version of bsp4s is not available
    libraryDependencies += "ch.epfl.scala" %% "bsp4s" % "2.0.0-M3"
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
