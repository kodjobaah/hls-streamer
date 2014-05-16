
import com.typesafe.sbt.SbtNativePackager._
import spray.revolver.RevolverPlugin.Revolver

name := "hls-streamer"

version := "1.0"

scalaVersion := "2.10.3"

//packagerSettings

packageArchetype.java_application

resolvers ++= Seq(
  "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "anormcypher" at "http://repo.anormcypher.org/",
  "Spray repository" at "http://repo.spray.io/"
)

libraryDependencies ++= {
  val akkaV = "2.2.3"
  val sprayV = "1.2.0"
  val atmosV = "1.3.0"
  Seq(
//  "org.java-websocket"  %   "Java-WebSocket" % "1.3.1",
    "io.spray"            %%  "spray-json"     % "1.2.5",
    "io.spray"            %   "spray-can"      % sprayV,
    "io.spray"            %   "spray-routing"  % sprayV,
    "io.spray"            %   "spray-client"   % "1.2.0",
    "com.typesafe.akka"   %%  "akka-actor"     % akkaV,
    "com.typesafe.atmos"  %   "trace-akka-2.2.1_2.10"  % atmosV ,
    "com.typesafe.akka"   %%  "akka-slf4j"     % "2.2.3",
    "joda-time"           %   "joda-time"       % "2.2",
    "org.mockito"         %   "mockito-all"     % "1.9.5",
    "org.apache.commons"  %   "commons-io"      % "1.3.2",
    "commons-logging"     %   "commons-logging" % "1.1.1",
    "org.greencheek.spray" %  "spray-cache-spymemcached" % "0.1.6",
    "org.ostermiller"     %   "utils"          % "1.07.00",
    "net.java.dev.jna"    %   "jna"            % "3.5.2",
    "org.apache.commons"  %   "commons-lang3"  % "3.0",
    "commons-cli"         %   "commons-cli"    % "1.2",
    "ch.qos.logback"      %   "logback-core"   % "1.0.13",
    "ch.qos.logback"      %   "logback-classic" % "1.0.13",
    "ch.qos.logback"      %   "logback-access"  % "1.0.13",
    "org.mindrot"         %   "jbcrypt"         % "0.3m",
    "org.anormcypher"     %%  "anormcypher"     % "0.4.3",
    "org.apache.commons"  %   "commons-email"   % "1.3.1",
    "io.spray"            %   "spray-can"       % "1.0",
    "org.apache.mina"     %   "mina-core"     % "2.0.7",
    "com.typesafe.akka"   %%  "akka-testkit"   % akkaV   % "test",
    "io.spray"            %   "spray-testkit"  % sprayV  % "test",
    //"org.scalatest"       %%  "scalatest"      % "2.1.5"   % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test",
    "org.neo4j"           %   "neo4j-kernel"    % "2.0.0-M06" % "test" classifier "tests" classifier "",
    "org.neo4j"           %   "neo4j-cypher" % "2.0.0-M06" % "test",
    "junit"               %   "junit"          % "4.11"  % "test",
    //"org.specs2"          %%  "specs2"         % "2.3.11" % "test",
    //"org.scalamock" %% "scalamock-specs2-support" % "3.0.1" % "test"
    "com.jhlabs"          %   "filters"        % "2.0.235-1")
}



scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)

parallelExecution in Test := false

javaOptions in run ++= Seq(
  "-javaagent:../lib/weaver/aspectjweaver.jar",
  "-Dorg.aspectj.tracing.factory=default",
  "-Djava.library.path=../lib/sigar"
)

//javaOptions in run += "-javaagent:" + System.getProperty("user.home") + "/.ivy2/cache/org.aspectj/aspectjweaver/jars/aspectjweaver-1.7.3.jar"

fork in run := true

connectInput in run := true

outputStrategy in run := Some(StdoutOutput)

net.virtualvoid.sbt.graph.Plugin.graphSettings

mainClass in (Compile, run) := Some("VideoProcessingServer")

//Revolver.settings.settings
seq(Revolver.settings: _*)

