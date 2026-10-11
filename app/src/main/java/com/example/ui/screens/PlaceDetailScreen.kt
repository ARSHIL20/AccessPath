package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AccessibilityCategory
import com.example.model.AccessibilityProfile
import com.example.model.PlaceWithStats
import com.example.model.ScoreLevel
import com.example.model.resolveImagesList
import com.example.ui.components.AccessPathFooter
import com.example.ui.components.AccessibilityScoreBadge
import com.example.ui.components.AiSummaryCard
import com.example.ui.components.ReviewCard
import com.example.ui.components.ReviewSubmissionSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PlaceDetailScreen(
    placeStats: PlaceWithStats,
    initialProfile: AccessibilityProfile = AccessibilityProfile.GENERAL,
    isAiLoading: Boolean,
    aiErrorMessage: String? = null,
    onBackClicked: () -> Unit,
    onToggleBookmark: () -> Unit,
    onGenerateAiSummary: () -> Unit,
    onNavigateToVenues: () -> Unit = onBackClicked,
    onNavigateToMap: () -> Unit = onBackClicked,
    onNavigateToProfile: () -> Unit = {},
    onSubmitReview: (
        authorName: String,
        authorRole: String,
        stepFreeRating: Int,
        liftsRating: Int,
        washroomsRating: Int,
        seatingRating: Int,
        wayfindingRating: Int,
        comment: String
    ) -> Unit,
    onHelpfulReviewClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackClicked()
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var selectedProfile by remember { mutableStateOf(initialProfile) }
    var showReviewSheet by remember { mutableStateOf(false) }
    var selectedReviewFilter by remember { mutableStateOf("All") }

    val place = placeStats.place
    val photos = remember(place) { place.resolveImagesList() }
    var currentPhotoIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(photos) {
        if (photos.size > 1) {
            while (isActive) {
                delay(3000L)
                currentPhotoIndex = (currentPhotoIndex + 1) % photos.size
            }
        }
    }

    val personalizedScore = placeStats.personalizedScore(selectedProfile)
    val personalizedBadgeLabel = placeStats.personalizedBadgeLabel(selectedProfile)
    val personalizedScoreLevel = placeStats.personalizedScoreLevel(selectedProfile)
    val reasonLine = placeStats.personalizedReasonLine(selectedProfile)

    // Filter reviews according to selected perspective
    val filteredReviews = remember(placeStats.reviews, selectedReviewFilter) {
        when (selectedReviewFilter) {
            "Wheelchair" -> placeStats.reviews.filter { it.authorRole.contains("Wheelchair", ignoreCase = true) }
            "Seniors" -> placeStats.reviews.filter { it.authorRole.contains("Senior", ignoreCase = true) }
            "Sensory / Vision" -> placeStats.reviews.filter {
                it.authorRole.contains("Vision", ignoreCase = true) ||
                it.authorRole.contains("Hearing", ignoreCase = true) ||
                it.authorRole.contains("Deaf", ignoreCase = true)
            }
            "Highest" -> placeStats.reviews.sortedByDescending { it.overallRating }
            else -> placeStats.reviews
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .testTag("place_detail_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = place.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClicked,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to venues directory"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onToggleBookmark) {
                        Icon(
                            imageVector = if (place.isBookmarked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Save place",
                            tint = if (place.isBookmarked) Color(0xFFE11D48) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { showReviewSheet = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("open_write_review_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.RateReview,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Rate This Venue (Stars + Review)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("detail_scroll_column"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Venue Header Card with Photo Slideshow
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("detail_hero_card"),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(22.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Hero Image Slideshow Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(230.dp)
                        ) {
                            AnimatedContent(
                                targetState = currentPhotoIndex,
                                transitionSpec = {
                                    fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
                                },
                                label = "detail_hero_slideshow"
                            ) { index ->
                                val photoRes = photos.getOrElse(index) { photos.first() }
                                Image(
                                    painter = painterResource(id = photoRes),
                                    contentDescription = "Photo of ${place.name} (${index + 1} of ${photos.size})",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            // Dark gradient overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Black.copy(alpha = 0.35f),
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.55f)
                                            )
                                        )
                                    )
                            )

                            // Top pills: Category and Verified Badge
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
                                ) {
                                    Text(
                                        text = place.category,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.Black.copy(alpha = 0.55f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Bookmark,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "${indexToIndicator(currentPhotoIndex, photos.size)}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            // Bottom center: Slideshow dots
                            if (photos.size > 1) {
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(bottom = 12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    photos.indices.forEach { idx ->
                                        val isCurrent = idx == currentPhotoIndex
                                        Box(
                                            modifier = Modifier
                                                .animateContentSize()
                                                .height(6.dp)
                                                .width(if (isCurrent) 22.dp else 6.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (isCurrent) Color.White else Color.White.copy(alpha = 0.5f)
                                                )
                                                .clickable { currentPhotoIndex = idx }
                                        )
                                    }
                                }
                            }
                        }

                        // Hero Venue Text Information
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = place.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "${place.address}, ${place.neighborhood}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row(
                                modifier = Modifier.padding(top = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = place.openingHours,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Profile Selector Pills
                            Text(
                                text = "Personalized View for:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                AccessibilityProfile.entries.forEach { profile ->
                                    val isSelected = profile == selectedProfile
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier
                                            .clickable { selectedProfile = profile }
                                            .testTag("detail_profile_chip_${profile.id}")
                                    ) {
                                        Text(
                                            text = profile.shortName,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Personalized Score Badge Card
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        AccessibilityScoreBadge(
                                            score = personalizedScore,
                                            label = personalizedBadgeLabel,
                                            scoreLevel = personalizedScoreLevel,
                                            isLarge = true
                                        )

                                        Text(
                                            text = "Based on ${placeStats.reviewCount} community audits",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    // Reason line
                                    Text(
                                        text = reasonLine,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Venue Description & Verified Amenities Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("detail_description_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "About Venue & Accessibility",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = place.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 22.sp
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        // Facility Icons
                        Text(
                            text = "Supported Accessibility Features:",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AccessibilityCategory.entries.forEach { category ->
                                val isSupported = place.supportedTags.contains(category)
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isSupported) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        modifier = Modifier.size(46.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = category.icon,
                                                contentDescription = category.title,
                                                tint = if (isSupported) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = category.shortName,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSupported) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSupported) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        // Verified Physical Amenities
                        Text(
                            text = "Verified Physical Amenities:",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        place.amenityDetails.forEach { amenity ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .padding(top = 2.dp)
                                    )
                                Text(
                                    text = amenity,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Category Breakdown Card (All 5 Categories)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("detail_breakdown_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Accessibility Scores Breakdown",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        CategoryScoreBar(
                            category = AccessibilityCategory.STEP_FREE,
                            score = placeStats.stepFreeScore
                        )

                        CategoryScoreBar(
                            category = AccessibilityCategory.LIFTS,
                            score = placeStats.liftsScore
                        )

                        CategoryScoreBar(
                            category = AccessibilityCategory.ACCESSIBLE_WASHROOMS,
                            score = placeStats.washroomsScore
                        )

                        CategoryScoreBar(
                            category = AccessibilityCategory.SEATING,
                            score = placeStats.seatingScore
                        )

                        CategoryScoreBar(
                            category = AccessibilityCategory.WAYFINDING,
                            score = placeStats.wayfindingScore
                        )
                    }
                }
            }

            // AI Community Summary Card (Requirement 5: Fix AI Summary)
            item {
                AiSummaryCard(
                    aiSummary = placeStats.aiSummary,
                    isLoading = isAiLoading,
                    errorMessage = aiErrorMessage,
                    onGenerateClicked = onGenerateAiSummary,
                    onRetryClicked = onGenerateAiSummary,
                    reviewCount = placeStats.reviews.size
                )
            }

            // Community Reviews Section
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().testTag("detail_reviews_section"),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Community Reviews (${placeStats.reviews.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Verified Visits",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Reviews filter chips
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("All", "Wheelchair", "Seniors", "Sensory / Vision", "Highest").forEach { filterLabel ->
                            val isSelected = selectedReviewFilter == filterLabel
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedReviewFilter = filterLabel },
                                label = { Text(filterLabel, fontSize = 12.sp) }
                            )
                        }
                    }
                }
            }

            // Review List
            if (filteredReviews.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "No reviews under '$selectedReviewFilter'",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Be the first to share your experience from this perspective!",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredReviews, key = { it.id }) { review ->
                    ReviewCard(
                        review = review,
                        onHelpfulClicked = { onHelpfulReviewClicked(review.id) }
                    )
                }
            }

            // Consistent Responsive Footer (Requirement 8)
            item {
                AccessPathFooter(
                    onNavigateToVenues = onNavigateToVenues,
                    onNavigateToMap = onNavigateToMap,
                    onNavigateToProfile = onNavigateToProfile,
                    onShowAbout = onNavigateToVenues
                )
            }

            // Bottom space for floating review button
            item {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }

    if (showReviewSheet) {
        ReviewSubmissionSheet(
            place = place,
            onDismiss = { showReviewSheet = false },
            onSubmitReview = { author, role, stepFree, lifts, washrooms, seating, signs, comment ->
                onSubmitReview(author, role, stepFree, lifts, washrooms, seating, signs, comment)
                showReviewSheet = false
            }
        )
    }
}

private fun indexToIndicator(current: Int, total: Int): String {
    return "${current + 1}/$total"
}

@Composable
private fun CategoryScoreBar(
    category: AccessibilityCategory,
    score: Float
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = String.format(Locale.US, "%.1f / 5.0", score),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        LinearProgressIndicator(
            progress = { (score / 5.0f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = when {
                score >= 4.4f -> Color(0xFF059669)
                score >= 3.8f -> Color(0xFF0D9488)
                score >= 2.8f -> Color(0xFFD97706)
                else -> Color(0xFFE11D48)
            },
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
