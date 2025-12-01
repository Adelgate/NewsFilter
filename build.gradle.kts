import org.springframework.boot.gradle.plugin.SpringBootPlugin

		plugins {
			id("java")
			id("org.springframework.boot") version "3.3.0"
			id("io.spring.dependency-management") version "1.1.4"
		}

group = "com.newsfilter"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_23

repositories {
	mavenCentral()
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:mongodb")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

// Ensure consistent MapStruct and Lombok processors for test sources
configurations {
	testAnnotationProcessor {
		extendsFrom(annotationProcessor.get())
	}
}

// Optimize build cacheability for Spring Boot
springBoot {
	mainClass.set("com.newsfilter.NewsFilterApplication")
}
