val Http4sVersion = "0.20.15"
val CirceVersion = "0.11.1"
val BetterFilesVersion = "3.8.0"
val Specs2Version = "4.1.0"
val LogbackVersion = "1.2.3"

lazy val root = (project in file("."))
  .settings(
    organization := "com.talkdesk",
    name := "talkdesk-code-challenge",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.10",
    libraryDependencies ++= Seq(
      "org.http4s"             %% "http4s-blaze-server"        % Http4sVersion,
      "org.http4s"             %% "http4s-blaze-client"        % Http4sVersion,
      "org.http4s"             %% "http4s-circe"               % Http4sVersion,
      "org.http4s"             %% "http4s-dsl"                 % Http4sVersion,
      "io.circe"               %% "circe-generic"              % CirceVersion,
      "com.github.pathikrit"   %% "better-files"               % BetterFilesVersion,
      "org.specs2"             %% "specs2-core"                % Specs2Version % "test",
      "ch.qos.logback"         %  "logback-classic"            % LogbackVersion
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.0")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
  "-Xfatal-warnings",
)
