ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

val lwjglVersion = "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "engine4s",
    libraryDependencies ++= Seq(
      "org.lwjgl" % "lwjgl" % lwjglVersion,
      "org.lwjgl" % "lwjgl-glfw" % lwjglVersion,
      "org.lwjgl" % "lwjgl-opengl" % lwjglVersion,


      "org.lwjgl" % "lwjgl" % lwjglVersion classifier "natives-windows",
      "org.lwjgl" % "lwjgl-glfw" % lwjglVersion classifier "natives-windows",
      "org.lwjgl" % "lwjgl-opengl" % lwjglVersion classifier "natives-windows",

      "org.lwjgl" % "lwjgl" % lwjglVersion classifier "natives-macos",
      "org.lwjgl" % "lwjgl-glfw" % lwjglVersion classifier "natives-macos",
      "org.lwjgl" % "lwjgl-opengl" % lwjglVersion classifier "natives-macos"
    )
  )
