plugins {
    kotlin("jvm") version "1.9.21"
}

repositories {
    mavenCentral()
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "8.4"
    }
}

dependencies {
    implementation(kotlin("test"))
}

kotlin {
  jvmToolchain(19)
}
