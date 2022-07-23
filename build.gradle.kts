plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.40"
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
    kotlin("plugin.serialization") version "1.5.10"
}

taboolib {
    install("common")
    install("common-5")
    install("module-chat")
    install("module-configuration")
    install("platform-bukkit")
    install("expansion-command-helper")
    classifier = null
    version = "6.0.9-31"
    description {
        contributors {
            name("Siooraen")
        }
        dependencies {
            name("Zaphkiel")
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms:Zaphkiel:1.7.2")
    compileOnly("net.sourceforge.jexcelapi:jxl:2.6.12")
    compileOnly("ink.ptms.core:v11802:11802:mapped")
    compileOnly("ink.ptms.core:v11802:11802:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}