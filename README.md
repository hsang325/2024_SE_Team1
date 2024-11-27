# LLM Review Summarizer(WIP - w/o lambda)

## Overview
This branch implements a restaurant review summarization system using Claude LLM API.

## Developer
Yil Jang

## Core Components

### Testing Code (`Main.kt`)
- Test code for things below

### Claude API Wrapper (`Claude.kt`)
- REST API wrapper for Claude LLM
- Handles API communication and response parsing

### Review Summarizer (`Summarizer.kt`)
- Wrapper for review summarization logic
- Input: `List<Review>`
- Output: `ReviewSummary` 

## Dependencies
Add the following to your `build.gradle.kts`:
```kotlin
dependencies {
    // Logging
    implementation("org.slf4j:slf4j-api:1.6.1")
    implementation("org.slf4j:slf4j-simple:1.6.1")
    
    // Ktor
    val ktorVersion = "2.3.7"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    
    // JSON Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    
    // Testing
    testImplementation(kotlin("test"))
}
```


## Database Schema and API Endpoints

For more details on the database schema and API endpoints, please refer to the following Google Docs link:

[Database Schema and API Endpoints Documentation](https://docs.google.com/document/d/1aXXgo7c4Y81xk8ZX0QBeSDF9nEpRvSenxREM266sSAM/edit?tab=t.0)


## Software Requirements Specification (SRS)

For more details on the Software Requirements Specification, please refer to the following Google Docs link:

[Software Requirements Specification Documentation](https://docs.google.com/document/d/13t9zPQ4jw4ti35iKwZmpUgVFYSPINhDv/edit?rtpof=true&tab=t.0)


개인 브랜치 만들어서 개발후 develop 브랜치에 올리기
