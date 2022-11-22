plugins {
    id("com.google.devtools.ksp")
    kotlin("jvm")
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":ksp-processor"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.junit-pioneer:junit-pioneer:1.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    ksp(project(":ksp-processor"))
}

tasks.test {
    useJUnitPlatform()
}
