plugins {
    kotlin("jvm") version "2.0.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "dev.sparky200"
version = "0.0.1"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

    maven("https://repo.skriptlang.org/releases")
}

// provided contains dependencies intended to use the spigot plugin loader
// provided dependencies MUST BE EXPLICIT
val provided: Configuration by configurations.creating

// configure compileOnly to pull from provided dependencies
configurations {
    compileOnly {
        extendsFrom(provided)
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.1-R0.1-SNAPSHOT")

    provided("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")

    compileOnly("com.github.SkriptLang:Skript:2.9.1")
    compileOnly("org.jetbrains:annotations:24.1.0")

    testImplementation(kotlin("test"))
}

tasks.runServer {
    minecraftVersion("1.21")

    downloadPlugins {
        github("SkriptLang", "Skript", "2.9.1", "Skript-2.9.1.jar")
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks {
    processResources {
        expand(
            "version" to version,
            "libraries" to provided.dependencies.joinToString(
                prefix = "\n  - ",
                separator = "\n  - "
            ) { "${it.group}:${it.name}:${it.version}" }
        )
    }
}