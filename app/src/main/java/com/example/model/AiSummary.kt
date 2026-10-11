package com.example.model

data class AiSummary(
    val placeId: String,
    val headline: String,
    val overallVerdict: String,
    val strengths: List<String>,
    val cautions: List<String>,
    val practicalTip: String,
    val generatedDate: String,
    val reviewCountAnalyzed: Int,
    val confidenceBadge: String = "High Confidence"
)
