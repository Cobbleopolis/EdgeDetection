name := "EdgeDetection"

version := "0.1"

scalaVersion := "2.12.4"

val lwjglVersion: String = "3.1.5"

libraryDependencies ++= Seq(
    "org.lwjgl" % "lwjgl" % lwjglVersion,
    "org.lwjgl" % "lwjgl-assimp" % lwjglVersion,
    "org.lwjgl" % "lwjgl-glfw" % lwjglVersion,
    "org.lwjgl" % "lwjgl-opengl" % lwjglVersion,
    "org.lwjgl" % "lwjgl-stb" % lwjglVersion,
    "org.lwjgl" % "lwjgl" % lwjglVersion classifier "natives-windows" classifier "natives-linux" classifier "natives-macos",
    "org.lwjgl" % "lwjgl-assimp" % lwjglVersion classifier "natives-windows" classifier "natives-linux" classifier "natives-macos",
    "org.lwjgl" % "lwjgl-glfw" % lwjglVersion classifier "natives-windows" classifier "natives-linux" classifier "natives-macos",
    "org.lwjgl" % "lwjgl-opengl" % lwjglVersion classifier "natives-windows" classifier "natives-linux" classifier "natives-macos",
    "org.lwjgl" % "lwjgl-stb" % lwjglVersion classifier "natives-windows" classifier "natives-linux" classifier "natives-macos",
    "org.joml" % "joml" % "1.9.6"
)

crossPaths := false