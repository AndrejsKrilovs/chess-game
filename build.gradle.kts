plugins {
  base
}

val frontendDir = file("frontend")
val npmCommand = if (System.getProperty("os.name").contains("Windows")) "npm.cmd" else "npm"

tasks.register<Exec>("npmInstall") {
  workingDir = frontendDir
  commandLine(npmCommand, "install")
  inputs.file("$frontendDir/package.json")
  outputs.dir("$frontendDir/node_modules")
}

tasks.register<Exec>("npmBuild") {
  workingDir = frontendDir
  commandLine(npmCommand, "run", "build")
  dependsOn("npmInstall")
  inputs.dir("$frontendDir/src")
  inputs.file("$frontendDir/package.json")
  outputs.dir("$frontendDir/dist")
}

tasks.register<Copy>("copyFrontend") {
  dependsOn("npmBuild")
  from("$frontendDir/dist")
  into("backend/src/main/resources/static")
  doFirst {
    delete("backend/src/main/resources/static")
  }
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
    delete(layout.projectDirectory.dir("backend/src/main/resources/static"))
  }
}