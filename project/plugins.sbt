addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.1")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.2")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "0.7.0-RC2")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.4")

addCommandAlias("generate-project", ";update-classifiers;gen-idea sbt-classifiers")
