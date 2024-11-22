plugins {
    id("java")
    id("war")
    id("org.gretty") version "3.0.6"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("javax.servlet:javax.servlet-api:4.0.1") // 서블릿 API 의존성
    implementation("com.google.code.gson:gson:2.8.8") // 예시로 추가된 의존성
    testImplementation("junit:junit:4.13.2")
}

gretty {
    contextPath = "/"
    servletContainer = "tomcat9"
}

tasks.war {
    from("src/main/webapp") {
        include("**/*.jsp")
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
