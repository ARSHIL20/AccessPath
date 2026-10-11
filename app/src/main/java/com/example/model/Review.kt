package com.example.model

data class Review(
    val id: String,
    val placeId: String,
    val authorName: String,
    val authorRole: String, // e.g. "Power Wheelchair User", "Senior (Walker)", "Parent with Stroller", "Guide Dog Handler"
    val timestamp: String,
    val stepFreeRating: Int, // 1 to 5
    val liftsRating: Int,
    val washroomsRating: Int,
    val seatingRating: Int,
    val wayfindingRating: Int,
    val comment: String,
    val verifiedVisit: Boolean = true,
    val helpfulCount: Int = 0,
    val isHelpfulMarkedByMe: Boolean = false
) {
    val overallRating: Float
        get() = (stepFreeRating + liftsRating + washroomsRating + seatingRating + wayfindingRating) / 5.0f

    fun ratingFor(category: AccessibilityCategory): Int {
        return when (category) {
            AccessibilityCategory.STEP_FREE -> stepFreeRating
            AccessibilityCategory.LIFTS -> liftsRating
            AccessibilityCategory.ACCESSIBLE_WASHROOMS -> washroomsRating
            AccessibilityCategory.SEATING -> seatingRating
            AccessibilityCategory.WAYFINDING -> wayfindingRating
        }
    }
}
