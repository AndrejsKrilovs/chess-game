plugins {
  base
}

val frontendDir = file("frontend")
val npmCommand = if (System.getProperty("os.name").contains("Windows")) "npm.cmd" else "npm"

tasks.register<Exec>("npmInstall") {
  workingDir = frontendDir
  commandLine(npmCommand, "install")
}

tasks.register<Exec>("npmBuild") {
  workingDir = frontendDir
  commandLine(npmCommand, "run", "build")
  dependsOn("npmInstall")
}

tasks.register<Copy>("copyFrontend") {
  dependsOn("npmBuild")
  doFirst {
    delete("backend/src/main/resources/static")
  }
  from("$frontendDir/dist")
  into("backend/src/main/resources/static")
}

project(":backend") {
  plugins.withId("org.springframework.boot") {
    tasks.named("processResources") {
      dependsOn(":copyFrontend")
    }
  }
}

tasks.named("clean") {
  doLast {
    delete("backend/src/main/resources/static")
  }
}