import java.time.LocalDateTime

suspend fun main() {
    // API key 설정
    // env or raw(CAUTION!)
    val apiKey = System.getenv("ANTHROPIC_API_KEY") ?: throw IllegalStateException("API key not found")
    // val apiKey = "..."

    // generateSummary interface 를 구현하는 llm Client class
    val llmClient = ClaudeClient(apiKey)

    // llc Client class 의 wrapper
    val summarizer = ReviewSummarizer(llmClient)

    val tempID = 1
    val tempName = "모수 성균관대2호점"

    // Create sample reviews
    val reviews = listOf(
        Review(
            restaurantId = tempID,
            restaurantName = tempName,
            content = "주말에 가족들과 방문했는데 분위기도 좋고 특히 파스타가 맛있었어요. 다만 웨이팅이 좀 길었네요.",
            rating = 4.5f,
            tags = listOf("데이트", "파스타", "분위기좋은"),
            createdAt = LocalDateTime.now()
        ),
        Review(
            restaurantId = tempID,
            restaurantName = tempName,
            content = "점심특선 파스타가 가성비가 정말 좋아요! 직장인들한테 좋을 것 같아요. 평소엔 좀 비쌀 것 같네요.",
            rating = 4.0f,
            tags = listOf("점심특선", "혼밥", "가성비"),
            createdAt = LocalDateTime.now()
        ),
        Review(
            restaurantId = tempID,
            restaurantName = tempName,
            content = "맛은 괜찮은데 가격이 좀 있는 편이에요. 서비스는 친절했습니다.",
            rating = 3.5f,
            tags = listOf("데이트", "파스타"),
            createdAt = LocalDateTime.now()
        ),
        Review(
            restaurantId = tempID,
            restaurantName = tempName,
            content = "최악이에요. 다시는 방문 안 함 ^^",
            rating = 1.0f,
            tags = listOf("불친절"),
            createdAt = LocalDateTime.now()
        ),
        Review(
            restaurantId = tempID,
            restaurantName = tempName,
            content = "전반적으로 나쁘지 않았습니다. 굿.",
            rating = 4.0f,
            tags = listOf("파스타"),
            createdAt = LocalDateTime.now()
        ),
        Review(
            restaurantId = tempID,
            restaurantName = tempName,
            content = "여자친구랑 왔는데 맛있었어요. 파스타가 맛있네요.",
            rating = 4.5f,
            tags = listOf("데이트"),
            createdAt = LocalDateTime.now()
        )
    )

    try {
        val summary = summarizer.summarizeReviews(reviews)
        println("요약:")
        println(summary.narrativeSummary)
        println("\n별점 평균: ${summary.overallRating}")
        println("리뷰 갯수: ${summary.reviewCount}")
        println("리뷰 시점: ${summary.timeRange}")
    } catch (e: Exception) {
        println("Error: ${e.message}")
        e.printStackTrace()
    }
}