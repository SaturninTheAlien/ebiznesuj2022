val ktor_version="2.0.0-beta-1"
val logback_version="1.2.3"

plugins {
    application
    kotlin("jvm") version "1.6.10"
}

application {
    // Define the main class for the application.
    mainClass.set("kogutex.AppKt")

    tasks.run.get().workingDir = rootProject.projectDir
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}


repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("io.ktor:ktor-client-core:$ktor_version")
    
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")

    implementation("io.ktor:ktor-client-auth:$ktor_version")

    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    
}

tasks {
    withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
}
}

