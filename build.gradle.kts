plugins {
    id("fabric-loom").version("1.14-SNAPSHOT")
    id("maven-publish")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

val minecraft_version: String by project
val yarn_mappings: String by project
val loader_version: String by project
val fabric_version: String by project
val malilib_id: String by project
val litematica_version: String by project
val mod_version: String by project

base {
    archivesName.set("Litematica-Printer-${minecraft_version}")
}

dependencies {
    minecraft("com.mojang:minecraft:${minecraft_version}")
    mappings("net.fabricmc:yarn:${yarn_mappings}:v2")

    modImplementation("net.fabricmc:fabric-loader:${loader_version}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabric_version}")

    modImplementation("maven.modrinth:malilib:${malilib_id}")
    modImplementation("maven.modrinth:litematica:${litematica_version}")
}

repositories {
    maven("https://maven.fabricmc.net/")
    maven("https://masa.dy.fi/maven")
    maven("https://www.cursemaven.com")
    maven("https://jitpack.io")
    maven("https://api.modrinth.com/maven")
}

tasks.withType<ProcessResources> {
    inputs.property("version", mod_version)

    filesMatching("fabric.mod.json") {
        expand(mapOf("version" to mod_version))
    }
}
