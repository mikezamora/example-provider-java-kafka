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

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group= "org.junit.vintage",module= "junit-vintage-engine")
	}
	implementation("com.github.javafaker:javafaker:1.0.2")

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.kafka:spring-kafka")
	testImplementation("au.com.dius.pact.provider:junit5:4.1.34")
	testImplementation("au.com.dius.pact.provider:junit5spring:4.1.34")
//	testImplementation("au.com.dius:pact-jvm-provider-spring:4.0.10")

	runtimeOnly("com.h2database:h2")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
}

tasks {
	test {
		useJUnitPlatform()
//		systemProperty("pactbroker.host", "mzamorahappymoney.pactflow.io")
		if (environment["pactPublishResults"] == "true" || true) {
			systemProperty("pact.provider.version", getGitHash())
			systemProperty("pact.provider.tag", "${getGitBranch()}, test, prod" )
			systemProperty("pact.verifier.publishResults", "true")
		}
	}

//	val copyPacts by creating(Copy::class) {
//		description = "Copies the generated Pact json file to the provider resources directory"
//
//		from("/Users/mzamora/projects/pactio/example-consumer-java-kafka/build/pacts")
//		into("../provider/src/test/resources/pacts/")
//	}
}


fun getGitHash(): String {
	val process = Runtime.getRuntime().exec("git rev-parse --short HEAD")
	val sb: StringBuilder = StringBuilder()
	while (true) {
		val char = process.inputStream.read()
		if (char == -1) break
		sb.append(char.toChar())
	}
	return sb.toString().trim()
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


//pact {
//	serviceProviders {
//		create("pactflow-example-provider-java-kafka") {
//			providerVersion = getGitHash()
//			hasPactsFromPactBroker(
//				mapOf("authentication" to listOf("Bearer", "wGb_AxvRRLCkrXgO_WrbWQ")),
//				"https://mzamorahappymoney.pactflow.io")
//		}
//	}
//	publish {
//		pactBrokerUrl = "https://mzamorahappymoney.pactflow.io"
//		pactBrokerToken = "wGb_AxvRRLCkrXgO_WrbWQ"
//		tags = listOf(getGitBranch(), "test", "prod")
//	}
//
//	broker {
//		pactBrokerUrl = "https://mzamorahappymoney.pactflow.io"
//		pactBrokerToken = "wGb_AxvRRLCkrXgO_WrbWQ"
//	}
//}

