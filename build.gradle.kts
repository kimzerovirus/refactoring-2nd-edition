plugins {
    kotlin("jvm") version "1.9.22"
}

group = "me.kzv"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // spring이면 object mapper가 기본적으로 있겠지만 kotlin 은 요런 라이브러리도 있음
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-serialization-json
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}