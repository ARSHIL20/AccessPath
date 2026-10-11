package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PlaceDetailScreen
import com.example.ui.screens.UserProfileScreen

@Composable
fun AccessPathApp(
    viewModel: AccessPathViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val filteredPlaces by viewModel.filteredPlaces.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategories by viewModel.selectedCategories.collectAsStateWithLifecycle()
    val selectedPlaceDetail by viewModel.selectedPlaceDetail.collectAsStateWithLifecycle()
    val isAiLoading by viewModel.isAiLoading.collectAsStateWithLifecycle()
    val aiErrorMessage by viewModel.aiErrorMessage.collectAsStateWithLifecycle()
    val isMapView by viewModel.isMapView.collectAsStateWithLifecycle()
    val accessibilityProfile by viewModel.selectedProfile.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            // Persistent Bottom Navigation Bar across main app sections (Requirement 3, 7, 8)
            if (currentScreen != AppScreen.PLACE_DETAIL) {
                NavigationBar(modifier = Modifier.testTag("app_bottom_nav_bar")) {
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.DIRECTORY,
                        onClick = { viewModel.navigateTo(AppScreen.DIRECTORY) },
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Venues Directory") },
                        label = { Text("Directory", fontWeight = if (currentScreen == AppScreen.DIRECTORY) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("nav_tab_directory")
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.MAP,
                        onClick = { viewModel.navigateTo(AppScreen.MAP) },
                        icon = { Icon(Icons.Default.Map, contentDescription = "Interactive Map") },
                        label = { Text("Map", fontWeight = if (currentScreen == AppScreen.MAP) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("nav_tab_map")
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.PROFILE,
                        onClick = { viewModel.navigateTo(AppScreen.PROFILE) },
                        icon = { Icon(Icons.Default.Person, contentDescription = "User Profile") },
                        label = { Text("Profile", fontWeight = if (currentScreen == AppScreen.PROFILE) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("nav_tab_profile")
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    if (targetState == AppScreen.PLACE_DETAIL) {
                        (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> -width / 3 } + fadeOut()
                        )
                    } else if (initialState == AppScreen.PLACE_DETAIL) {
                        (slideInHorizontally { width -> -width / 3 } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> width } + fadeOut()
                        )
                    } else {
                        fadeIn().togetherWith(fadeOut())
                    }
                },
                label = "screen_transition"
            ) { targetScreen ->
                when (targetScreen) {
                    AppScreen.PLACE_DETAIL -> {
                        val placeStats = selectedPlaceDetail
                        if (placeStats != null) {
                            PlaceDetailScreen(
                                placeStats = placeStats,
                                initialProfile = accessibilityProfile,
                                isAiLoading = isAiLoading,
                                aiErrorMessage = aiErrorMessage,
                                onBackClicked = { viewModel.selectPlace(null) },
                                onToggleBookmark = { viewModel.toggleBookmark(placeStats.place.id) },
                                onGenerateAiSummary = { viewModel.generateAiSummary(placeStats.place.id) },
                                onNavigateToVenues = { viewModel.navigateTo(AppScreen.DIRECTORY) },
                                onNavigateToMap = { viewModel.navigateTo(AppScreen.MAP) },
                                onNavigateToProfile = { viewModel.navigateTo(AppScreen.PROFILE) },
                                onSubmitReview = { author, role, stepFree, lifts, washrooms, seating, signs, comment ->
                                    viewModel.submitReview(
                                        placeId = placeStats.place.id,
                                        authorName = author,
                                        authorRole = role,
                                        stepFreeRating = stepFree,
                                        liftsRating = lifts,
                                        washroomsRating = washrooms,
                                        seatingRating = seating,
                                        wayfindingRating = signs,
                                        comment = comment
                                    )
                                },
                                onHelpfulReviewClicked = { reviewId ->
                                    viewModel.toggleHelpfulReview(reviewId)
                                }
                            )
                        } else {
                            // Fallback to directory if detail is empty
                            viewModel.navigateTo(AppScreen.DIRECTORY)
                        }
                    }

                    AppScreen.PROFILE -> {
                        UserProfileScreen(
                            userProfile = userProfile,
                            onSaveProfile = { name, email, phone, loc, avatarIdx, prefProfile, bio ->
                                viewModel.updateProfile(name, email, phone, loc, avatarIdx, prefProfile, bio)
                            },
                            onBackClicked = { viewModel.navigateTo(AppScreen.DIRECTORY) },
                            onNavigateToVenues = { viewModel.navigateTo(AppScreen.DIRECTORY) },
                            onNavigateToMap = { viewModel.navigateTo(AppScreen.MAP) },
                            onShowAbout = { viewModel.navigateTo(AppScreen.DIRECTORY) }
                        )
                    }

                    AppScreen.DIRECTORY, AppScreen.MAP -> {
                        HomeScreen(
                            places = filteredPlaces,
                            searchQuery = searchQuery,
                            onSearchQueryChanged = viewModel::setSearchQuery,
                            selectedCategories = selectedCategories,
                            onToggleCategory = viewModel::toggleCategory,
                            onClearFilters = viewModel::clearFilters,
                            isMapView = (targetScreen == AppScreen.MAP),
                            onToggleMapView = viewModel::setMapView,
                            userProfile = accessibilityProfile,
                            onProfileChanged = viewModel::setSelectedProfile,
                            onPlaceSelected = viewModel::selectPlace,
                            onToggleBookmark = viewModel::toggleBookmark,
                            onNavigateToProfile = { viewModel.navigateTo(AppScreen.PROFILE) },
                            onAddPlace = viewModel::addPlace
                        )
                    }
                }
            }
        }
    }
}
