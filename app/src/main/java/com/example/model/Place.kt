package com.example.model

import com.example.R

data class Place(
    val id: String,
    val name: String,
    val category: String, // e.g. "Transit Hub", "Museum", "Public Library", "City Park", "Healthcare", "Civic Center"
    val address: String,
    val neighborhood: String,
    val description: String,
    val supportedTags: Set<AccessibilityCategory>,
    val amenityDetails: List<String>,
    val openingHours: String,
    val imageDrawableRes: Int = 0,
    val galleryImages: List<Int> = emptyList(),
    val isBookmarked: Boolean = false,
    val mapX: Float = 0.5f, // Normalized 0.0f..1.0f on stylized interactive city canvas
    val mapY: Float = 0.5f,
    val lat: Double = 37.7749,
    val lng: Double = -122.4194
)

enum class AccessibilityProfile(
    val id: String,
    val title: String,
    val shortName: String,
    val description: String
) {
    GENERAL("general", "General View", "General", "Standard overall accessibility score"),
    WHEELCHAIR("wheelchair", "Wheelchair User", "Wheelchair", "Weighted on step-free ramps & elevators"),
    WALKER_CANE("walker_cane", "Senior / Walker / Cane", "Walker/Cane", "Weighted on rest seating & low steps"),
    LOW_VISION("low_vision", "Low Vision / Blind", "Vision", "Weighted on tactile paving & audio guidance"),
    SENSORY("sensory", "Sensory / Neurodivergent", "Sensory", "Weighted on quiet spaces & wayfinding"),
    STROLLER("stroller", "Parent with Stroller", "Stroller", "Weighted on wide doors & elevators")
}

data class PlaceWithStats(
    val place: Place,
    val reviews: List<Review>,
    val overallScore: Float,
    val stepFreeScore: Float,
    val liftsScore: Float,
    val washroomsScore: Float,
    val seatingScore: Float,
    val wayfindingScore: Float,
    val reviewCount: Int,
    val aiSummary: AiSummary? = null
) {
    val scoreBadgeLabel: String
        get() = when {
            overallScore >= 4.6f -> "Exceptional Access"
            overallScore >= 4.0f -> "Highly Accessible"
            overallScore >= 3.2f -> "Moderate Access"
            overallScore >= 2.0f -> "Barriers Present"
            else -> "Needs Improvement"
        }

    val scoreLevel: ScoreLevel
        get() = when {
            overallScore >= 4.5f -> ScoreLevel.EXCELLENT
            overallScore >= 3.8f -> ScoreLevel.GOOD
            overallScore >= 2.8f -> ScoreLevel.MODERATE
            else -> ScoreLevel.CHALLENGING
        }

    fun categoryScore(category: AccessibilityCategory): Float {
        return when (category) {
            AccessibilityCategory.STEP_FREE -> stepFreeScore
            AccessibilityCategory.LIFTS -> liftsScore
            AccessibilityCategory.ACCESSIBLE_WASHROOMS -> washroomsScore
            AccessibilityCategory.SEATING -> seatingScore
            AccessibilityCategory.WAYFINDING -> wayfindingScore
        }
    }

    fun personalizedScore(profile: AccessibilityProfile): Float {
        return when (profile) {
            AccessibilityProfile.GENERAL -> overallScore
            AccessibilityProfile.WHEELCHAIR -> (stepFreeScore * 0.45f + liftsScore * 0.35f + washroomsScore * 0.20f)
            AccessibilityProfile.WALKER_CANE -> (seatingScore * 0.40f + stepFreeScore * 0.35f + liftsScore * 0.25f)
            AccessibilityProfile.LOW_VISION -> (wayfindingScore * 0.50f + stepFreeScore * 0.30f + seatingScore * 0.20f)
            AccessibilityProfile.SENSORY -> (seatingScore * 0.40f + wayfindingScore * 0.40f + washroomsScore * 0.20f)
            AccessibilityProfile.STROLLER -> (stepFreeScore * 0.45f + liftsScore * 0.35f + washroomsScore * 0.20f)
        }
    }

    fun personalizedScoreLevel(profile: AccessibilityProfile): ScoreLevel {
        val score = personalizedScore(profile)
        return when {
            score >= 4.5f -> ScoreLevel.EXCELLENT
            score >= 3.8f -> ScoreLevel.GOOD
            score >= 2.8f -> ScoreLevel.MODERATE
            else -> ScoreLevel.CHALLENGING
        }
    }

    fun personalizedBadgeLabel(profile: AccessibilityProfile): String {
        val score = personalizedScore(profile)
        return when {
            score >= 4.6f -> "Exceptional Fit"
            score >= 4.0f -> "Strongly Accessible"
            score >= 3.2f -> "Moderate Barriers"
            else -> "Significant Obstacles"
        }
    }

    fun personalizedReasonLine(profile: AccessibilityProfile): String {
        return when (profile) {
            AccessibilityProfile.GENERAL -> {
                if (overallScore >= 4.5f) "Consistently praised for full physical and sensory accessibility standards."
                else if (overallScore >= 3.8f) "Dependable step-free transit across primary zones with minor caveats."
                else "Community reviews note partial barriers or older historic infrastructure."
            }
            AccessibilityProfile.WHEELCHAIR -> {
                if (stepFreeScore >= 4.5f && liftsScore >= 4.0f)
                    "Level barrier-free entrances, spacious lifts & compliant 110cm door clearances."
                else if (stepFreeScore < 3.8f)
                    "Specific entrance steps reported; use alternate ramp gate to avoid thresholds."
                else
                    "Ramps and lifts available; elevator call times can vary during peak hours."
            }
            AccessibilityProfile.WALKER_CANE -> {
                if (seatingScore >= 4.2f)
                    "Abundant ergonomic resting benches every 50 meters and low-incline walkways."
                else
                    "Long continuous corridors between seating; plan short pause intervals."
            }
            AccessibilityProfile.LOW_VISION -> {
                if (wayfindingScore >= 4.2f)
                    "Continuous tactile ground guidance strips, audible announcements & Braille plaques."
                else
                    "Signage lighting is moderate; front desk offers assistance for first-time orientation."
            }
            AccessibilityProfile.SENSORY -> {
                if (wayfindingScore >= 4.0f && seatingScore >= 4.0f)
                    "Clear logical spatial layout with dedicated low-sensory rest zones."
                else
                    "Acoustics can be reverberant during peak visiting periods."
            }
            AccessibilityProfile.STROLLER -> {
                if (stepFreeScore >= 4.2f && liftsScore >= 4.0f)
                    "Wide double automatic doors, spacious family restrooms & level elevators."
                else
                    "Some narrow turnstiles; staff gate is accessible for wide twin strollers."
            }
        }
    }
}

enum class ScoreLevel {
    EXCELLENT,
    GOOD,
    MODERATE,
    CHALLENGING
}

fun Place.resolveImageRes(): Int {
    if (imageDrawableRes != 0) return imageDrawableRes
    return when {
        category.contains("Transit", ignoreCase = true) || category.contains("Terminal", ignoreCase = true) || category.contains("Station", ignoreCase = true) ->
            R.drawable.img_transit_hub_1791713415968
        category.contains("Museum", ignoreCase = true) || category.contains("Art", ignoreCase = true) || category.contains("Gallery", ignoreCase = true) ->
            R.drawable.img_art_museum_1791713436963
        category.contains("Park", ignoreCase = true) || category.contains("Garden", ignoreCase = true) || category.contains("Waterfront", ignoreCase = true) || category.contains("Shoreline", ignoreCase = true) ->
            R.drawable.img_waterfront_park_1791713462668
        category.contains("Library", ignoreCase = true) || category.contains("Science", ignoreCase = true) || category.contains("Planetarium", ignoreCase = true) ->
            R.drawable.img_public_library_1791713499087
        category.contains("Market", ignoreCase = true) || category.contains("Arcade", ignoreCase = true) || category.contains("Civic", ignoreCase = true) || category.contains("Hall", ignoreCase = true) ->
            R.drawable.img_historic_arcade_1791716947902
        category.contains("Health", ignoreCase = true) || category.contains("Medical", ignoreCase = true) || category.contains("Hospital", ignoreCase = true) || category.contains("Campus", ignoreCase = true) ->
            R.drawable.img_hospital_pavilion_1791716977552
        category.contains("Symphony", ignoreCase = true) || category.contains("Cinema", ignoreCase = true) || category.contains("Theater", ignoreCase = true) || category.contains("Concert", ignoreCase = true) ->
            R.drawable.img_symphony_hall_1791717005320
        category.contains("Sport", ignoreCase = true) || category.contains("Recreation", ignoreCase = true) || category.contains("Gym", ignoreCase = true) || category.contains("Pool", ignoreCase = true) ->
            R.drawable.img_sports_complex_1791717038248
        category.contains("Botanical", ignoreCase = true) || category.contains("Conservatory", ignoreCase = true) ->
            R.drawable.img_botanical_garden_1791719288905
        category.contains("Ferry", ignoreCase = true) || category.contains("Pier", ignoreCase = true) ->
            R.drawable.img_ferry_pier_1791719313054
        else -> R.drawable.img_historic_arcade_1791716947902
    }
}

fun Place.resolveImagesList(): List<Int> {
    if (galleryImages.size >= 3) return galleryImages.take(3)
    val primary = resolveImageRes()
    val second = when (primary) {
        R.drawable.img_transit_hub_1791713415968 -> R.drawable.img_historic_arcade_1791716947902
        R.drawable.img_art_museum_1791713436963 -> R.drawable.img_symphony_hall_1791717005320
        R.drawable.img_waterfront_park_1791713462668 -> R.drawable.img_ferry_pier_1791719313054
        R.drawable.img_public_library_1791713499087 -> R.drawable.img_art_museum_1791713436963
        R.drawable.img_historic_arcade_1791716947902 -> R.drawable.img_public_library_1791713499087
        R.drawable.img_hospital_pavilion_1791716977552 -> R.drawable.img_botanical_garden_1791719288905
        R.drawable.img_symphony_hall_1791717005320 -> R.drawable.img_art_museum_1791713436963
        R.drawable.img_sports_complex_1791717038248 -> R.drawable.img_waterfront_park_1791713462668
        R.drawable.img_botanical_garden_1791719288905 -> R.drawable.img_waterfront_park_1791713462668
        R.drawable.img_ferry_pier_1791719313054 -> R.drawable.img_transit_hub_1791713415968
        else -> R.drawable.img_botanical_garden_1791719288905
    }
    val third = when (primary) {
        R.drawable.img_transit_hub_1791713415968 -> R.drawable.img_ferry_pier_1791719313054
        R.drawable.img_art_museum_1791713436963 -> R.drawable.img_historic_arcade_1791716947902
        R.drawable.img_waterfront_park_1791713462668 -> R.drawable.img_botanical_garden_1791719288905
        R.drawable.img_public_library_1791713499087 -> R.drawable.img_symphony_hall_1791717005320
        R.drawable.img_historic_arcade_1791716947902 -> R.drawable.img_ferry_pier_1791719313054
        R.drawable.img_hospital_pavilion_1791716977552 -> R.drawable.img_sports_complex_1791717038248
        R.drawable.img_symphony_hall_1791717005320 -> R.drawable.img_historic_arcade_1791716947902
        R.drawable.img_sports_complex_1791717038248 -> R.drawable.img_hospital_pavilion_1791716977552
        R.drawable.img_botanical_garden_1791719288905 -> R.drawable.img_public_library_1791713499087
        R.drawable.img_ferry_pier_1791719313054 -> R.drawable.img_waterfront_park_1791713462668
        else -> R.drawable.img_historic_arcade_1791716947902
    }
    return listOf(primary, second, third)
}
