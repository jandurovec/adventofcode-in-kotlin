plugins {
    kotlin("jvm") version "2.2.21"
}

sourceSets {
    main {
        java.srcDirs("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "9.2.1"
    }
}

dependencies {
    implementation(kotlin("test"))
}
