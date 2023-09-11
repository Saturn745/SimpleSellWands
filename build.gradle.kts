import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("com.modrinth.minotaur") version "2.8.2"
    application
}
val buildNum = System.getenv("GITHUB_RUN_NUMBER") ?: "SNAPSHOT"
group = "xyz.galaxyy.lualink"
version = "1.20.1-$buildNum"

repositories {
    mavenCentral()
    maven("https://repo.purpurmc.org/snapshots")
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

val cloudVersion: String by project

dependencies {
    testImplementation(kotlin("test"))
    library(kotlin("stdlib"))
    compileOnly("org.purpurmc.purpur:purpur-api:1.20.1-R0.1-SNAPSHOT")
    library("com.github.only52607.luakt:luakt:2.6.1")
    library("com.github.only52607.luakt:luakt-core:2.6.1")
    library("com.github.only52607.luakt:luakt-extension:2.6.1")
    library("com.github.only52607.luakt:luakt-luaj:2.6.1")
    library("cloud.commandframework:cloud-paper:$cloudVersion")
    library("cloud.commandframework:cloud-brigadier:$cloudVersion")
    library("cloud.commandframework:cloud-annotations:$cloudVersion")
}

paper {
    loader = "xyz.galaxyy.lualink.PluginLibrariesLoader"
    main = "xyz.galaxyy.lualink.LuaLink"
    name = "LuaLink"
    authors = listOf("Element4521")
    description = "A plugin that allows you to run Lua scripts in Minecraft."
    apiVersion = "1.20"

    load = BukkitPluginDescription.PluginLoadOrder.STARTUP

    generateLibrariesJson = true
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("lualink")
    versionNumber.set(version.toString())
    versionType.set("beta")
    uploadFile.set(tasks.jar.get())
    gameVersions.addAll("1.20.1")
    loaders.addAll("paper", "purpur")
    changelog.set(System.getenv("GIT_COMMIT_MESSAGE"))
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