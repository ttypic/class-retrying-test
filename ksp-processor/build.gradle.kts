val kspVersion: String by project

plugins {
    kotlin("jvm")
}

group = "com.ttypic.junitpioneer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup:kotlinpoet:1.12.0")
    implementation("com.squareup:kotlinpoet-ksp:1.12.0")
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
    implementation("org.junit-pioneer:junit-pioneer:1.9.1")
    testImplementation(kotlin("test"))
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.4.9")
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

tasks.test {
    useJUnitPlatform()
}
