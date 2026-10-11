package com.example.data

import com.example.model.AccessibilityProfile
import com.example.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserProfileRepository {

    private val _userProfile = MutableStateFlow(
        UserProfile(
            id = "user_primary_auth",
            fullName = "Alex Morgan",
            email = "alex.morgan@accesspath.org",
            phone = "+1 (415) 890-2451",
            location = "San Francisco, CA",
            avatarIndex = 0,
            preferredProfile = AccessibilityProfile.WHEELCHAIR,
            bio = "Active community reviewer evaluating step-free transit routes, elevator availability, and sensory accessible spaces.",
            isEmailVerified = true,
            reviewsCount = 8,
            helpfulVotesCount = 34,
            savedPlacesCount = 4
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    fun updateProfile(
        fullName: String,
        email: String,
        phone: String,
        location: String,
        avatarIndex: Int,
        preferredProfile: AccessibilityProfile,
        bio: String
    ): UserProfile {
        val updated = _userProfile.value.copy(
            fullName = fullName.trim(),
            email = email.trim(),
            phone = phone.trim(),
            location = location.trim(),
            avatarIndex = avatarIndex,
            preferredProfile = preferredProfile,
            bio = bio.trim()
        )
        _userProfile.value = updated
        return updated
    }

    fun incrementReviewCount() {
        val current = _userProfile.value
        _userProfile.value = current.copy(reviewsCount = current.reviewsCount + 1)
    }

    fun updateSavedPlacesCount(count: Int) {
        val current = _userProfile.value
        _userProfile.value = current.copy(savedPlacesCount = count)
    }
}
