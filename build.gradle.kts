import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.3.0.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
	jacoco
	id("com.google.cloud.tools.jib") version "1.8.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
	maven(url = "https://maven.vaadin.com/vaadin-addons")
}

extra["vaadinVersion"] = "14.2.0"

dependencies {
	implementation("com.vaadin:vaadin-spring-boot-starter") {
		exclude("org.hibernate.validator", "hibernate-validator")
	}
	implementation("com.vaadin:vaadin-core")
	implementation("com.vaadin:vaadin-board-flow")
	implementation("com.vaadin:vaadin-charts-flow")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.apache.commons:commons-lang3:3.9")
	implementation("org.apache.commons:commons-csv:1.8")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-webflux") {
		exclude("org.hibernate.validator", "hibernate-validator")
	}
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.squareup.okhttp3:okhttp:4.0.0")
	implementation("com.github.mvysny.karibudsl:karibu-dsl:1.0.1")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testImplementation("com.squareup.okhttp3:mockwebserver:4.0.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("io.mockk:mockk:1.9.3")
	testImplementation("com.github.tomakehurst:wiremock-jre8:2.25.1")
}

dependencyManagement {
	imports {
		mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy("jacocoTestCoverageVerification")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.jacocoTestCoverageVerification {
	classDirectories.setFrom(
			sourceSets.main.get().output.asFileTree.matching {
				exclude("io/web/covid19tracker/Covid19TrackerApplicationKt.class")
			}
	)
	violationRules {
		rule {
			limit {
				minimum = "0.01".toBigDecimal()
			}
		}
	}
}