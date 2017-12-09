name := "DepopPrep"

version := "0.1"

scalaVersion := "2.12.4"


libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc"        % "3.1.+",
  "com.h2database"  %  "h2"                 % "1.4.+",
  "ch.qos.logback"  %  "logback-classic"    % "1.2.+"
)

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-optics"
).map(_ % "0.8.0")

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"

