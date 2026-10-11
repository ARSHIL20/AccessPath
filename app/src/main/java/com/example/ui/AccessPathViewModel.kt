package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AccessPathRepository
import com.example.data.UserProfileRepository
import com.example.model.AccessibilityCategory
import com.example.model.AccessibilityProfile
import com.example.model.PlaceWithStats
import com.example.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    DIRECTORY,
    MAP,
    PROFILE,
    PLACE_DETAIL
}

class AccessPathViewModel(
    private val repository: AccessPathRepository = AccessPathRepository(),
    private val profileRepository: UserProfileRepository = UserProfileRepository()
) : ViewModel() {

    private val _currentScreen = MutableStateFlow(AppScreen.DIRECTORY)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategories = MutableStateFlow<Set<AccessibilityCategory>>(emptySet())
    val selectedCategories: StateFlow<Set<AccessibilityCategory>> = _selectedCategories.asStateFlow()

    private val _selectedPlaceId = MutableStateFlow<String?>(null)
    val selectedPlaceId: StateFlow<String?> = _selectedPlaceId.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    private val _aiErrorMessage = MutableStateFlow<String?>(null)
    val aiErrorMessage: StateFlow<String?> = _aiErrorMessage.asStateFlow()

    // Map vs List view mode
    private val _isMapView = MutableStateFlow(false)
    val isMapView: StateFlow<Boolean> = _isMapView.asStateFlow()

    // Personalized Accessibility Profile
    private val _selectedProfile = MutableStateFlow(AccessibilityProfile.GENERAL)
    val selectedProfile: StateFlow<AccessibilityProfile> = _selectedProfile.asStateFlow()

    // Persistent User Profile
    val userProfile: StateFlow<UserProfile> = profileRepository.userProfile

    val allPlaces: StateFlow<List<PlaceWithStats>> = repository.allPlacesWithStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredPlaces: StateFlow<List<PlaceWithStats>> = combine(
        allPlaces,
        _searchQuery,
        _selectedCategories
    ) { places, query, tags ->
        places.filter { item ->
            val place = item.place
            val matchesQuery = query.isBlank() ||
                    place.name.contains(query, ignoreCase = true) ||
                    place.category.contains(query, ignoreCase = true) ||
                    place.address.contains(query, ignoreCase = true) ||
                    place.neighborhood.contains(query, ignoreCase = true)

            val matchesTags = tags.isEmpty() || tags.all { requiredTag ->
                place.supportedTags.contains(requiredTag)
            }

            matchesQuery && matchesTags
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedPlaceDetail: StateFlow<PlaceWithStats?> = combine(
        allPlaces,
        _selectedPlaceId
    ) { places, id ->
        if (id == null) null else places.find { it.place.id == id }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun navigateTo(screen: AppScreen) {
        if (screen == AppScreen.MAP) {
            _isMapView.value = true
        } else if (screen == AppScreen.DIRECTORY) {
            _isMapView.value = false
        }
        if (screen != AppScreen.PLACE_DETAIL) {
            _selectedPlaceId.value = null
        }
        _currentScreen.value = screen
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleCategory(category: AccessibilityCategory) {
        val current = _selectedCategories.value
        _selectedCategories.value = if (current.contains(category)) {
            current - category
        } else {
            current + category
        }
    }

    fun clearFilters() {
        _selectedCategories.value = emptySet()
    }

    fun selectPlace(placeId: String?) {
        _selectedPlaceId.value = placeId
        _aiErrorMessage.value = null
        if (placeId != null) {
            _currentScreen.value = AppScreen.PLACE_DETAIL
        } else {
            _currentScreen.value = if (_isMapView.value) AppScreen.MAP else AppScreen.DIRECTORY
        }
    }

    fun toggleBookmark(placeId: String) {
        repository.toggleBookmark(placeId)
        val bookmarkedCount = allPlaces.value.count { it.place.isBookmarked }
        profileRepository.updateSavedPlacesCount(bookmarkedCount)
    }

    fun toggleHelpfulReview(reviewId: String) {
        repository.toggleHelpfulReview(reviewId)
    }

    fun setMapView(isMap: Boolean) {
        _isMapView.value = isMap
        _currentScreen.value = if (isMap) AppScreen.MAP else AppScreen.DIRECTORY
    }

    fun setSelectedProfile(profile: AccessibilityProfile) {
        _selectedProfile.value = profile
    }

    fun generateAiSummary(placeId: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            _aiErrorMessage.value = null
            try {
                repository.generateOrGetAiSummary(placeId)
            } catch (e: Exception) {
                _aiErrorMessage.value = "AI summary generation could not be completed: ${e.localizedMessage ?: "Network or service limitation"}. You can retry or rely on community audit scores."
            } finally {
                _isAiLoading.value = false
            }
        }
    }

    fun submitReview(
        placeId: String,
        authorName: String,
        authorRole: String,
        stepFreeRating: Int,
        liftsRating: Int,
        washroomsRating: Int,
        seatingRating: Int,
        wayfindingRating: Int,
        comment: String
    ) {
        viewModelScope.launch {
            repository.addReview(
                placeId = placeId,
                authorName = authorName,
                authorRole = authorRole,
                stepFreeRating = stepFreeRating,
                liftsRating = liftsRating,
                washroomsRating = washroomsRating,
                seatingRating = seatingRating,
                wayfindingRating = wayfindingRating,
                comment = comment
            )
            profileRepository.incrementReviewCount()
        }
    }

    fun addPlace(
        name: String,
        category: String,
        address: String,
        neighborhood: String,
        description: String,
        tags: Set<AccessibilityCategory>,
        amenities: List<String>,
        hours: String
    ) {
        viewModelScope.launch {
            val newPlace = repository.addNewPlace(
                name = name,
                category = category,
                address = address,
                neighborhood = neighborhood,
                description = description,
                tags = tags,
                amenities = amenities,
                hours = hours
            )
            _selectedPlaceId.value = newPlace.id
            _currentScreen.value = AppScreen.PLACE_DETAIL
        }
    }

    fun updateProfile(
        fullName: String,
        email: String,
        phone: String,
        location: String,
        avatarIndex: Int,
        preferredProfile: AccessibilityProfile,
        bio: String
    ) {
        profileRepository.updateProfile(
            fullName = fullName,
            email = email,
            phone = phone,
            location = location,
            avatarIndex = avatarIndex,
            preferredProfile = preferredProfile,
            bio = bio
        )
        _selectedProfile.value = preferredProfile
    }
}
