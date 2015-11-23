name := """cryptsy"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
//  evolutions,
  jdbc,
  cache,
  ws,
  "com.pusher" % "pusher-java-client" % "1.0.0",
  "mysql" % "mysql-connector-java" % "5.1.37",
  specs2 % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
resolvers += "clojars" at "http://clojars.org/repo"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
