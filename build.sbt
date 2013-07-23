name := "unfinagled"

organization := "com.novus"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-http" % "6.5.1",
  "com.twitter" %% "finagle-zipkin" % "6.5.1",
  "net.databinder" %% "unfiltered-netty" % "0.6.8",
  "net.databinder" %% "unfiltered-scalatest" % "0.6.8" % "test"
)

initialCommands := "import com.novus.finagletest._"
