import au.com.dius.pact.provider.gradle.PactBrokerConsumerConfig.Companion.latestTags

plugins {
	id("org.springframework.boot") version ("2.5.3")
	id("io.spring.dependency-management") version ("1.0.11.RELEASE")
	java
	id("au.com.dius.pact") version "4.3.2"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java {
	sourceCompatibility = JavaVersion.VERSION_11
}

repositories {
	mavenCentral()
}

//configurations {
//  compileOnly {
//    extendsFrom annotationProcessor
//  }
//}

dependencies {
	testImplementation("au.com.dius:pact-jvm-provider-junit5:4.0.10")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group= "org.junit.vintage",module= "junit-vintage-engine")
	}
	implementation("com.github.javafaker:javafaker:1.0.2")

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.kafka:spring-kafka")
	testImplementation("au.com.dius.pact.provider:junit5spring:4.3.2")
	testImplementation("au.com.dius:pact-jvm-provider-spring:4.0.10")

	runtimeOnly("com.h2database:h2")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
}

tasks {
	test {
		useJUnitPlatform()
	}
}

fun getGitBranch(): String {
	val process = Runtime.getRuntime().exec("git rev-parse --abbrev-ref HEAD")
	val sb: StringBuilder = StringBuilder()
	while (true) {
		val char = process.inputStream.read()
		if (char == -1) break
		sb.append(char.toChar())
	}
	return sb.toString().trim()
}

pact {

    broker {
        pactBrokerUrl = System.getenv("PACT_BROKER_BASE_URL")
        pactBrokerToken = System.getenv("PACT_BROKER_TOKEN")
    }
	/** Configs to publish to Pactbroker **/
	publish {
		pactDirectory = "build/pacts"
		tags = listOf(getGitBranch(), "test", "prod")
		consumerVersion = "0.0.2-SNAPSHOT"
	}

}