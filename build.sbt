name := "GravityGolf"

version := "1.0"

scalaVersion := "2.10.3"

mainClass in (Compile,run) := Some("game.GravityGolf")

libraryDependencies += "com.novocode" % "junit-interface" % "0.9" % "test"
