val sarVersion: String by properties

val minecraftVersion: String by properties
val yarnVersion: String by properties
val fabricLoaderVersion: String by properties
val javaVersion: String by properties

val fabricApiVersion: String by properties
val divineInterventionVersion: String by properties

plugins {
    id("fabric-loom") version "0.12-SNAPSHOT"
    `maven-publish`
}

group = "net.immortaldevs"
version = sarVersion

repositories {
    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")
    }
}

sourceSets {
    create("testmod") {
        compileClasspath += main.get().compileClasspath
        runtimeClasspath += main.get().runtimeClasspath
    }
}

dependencies {
    minecraft(
        group = "com.mojang",
        name = "minecraft",
        version = minecraftVersion,
    )
    
    mappings(
        group = "net.fabricmc",
        name = "yarn",
        version = yarnVersion,
        classifier = "v2",
    )
    
    modImplementation(
        group = "net.fabricmc",
        name = "fabric-loader",
        version = fabricLoaderVersion,
    )

    modImplementation(
        group = "net.fabricmc.fabric-api",
        name = "fabric-api",
        version = fabricApiVersion,
    )
    
    modImplementation(
            group = "com.github.devs-immortal",
            name = "Divine-Intervention",
            version = divineInterventionVersion,
    ).also(::annotationProcessor)

    "testmodImplementation"(sourceSets.main.get().output)
}

tasks {
    processResources {
        inputs.property("version", sarVersion)
        
        filesMatching("fabric.mod.json") {
            expand("version" to sarVersion)
        }
    }

    test {
        dependsOn("runGametest")
    }
}

java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    withSourcesJar()
}

@Suppress("UnstableApiUsage")
loom {
    splitEnvironmentSourceSets()

    mods {
        create("sar") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets["client"])
        }

        register("sar_test") {
            sourceSet(sourceSets["testmod"])
        }
    }

    runs {
        create("gametest") {
            server()
            configName = "Gametest"
            vmArgs.add("-Dfabric-api.gametest")
            runDir = "build/gametest"
            setSource(sourceSets["testmod"])

        }
    }

    accessWidenerPath.set(file("src/main/resources/sar.accesswidener"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
