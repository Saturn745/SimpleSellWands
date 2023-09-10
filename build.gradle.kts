import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    kotlin("jvm") version "1.9.0"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    application
}

group = "xyz.galaxyy.mclua"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.purpurmc.org/snapshots")
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    library(kotlin("stdlib"))
    compileOnly("org.purpurmc.purpur:purpur-api:1.20.1-R0.1-SNAPSHOT")
    library("com.github.only52607.luakt:luakt:2.6.1")
    library("com.github.only52607.luakt:luakt-core:2.6.1")
    library("com.github.only52607.luakt:luakt-extension:2.6.1")
    library("com.github.only52607.luakt:luakt-luaj:2.6.1")
}

paper {
    // Plugin main class (required)
    loader = "xyz.galaxyy.mclua.PluginLibrariesLoader"
    main = "xyz.galaxyy.mclua.MCLua"
    name = "MCLua"
    authors = listOf("Element4521")
    description = "A plugin that allows you to run Lua scripts in Minecraft."
    apiVersion = "1.20"

    load = BukkitPluginDescription.PluginLoadOrder.STARTUP

    // Generate paper-libraries.json from `library` and `paperLibrary` in `dependencies`
    generateLibrariesJson = true
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}