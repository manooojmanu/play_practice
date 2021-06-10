name := "PlayPractice"

version := "1.0"

scalaVersion := "2.12.12"

lazy val `playpractice` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
//  jdbc,
  ehcache,
  ws,
  specs2 % Test,
  guice,
  "mysql" % "mysql-connector-java" % "8.0.22",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0" % Compile,
  "org.typelevel" %% "cats-core" % "2.3.0",
  "com.typesafe.play" %% "play-slick" % "4.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0",
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.0.2",
  "io.gatling" % "gatling-test-framework" % "3.0.2",
  "com.github.ben-manes.caffeine" % "caffeine" % "2.5.6"
)

routesImport += "com.manoj.actors.DivisionActor.Divide"
unmanagedResourceDirectories in Test <+= baseDirectory(
  _ / "target/web/public/test"
)
//For Cats
scalacOptions += "-Ypartial-unification"

enablePlugins(GatlingPlugin)
lazy val GTest = config("gatling") extend (Test)
lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .enablePlugins(GatlingPlugin)
  .configs(GTest)
  .settings(inConfig(GTest)(Defaults.testSettings): _*)


scalaSource in GTest := baseDirectory.value / "/gatling/com/manoj/gatling"