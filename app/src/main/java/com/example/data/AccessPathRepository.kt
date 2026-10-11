package com.example.data

import com.example.model.AccessibilityCategory
import com.example.model.AiSummary
import com.example.model.Place
import com.example.model.PlaceWithStats
import com.example.model.Review
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AccessPathRepository(
    private val geminiService: GeminiAiService = GeminiAiService()
) {
    private val placesState = MutableStateFlow<List<Place>>(SamplePlacesData.initialPlaces)
    private val reviewsState = MutableStateFlow<List<Review>>(SamplePlacesData.initialReviews)
    private val aiSummariesState = MutableStateFlow<Map<String, AiSummary>>(emptyMap())

    val allPlacesWithStats: Flow<List<PlaceWithStats>> =
        placesState.map { places ->
            val reviews = reviewsState.value
            val summaries = aiSummariesState.value
            places.map { place ->
                computeStats(place, reviews.filter { it.placeId == place.id }, summaries[place.id])
            }
        }

    fun getPlaceDetails(placeId: String): Flow<PlaceWithStats?> {
        return allPlacesWithStats.map { list ->
            list.find { it.place.id == placeId }
        }
    }

    suspend fun addReview(
        placeId: String,
        authorName: String,
        authorRole: String,
        stepFreeRating: Int,
        liftsRating: Int,
        washroomsRating: Int,
        seatingRating: Int,
        wayfindingRating: Int,
        comment: String
    ): Review {
        val newReview = Review(
            id = "rev_" + UUID.randomUUID().toString().take(8),
            placeId = placeId,
            authorName = authorName.ifBlank { "Accessibility Contributor" },
            authorRole = authorRole.ifBlank { "Community Visitor" },
            timestamp = "Just now",
            stepFreeRating = stepFreeRating.coerceIn(1, 5),
            liftsRating = liftsRating.coerceIn(1, 5),
            washroomsRating = washroomsRating.coerceIn(1, 5),
            seatingRating = seatingRating.coerceIn(1, 5),
            wayfindingRating = wayfindingRating.coerceIn(1, 5),
            comment = comment.trim(),
            verifiedVisit = true,
            helpfulCount = 0
        )

        reviewsState.value = listOf(newReview) + reviewsState.value

        // Invalidate or update AI summary for this place so it accounts for the new review
        val currentSummaries = aiSummariesState.value.toMutableMap()
        currentSummaries.remove(placeId)
        aiSummariesState.value = currentSummaries

        return newReview
    }

    fun toggleHelpfulReview(reviewId: String) {
        reviewsState.value = reviewsState.value.map { review ->
            if (review.id == reviewId) {
                val wasMarked = review.isHelpfulMarkedByMe
                review.copy(
                    helpfulCount = if (wasMarked) (review.helpfulCount - 1).coerceAtLeast(0) else review.helpfulCount + 1,
                    isHelpfulMarkedByMe = !wasMarked
                )
            } else {
                review
            }
        }
    }

    fun toggleBookmark(placeId: String) {
        placesState.value = placesState.value.map { place ->
            if (place.id == placeId) {
                place.copy(isBookmarked = !place.isBookmarked)
            } else {
                place
            }
        }
    }

    fun addNewPlace(
        name: String,
        category: String,
        address: String,
        neighborhood: String,
        description: String,
        tags: Set<AccessibilityCategory>,
        amenities: List<String>,
        hours: String
    ): Place {
        val newPlace = Place(
            id = "place_" + UUID.randomUUID().toString().take(8),
            name = name.trim(),
            category = category.trim().ifBlank { "Public Facility" },
            address = address.trim(),
            neighborhood = neighborhood.trim().ifBlank { "City Central" },
            description = description.trim(),
            supportedTags = tags,
            amenityDetails = amenities.ifEmpty { listOf("Accessible entrance verified") },
            openingHours = hours.trim().ifBlank { "Standard Public Hours" },
            imageDrawableRes = when {
                category.contains("Transit", ignoreCase = true) -> com.example.R.drawable.img_transit_hub_1791713415968
                category.contains("Museum", ignoreCase = true) || category.contains("Art", ignoreCase = true) -> com.example.R.drawable.img_art_museum_1791713436963
                category.contains("Park", ignoreCase = true) || category.contains("Garden", ignoreCase = true) -> com.example.R.drawable.img_waterfront_park_1791713462668
                else -> com.example.R.drawable.img_public_library_1791713499087
            }
        )

        placesState.value = listOf(newPlace) + placesState.value
        return newPlace
    }

    suspend fun generateOrGetAiSummary(placeId: String): AiSummary {
        val cached = aiSummariesState.value[placeId]
        if (cached != null) return cached

        val place = placesState.value.find { it.id == placeId }
            ?: throw IllegalArgumentException("Place not found: $placeId")
        val reviews = reviewsState.value.filter { it.placeId == placeId }
        val stats = computeStats(place, reviews, null)

        val summary = geminiService.generateCommunitySummary(stats)
        aiSummariesState.value = aiSummariesState.value + (placeId to summary)
        return summary
    }

    private fun computeStats(place: Place, reviews: List<Review>, aiSummary: AiSummary?): PlaceWithStats {
        if (reviews.isEmpty()) {
            return PlaceWithStats(
                place = place,
                reviews = emptyList(),
                overallScore = 4.0f,
                stepFreeScore = 4.0f,
                liftsScore = 4.0f,
                washroomsScore = 4.0f,
                seatingScore = 4.0f,
                wayfindingScore = 4.0f,
                reviewCount = 0,
                aiSummary = aiSummary
            )
        }

        val stepFreeAvg = reviews.map { it.stepFreeRating }.average().toFloat()
        val liftsAvg = reviews.map { it.liftsRating }.average().toFloat()
        val washroomsAvg = reviews.map { it.washroomsRating }.average().toFloat()
        val seatingAvg = reviews.map { it.seatingRating }.average().toFloat()
        val wayfindingAvg = reviews.map { it.wayfindingRating }.average().toFloat()

        val overallAvg = (stepFreeAvg + liftsAvg + washroomsAvg + seatingAvg + wayfindingAvg) / 5.0f

        return PlaceWithStats(
            place = place,
            reviews = reviews,
            overallScore = overallAvg,
            stepFreeScore = stepFreeAvg,
            liftsScore = liftsAvg,
            washroomsScore = washroomsAvg,
            seatingScore = seatingAvg,
            wayfindingScore = wayfindingAvg,
            reviewCount = reviews.size,
            aiSummary = aiSummary
        )
    }
}
