credentials += Credentials(Path.userHome / ".ivy2" / ".novus_nexus")

resolvers ++= Seq(
  Resolver.url("artifactory", url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns),
  Resolver.url("typesafe-ivy", url("http://repo.typesafe.com/typesafe/ivy-releases/"))(Resolver.ivyStylePatterns),
  "Novus Snapshots (public)" at "http://repo.novus.com/snapshots/"
)

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.4.0")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.1")

addSbtPlugin("org.ensime" % "ensime-sbt-cmd" % "0.1.1")

addSbtPlugin("com.orrsella" % "sbt-sublime" % "1.0.5")