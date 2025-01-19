plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
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
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3") // 이 버전 보다 높으면 코틀린 2.0 버전 이상 써야 되는듯 - 나중에 코틀린 컴파일러 버전 올려야지
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}