Global / onChangedBuildSource := ReloadOnSourceChanges

name := "vigil-test"
version := "0.0.1"

scalaVersion := "2.13.10"

javacOptions := Seq(
  "-source",
  "17",
  "-target",
  "17"
)

scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps",
  "-language:higherKinds",
  "-language:existentials",
  "-Wconf:cat=other-match-analysis:error",
  "-Wunused",
  "-Xfatal-warnings",
  "-Ymacro-annotations",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Ywarn-dead-code",
  "-Ywarn-unused",
  "-Yrepl-class-based"
)

libraryDependencies ++= Seq(
  compilerPlugin("org.augustjune" %% "context-applied"    % "0.1.4"),
  "co.fs2"            %% "fs2-io"                   % "3.6.1",
  "org.scalatest"     %% "scalatest-shouldmatchers" % "3.2.15",
  "org.scalatest"     %% "scalatest-funspec"        % "3.2.15",
)
