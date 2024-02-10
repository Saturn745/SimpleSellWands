plugins {
    id("java")
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("com.modrinth.minotaur") version "2.8.2"
}

val buildNum = System.getenv("GITHUB_RUN_NUMBER") ?: "SNAPSHOT"

group = "xyz.galaxyy.SimpleSellWand"
version = "1.20.2-$buildNum"

repositories {
    mavenCentral()
    maven("https://repo.purpurmc.org/snapshots")
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("org.purpurmc.purpur:purpur-api:1.20.2-R0.1-SNAPSHOT")
    paperLibrary("space.arim.dazzleconf:dazzleconf-ext-hocon:1.2.1")
    // Shops
    compileOnly(
            "com.github.Gypopo:EconomyShopGUI-API:1.7.0"
    ) // EconomyShopGUI and EconomyShopGUI-Premium

    // Economys
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
}

paper {
    main = "xyz.galaxyy.simplesellwand.SimpleSellWand"
    loader = "xyz.galaxyy.simplesellwand.PluginLibrariesLoader"
    author = "Saturn745"
    apiVersion = "1.20"
    description = "A simple sell wand plugin"
    generateLibrariesJson = true
    serverDependencies {
        register("Vault") { required = false }
        register("EconomyShopGUI") { required = false }
        register("EconomyShopGUI-Premium") { required = false }
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("simplesellwands")
    versionNumber.set(version.toString())
    versionType.set("beta")
    uploadFile.set(tasks.jar.get())
    gameVersions.addAll("1.20.1", "1.20.2")
    loaders.addAll("paper", "purpur")
    changelog.set(System.getenv("GIT_COMMIT_MESSAGE"))
}

tasks.test { useJUnitPlatform() }
