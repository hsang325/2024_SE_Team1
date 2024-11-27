import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ClaudeMessage(
    val role: String,
    val content: String
)

@Serializable
data class ClaudeRequest(
    val model: String,
    val max_tokens: Int,
    val messages: List<ClaudeMessage>
)

// For ok response
// Claude API docs 참고
@Serializable
data class ClaudeResponse(
    val content: List<ClaudeContent>,
    val id: String,
    val model: String,
    val role: String,
    val stop_reason: String? = null,
    val stop_sequence: String? = null,
    val type: String,
    val usage: ClaudeUsage
)

@Serializable
data class ClaudeContent(
    val text: String,
    val type: String
)

@Serializable
data class ClaudeUsage(
    val input_tokens: Int,
    val output_tokens: Int
)

// For error response
@Serializable
data class ErrorResponse(
    val type: String,
    val error: ErrorDetail
)

@Serializable
data class ErrorDetail(
    val type: String,
    val message: String
)

class ClaudeClient(
    private val apiKey: String,
    private val model: String = "claude-3-5-sonnet-20241022",
    private val maxTokens: Int = 1024
) : LLMClient {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(json)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
        }
    }

    override suspend fun generateSummary(prompt: String): String {
        try {
            val requestBody = ClaudeRequest(
                model = model,
                max_tokens = maxTokens,
                messages = listOf(
                    ClaudeMessage(
                        role = "user",
                        content = prompt
                    )
                )
            )

            val response = httpClient.post("https://api.anthropic.com/v1/messages") {
                headers {
                    append("x-api-key", apiKey)
                    append("anthropic-version", "2023-06-01")
                    append("content-type", "application/json")
                }
                setBody(requestBody)
            }

            return when (response.status) {
                HttpStatusCode.OK -> {
                    val claudeResponse = response.body<ClaudeResponse>()
                    claudeResponse.content.firstOrNull()?.text
                        ?: throw IllegalStateException("No content in response")
                }
                else -> {
                    val errorResponse = response.body<ErrorResponse>()
                    throw ClaudeApiException(
                        errorResponse.error.message,
                        errorResponse.error.type
                    )
                }
            }
        } catch (e: Exception) {
            when (e) {
                is ClaudeApiException -> throw e
                is JsonConvertException -> {
                    println("JSON parsing error: ${e.message}")
                    throw ClaudeApiException("Invalid response format: ${e.message}", "parsing_error")
                }
                is HttpRequestTimeoutException -> throw ClaudeApiException("Request timed out", "timeout_error")
                else -> throw ClaudeApiException("Unknown error: ${e.message}", "unknown_error")
            }
        } finally {
            httpClient.close()
        }
    }
}

class ClaudeApiException(
    message: String,
    val errorType: String
) : Exception(message)