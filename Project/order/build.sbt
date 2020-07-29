name := """Order"""
organization := "com.cart.inventory"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.10"

crossScalaVersions := Seq("2.12.10", "2.12.3")

libraryDependencies ++= Seq(

  guice,

"com.typesafe.play" %% "play" % "2.8.1",
"mysql" % "mysql-connector-java" % "8.0.20",
"com.typesafe.slick" %% "slick" % "3.3.2",
"com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",

"org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test)


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.cart.inventory.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.cart.inventory.binders._"


lazy val akkaVersion = "2.6.4"

libraryDependencies ++= Seq(
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0-M2" % Test,
  "com.typesafe.akka" %% "akka-actor" % "2.6.4",
  "com.typesafe.akka" %% "akka-stream" % "2.6.4",
  "com.typesafe.akka" %% "akka-stream-kafka" % "2.0.3",
  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.5",
  "com.typesafe.akka" %% "akka-stream-typed" % "2.6.5",
  "com.typesafe.akka" %% "akka-persistence" % "2.6.5",
  "com.typesafe.akka" %% "akka-persistence-query" % "2.6.5",
  //"com.typesafe.akka" %% "akka-http-spray-json" % "10.1.8"
  // "com.typesafe.akka" %% "akka-persistence-experimental" % "2.4-M2"
)

libraryDependencies ++= Seq(
  // "com.typesafe.play" %% "play-akka-http-server" % "2.8.1",
  "com.typesafe.play" %% "play" % "2.8.1",
  "com.typesafe.play" %% "play-guice" % "2.8.1" % Test
)
//incompatiable
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.8.1",
  "io.spray" %% "spray-json" % "1.3.5",
  "io.spray" %% "spray-json" % "1.3.2",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.10.2",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.10.2",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.2",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.10.2"
)

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "8.0.20",
  "com.typesafe.slick" %% "slick" % "3.3.2",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "org.apache.spark" %% "spark-core" % "2.4.3",
  "org.apache.spark" %% "spark-sql" % "2.4.3",
  "org.scalatest" %% "scalatest" % "3.2.0" % Test,
  "org.scalamock" %% "scalamock" % "4.4.0" % Test,
  //"org.slf4j" % "slf4j-nop" % "1.7.30" % Test,
  "com.typesafe.play" %% "play-logback" % "2.8.1"
)

