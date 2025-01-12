import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    id("org.jlleitschuh.gradle.ktlint")
}

application {
    mainClass.set("archive.AppKt") // Required by shadowJar
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "16"
    }
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            showCauses = true
            showExceptions = true
            events("passed", "failed")
        }
    }
}

dependencies {
    implementation(project(":libs:hops-common-fhir"))
    implementation(project(":libs:hops-common-ktor"))
    implementation(project(":libs:hops-common-kafka"))
    implementation("io.ktor:ktor-metrics-micrometer:1.6.5")
    implementation("com.ibm.mq:com.ibm.mq.allclient:9.2.3.0")
    implementation("io.ktor:ktor-webjars:1.6.3")
    implementation("io.ktor:ktor-server-netty:1.6.3")
    implementation("io.micrometer:micrometer-registry-prometheus:1.8.0")
    implementation("org.apache.kafka:kafka-clients:2.8.1")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.7")
    runtimeOnly("net.logstash.logback:logstash-logback-encoder:6.6")
    runtimeOnly("org.webjars:swagger-ui:4.1.0")
    testImplementation(project(":libs:hops-common-test"))
    testImplementation(project(":libs:hops-common-kafka-test"))
    testImplementation("org.testcontainers:junit-jupiter:1.16.2")
}

kotlin.sourceSets["main"].kotlin.srcDirs("main")
kotlin.sourceSets["test"].kotlin.srcDirs("test")
sourceSets["main"].resources.srcDir(".config")
sourceSets["test"].resources.srcDir("test/resources")
