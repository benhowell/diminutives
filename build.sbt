name := "diminutives"

version := "1.0"

scalaVersion := "2.10.3"


unmanagedJars in Compile <++= baseDirectory map { base =>
  val baseDirectories = file(scala.util.Properties.javaHome + "/lib") +++ file("/usr/lib/akka/akka-2.3.0/lib/akka")
  val customJars = baseDirectories ** "*.jar"
  customJars.classpath
}

fork in run := true
