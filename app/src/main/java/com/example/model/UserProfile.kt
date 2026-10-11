package com.example.model

/**
 * Data model for User Profile.
 */
data class UserProfile(
    val id: String = "user_default_id",
    val fullName: String = "Alex Morgan",
    val email: String = "alex.morgan@accesspath.org",
    val phone: String = "+1 (415) 890-2451",
    val location: String = "San Francisco, CA",
    val avatarIndex: Int = 0,
    val preferredProfile: AccessibilityProfile = AccessibilityProfile.WHEELCHAIR,
    val bio: String = "Accessibility advocate and urban explorer. Passionate about step-free public transit and tactile wayfinding.",
    val isEmailVerified: Boolean = true,
    val reviewsCount: Int = 8,
    val helpfulVotesCount: Int = 34,
    val savedPlacesCount: Int = 4
)
