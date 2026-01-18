val scala3Version = "$scala_version$"

lazy val root = project
  .in(file("."))
  .settings(
    name         := "$name$",
    version      := "0.1.0-SNAPSHOT",
    organization := "$organization$",
    scalaVersion := scala3Version,

    // Scala 3 compiler flags
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Xmax-inlines:256",
      "-Yretain-trees"
    ),

    // Zefir dependencies
    libraryDependencies ++= Seq(
      // Zefir core
      "$zefir_group_id$" %% "pekko-postgres-r2dbc-journal" % "$zefir_version$",

      // HTTP4s
      "org.http4s" %% "http4s-ember-server" % "$http4s_version$",
      "org.http4s" %% "http4s-dsl"          % "$http4s_version$",
      "org.http4s" %% "http4s-circe"        % "$http4s_version$",

      // Circe JSON
      "io.circe" %% "circe-core"    % "$circe_version$",
      "io.circe" %% "circe-generic" % "$circe_version$",
      "io.circe" %% "circe-parser"  % "$circe_version$",

      // Testing
      "org.scalameta" %% "munit" % "1.0.3" % Test
    ),

    // Fork for Pekko
    run / fork := true,

    // JVM options for virtual threads
    run / javaOptions ++= Seq(
      "--enable-preview",
      "--add-opens=java.base/java.lang=ALL-UNNAMED"
    )
  )
