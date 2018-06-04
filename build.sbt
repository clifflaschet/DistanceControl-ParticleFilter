lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.functional.ai.distancecontrol.particlefilter",
      scalaVersion := "2.12.5",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "distancecontrol-particlefilter",
    libraryDependencies ++= Seq(
      "org.scalanlp" %% "breeze" % "0.13.2"
    )
  )