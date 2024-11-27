# LLM-summarizer branch
## Yil Jang
### Implementation without Lambda 
Branch for LLM review summarizer


Claude.kt -> REST API wrapper
Summarizer.kt -> Review Summraizer wrapper

List[Reviews] -> summarizer.summarizeReviews -> ReviewSummary

in build.gradle.kts

dependencies {
    implementation("org.slf4j:slf4j-api:1.6.1")
    implementation("org.slf4j:slf4j-simple:1.6.1")

    val ktorVersion = "2.3.7"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    testImplementation(kotlin("test"))
}


## Database Schema and API Endpoints

For more details on the database schema and API endpoints, please refer to the following Google Docs link:

[Database Schema and API Endpoints Documentation](https://docs.google.com/document/d/1aXXgo7c4Y81xk8ZX0QBeSDF9nEpRvSenxREM266sSAM/edit?tab=t.0)


## Software Requirements Specification (SRS)

For more details on the Software Requirements Specification, please refer to the following Google Docs link:

[Software Requirements Specification Documentation](https://docs.google.com/document/d/13t9zPQ4jw4ti35iKwZmpUgVFYSPINhDv/edit?rtpof=true&tab=t.0)


개인 브랜치 만들어서 개발후 develop 브랜치에 올리기
