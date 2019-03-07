import sbt.Keys.{libraryDependencies, scalaVersion, version}

lazy val root = (project in file(".")).
  settings(
    name := "DDSPhase1",

    version := "0.1.0",

    scalaVersion := "2.11.11",

    organization  := "org.datasyslab",

    publishMavenStyle := true,

    mainClass := Some("SparkSQLExample")
  )

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.2.0"% "compile" ,
  "org.apache.spark" %% "spark-sql" % "2.2.0"% "compile"  ,
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.specs2" %% "specs2-core" % "2.4.16" % "test",
  "org.specs2" %% "specs2-junit" % "2.4.16" % "test"
)

libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % "2.7"


libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.7"