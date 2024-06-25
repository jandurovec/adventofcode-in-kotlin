plugins {
    kotlin("jvm") version "2.0.0"
}

sourceSets {
    main {
        java.srcDirs("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.8"
    }
}

dependencies {
    implementation(kotlin("test"))
}
