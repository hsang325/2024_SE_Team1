import java.time.LocalDateTime

data class Review(
    val restaurantId: Int,
    val restaurantName: String,
    val content: String,
    val rating: Float,
    val tags: List<String>,
    val createdAt: LocalDateTime
)

data class ReviewSummary(
    val restaurantId: Int,
    val restaurantName: String,
    val overallRating: Float,
    val narrativeSummary: String, // This is the summary
    val reviewCount: Int,
    val timeRange: String
)

interface LLMClient {
    suspend fun generateSummary(prompt: String): String
}

class ReviewSummarizer(
    private val llmClient: LLMClient,
    // private val language: String = "ko"
) {
    suspend fun summarizeReviews(reviews: List<Review>): ReviewSummary {
        require(reviews.isNotEmpty()) { "Reviews list cannot be empty" }

        val restaurantId = reviews.first().restaurantId
        val restaurantName = reviews.first().restaurantName
        val avgRating = reviews.map { it.rating }.average().toFloat()

        // see below for implementations
        val dateRange = getDateRange(reviews)
        val prompt = buildNarrativePrompt(reviews)
        val narrativeSummary = llmClient.generateSummary(prompt)

        return ReviewSummary(
            restaurantId = restaurantId,
            restaurantName = restaurantName,
            overallRating = avgRating,
            narrativeSummary = narrativeSummary,
            reviewCount = reviews.size,
            timeRange = dateRange
        )
    }

    private fun buildNarrativePrompt(reviews: List<Review>): String {
        return buildString {
            appendLine("""
                다음 ${reviews.size}개의 ${reviews.first().restaurantName} 리뷰들을 자연스러운 문장으로 요약해주세요.
                
                요약할 때 다음 사항들을 고려해주세요:
                - 여러 사람들의 공통된 의견과 상반된 의견을 자연스럽게 포함해주세요
                - "대다수의 손님들은...", "일부 손님들은...", "특히 주말에는..." 처럼 자연스러운 표현을 사용해주세요
                - 메뉴, 가격, 분위기, 서비스 등 다양한 측면을 언급해주세요
                - 숫자나 통계보다는 서술적인 표현을 사용해주세요
                - 실제 손님들의 경험담처럼 읽히도록 해주세요
                
                리뷰들:
            """.trimIndent())

            val reviewsByRating = reviews.groupBy {
                when {
                    it.rating >= 4.5 -> "매우 긍정"
                    it.rating >= 4.0 -> "긍정"
                    it.rating >= 3.0 -> "중립"
                    else -> "부정"
                }
            }

            reviewsByRating.forEach { (sentiment, reviewsInGroup) ->
                appendLine("\n$sentiment"+"적인 리뷰들:")
                reviewsInGroup.forEach { review ->
                    appendLine("---")
                    appendLine("평점: ${review.rating}")
                    appendLine("태그: ${review.tags.joinToString()}")
                    appendLine("리뷰: ${review.content}")
                }
            }

            appendLine("""
                
                위 리뷰들을 바탕으로 자연스러운 문장으로 요약해주세요. 예시:
                "이 식당은 대체로 맛있다는 평가가 많습니다. 특히 주말 저녁에 방문한 손님들은 분위기가 좋다고 언급했지만, 
                일부 손님들은 웨이팅이 길다는 점을 아쉬워했습니다. 점심시간에 방문한 손님들은 점심 특선 메뉴가 
                가성비가 좋다고 평가했으며..."
            """.trimIndent())
        }
    }

    private fun getDateRange(reviews: List<Review>): String {
        val earliest = reviews.minOf { it.createdAt }
        val latest = reviews.maxOf { it.createdAt }
        return "${earliest.year}년 ${earliest.monthValue}월부터 ${latest.year}년 ${latest.monthValue}월까지의 리뷰 기준"
    }
}