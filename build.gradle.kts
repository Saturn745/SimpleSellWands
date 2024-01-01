plugins {
    id("java")
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

group = "xyz.galaxyy.SimpleSellWand"

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.purpurmc.org/snapshots")
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
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

tasks.test { useJUnitPlatform() }
