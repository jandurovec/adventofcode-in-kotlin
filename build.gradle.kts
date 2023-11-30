plugins {
    kotlin("jvm") version "1.9.21"
}

sourceSets {
    main {
        java.srcDirs("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}

dependencies {
    implementation(kotlin("test"))
}

kotlin {
  jvmToolchain(19)
}
