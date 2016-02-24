name := "chapter03-fault-tolerance"

version := "1.0"

scalaVersion := "2.11.7"

organization := "net.imadz"

libraryDependencies ++= {
  val akkaVersion = "2.3.10"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "org.scalatest" %% "scalatest" % "2.2.0" % "test"
  )
}