package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AccessibilityCategory
import com.example.model.AccessibilityProfile
import com.example.model.PlaceWithStats
import com.example.model.resolveImagesList
import com.example.ui.components.AccessPathFooter
import com.example.ui.components.AddPlaceSheet
import com.example.ui.components.FilterChipRow
import com.example.ui.components.InteractiveVenuesMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    places: List<PlaceWithStats>,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    selectedCategories: Set<AccessibilityCategory>,
    onToggleCategory: (AccessibilityCategory) -> Unit,
    onClearFilters: () -> Unit,
    isMapView: Boolean,
    onToggleMapView: (Boolean) -> Unit,
    userProfile: AccessibilityProfile,
    onProfileChanged: (AccessibilityProfile) -> Unit,
    onPlaceSelected: (String) -> Unit,
    onToggleBookmark: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    onAddPlace: (
        name: String,
        category: String,
        address: String,
        neighborhood: String,
        description: String,
        tags: Set<AccessibilityCategory>,
        amenities: List<String>,
        hours: String
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddPlaceSheet by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var showHeaderMenu by remember { mutableStateOf(false) }

    // Requirement 2: Header scrolls up smoothly on scroll down, reappears on scroll up without empty gap or overlap
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .testTag("home_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.DirectionsWalk,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "AccessPath",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Accessibility Directory for Public Places",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                },
                actions = {
                    // Profile shortcut button
                    IconButton(
                        onClick = onNavigateToProfile,
                        modifier = Modifier.testTag("header_profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Profile",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Info button
                    IconButton(
                        onClick = { showInfoDialog = true },
                        modifier = Modifier.testTag("app_info_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Accessibility Guide",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Requirement 3: Header dropdown menu with functional items
                    Box {
                        IconButton(
                            onClick = { showHeaderMenu = true },
                            modifier = Modifier.testTag("header_overflow_menu_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu Options",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        DropdownMenu(
                            expanded = showHeaderMenu,
                            onDismissRequest = { showHeaderMenu = false },
                            modifier = Modifier.testTag("header_dropdown_menu")
                        ) {
                            DropdownMenuItem(
                                text = { Text("Venues Directory", fontWeight = if (!isMapView) FontWeight.Bold else FontWeight.Normal) },
                                leadingIcon = {
                                    Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
                                },
                                onClick = {
                                    onToggleMapView(false)
                                    showHeaderMenu = false
                                },
                                modifier = Modifier.testTag("menu_item_directory")
                            )
                            DropdownMenuItem(
                                text = { Text("Interactive Map", fontWeight = if (isMapView) FontWeight.Bold else FontWeight.Normal) },
                                leadingIcon = {
                                    Icon(Icons.Default.Map, contentDescription = null)
                                },
                                onClick = {
                                    onToggleMapView(true)
                                    showHeaderMenu = false
                                },
                                modifier = Modifier.testTag("menu_item_map")
                            )
                            DropdownMenuItem(
                                text = { Text("User Profile") },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null)
                                },
                                onClick = {
                                    showHeaderMenu = false
                                    onNavigateToProfile()
                                },
                                modifier = Modifier.testTag("menu_item_profile")
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Scoring Methodology") },
                                leadingIcon = {
                                    Icon(Icons.Default.Info, contentDescription = null)
                                },
                                onClick = {
                                    showHeaderMenu = false
                                    showInfoDialog = true
                                },
                                modifier = Modifier.testTag("menu_item_about")
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            // Requirement 6: Make the Add Venue / Value button smaller & compact
            FloatingActionButton(
                onClick = { showAddPlaceSheet = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(42.dp)
                    .testTag("fab_add_place")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Add Venue",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Segmented Tabs: List vs Map
            TabRow(
                selectedTabIndex = if (isMapView) 1 else 0,
                modifier = Modifier.fillMaxWidth().testTag("map_list_tab_row"),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = !isMapView,
                    onClick = { onToggleMapView(false) },
                    text = { Text("List View (${places.size})", fontWeight = if (!isMapView) FontWeight.Bold else FontWeight.Medium) },
                    icon = { Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = null) },
                    modifier = Modifier.testTag("tab_list_view")
                )
                Tab(
                    selected = isMapView,
                    onClick = { onToggleMapView(true) },
                    text = { Text("Map View (${places.size} Pins)", fontWeight = if (isMapView) FontWeight.Bold else FontWeight.Medium) },
                    icon = { Icon(imageVector = Icons.Default.Map, contentDescription = null) },
                    modifier = Modifier.testTag("tab_map_view")
                )
            }

            // Personalized Accessibility Perspective Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Personalize:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 2.dp)
                )

                AccessibilityProfile.entries.forEach { profile ->
                    val isSelected = profile == userProfile
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        shadowElevation = if (isSelected) 2.dp else 0.dp,
                        modifier = Modifier
                            .clickable { onProfileChanged(profile) }
                            .testTag("profile_chip_${profile.id}")
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

            // Search Bar
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    placeholder = {
                        Text(
                            text = "Search 15 public venues, transit, museums, parks...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { onSearchQueryChanged("") },
                                modifier = Modifier.testTag("clear_search_button")
                            ) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("place_search_input")
                )
            }

            // Filter Chips Row
            FilterChipRow(
                selectedCategories = selectedCategories,
                onToggleCategory = onToggleCategory,
                onClearFilters = onClearFilters
            )

            // Content: Interactive Map OR Responsive Venue Grid
            if (isMapView) {
                InteractiveVenuesMap(
                    places = places,
                    userProfile = userProfile,
                    onPlaceSelected = onPlaceSelected,
                    modifier = Modifier.weight(1f)
                )
            } else {
                // Results count & reset banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${places.size} ${if (places.size == 1) "venue" else "venues"} • ${userProfile.title}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (selectedCategories.isNotEmpty() || searchQuery.isNotEmpty()) {
                        Text(
                            text = "Reset Filters",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable {
                                    onClearFilters()
                                    onSearchQueryChanged("")
                                }
                                .padding(4.dp)
                                .testTag("reset_all_filters_btn")
                        )
                    }
                }

                if (places.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .testTag("empty_places_state"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(54.dp)
                            )
                            Text(
                                text = "No matching venues found",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Try clearing filters to explore all 15 pre-seeded public venues.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            TextButton(
                                onClick = {
                                    onClearFilters()
                                    onSearchQueryChanged("")
                                }
                            ) {
                                Text("Show All 15 Venues")
                            }
                        }
                    }
                } else {
                    // Requirement 4: Responsive, consistent Venue Grid
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 340.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("places_lazy_list"),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(places, key = { it.place.id }) { placeStats ->
                            PlaceCard(
                                placeStats = placeStats,
                                userProfile = userProfile,
                                onClick = { onPlaceSelected(placeStats.place.id) },
                                onToggleBookmark = { onToggleBookmark(placeStats.place.id) }
                            )
                        }

                        // Responsive Footer spanning all grid columns (Requirement 8)
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(modifier = Modifier.padding(top = 16.dp)) {
                                AccessPathFooter(
                                    onNavigateToVenues = { onToggleMapView(false) },
                                    onNavigateToMap = { onToggleMapView(true) },
                                    onNavigateToProfile = onNavigateToProfile,
                                    onShowAbout = { showInfoDialog = true }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddPlaceSheet) {
        AddPlaceSheet(
            onDismiss = { showAddPlaceSheet = false },
            onAddPlace = { name, cat, addr, neigh, desc, tags, amen, hours ->
                onAddPlace(name, cat, addr, neigh, desc, tags, amen, hours)
                showAddPlaceSheet = false
            }
        )
    }

    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = {
                Text(text = "About AccessPath 15 Venues", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "AccessPath features 15 pre-seeded public venues with colored map pins and category ratings.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    HorizontalDivider()
                    Text(
                        text = "Colored Pins & Score Badges:",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "• Green: 4.5+ Exceptional Accessibility\n" +
                               "• Teal: 3.8 - 4.4 High Accessibility\n" +
                               "• Amber: 2.8 - 3.7 Moderate Access\n" +
                               "• Red: Under 2.8 Barriers Present",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Personalized scoring re-weights criteria for Wheelchair, Cane, Vision, Sensory, or Stroller profiles.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) {
                    Text("Got it")
                }
            }
        )
    }
}

/**
 * Clean, consistent PlaceCard displaying only Place name, Location, Rating, and 3-photo auto slideshow
 * (Requirements 1, 3, 4)
 */
@Composable
fun PlaceCard(
    placeStats: PlaceWithStats,
    userProfile: AccessibilityProfile,
    onClick: () -> Unit,
    onToggleBookmark: () -> Unit,
    modifier: Modifier = Modifier
) {
    val place = placeStats.place
    val personalizedScore = placeStats.personalizedScore(userProfile)
    val photos = remember(place) { place.resolveImagesList() }

    // Automatic slideshow: switches photo every 3 seconds (Requirement 3)
    var currentPhotoIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(photos) {
        if (photos.size > 1) {
            while (isActive) {
                delay(3000L)
                currentPhotoIndex = (currentPhotoIndex + 1) % photos.size
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("place_card_${place.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Photo Slideshow container (3 photos, 3-second auto cycle, dot indicators)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AnimatedContent(
                    targetState = currentPhotoIndex,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
                    },
                    label = "photo_slideshow_${place.id}"
                ) { index ->
                    val photoRes = photos.getOrElse(index) { photos.first() }
                    Image(
                        painter = painterResource(id = photoRes),
                        contentDescription = "Photo of ${place.name} (${index + 1} of ${photos.size})",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Subtle gradient overlay for contrast
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.30f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.55f)
                                )
                            )
                        )
                )

                // Top row overlay: Bookmark button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.45f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        IconButton(
                            onClick = onToggleBookmark,
                            modifier = Modifier.testTag("bookmark_${place.id}")
                        ) {
                            Icon(
                                imageVector = if (place.isBookmarked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Save place",
                                tint = if (place.isBookmarked) Color(0xFFFB7185) else Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Bottom center: Slideshow dot indicators (Requirement 3)
                if (photos.size > 1) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        photos.indices.forEach { idx ->
                            val isCurrent = idx == currentPhotoIndex
                            Box(
                                modifier = Modifier
                                    .animateContentSize()
                                    .height(6.dp)
                                    .width(if (isCurrent) 20.dp else 6.dp)
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

            // Clean, minimal information: Place name, Location, and Rating (Requirement 1)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                // Place Name
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Location
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "${place.neighborhood} • ${place.address}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = Color(0xFFFEF3C7),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = String.format(Locale.US, "%.1f", personalizedScore),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                        }
                    }

                    Text(
                        text = "(${placeStats.reviewCount} reviews)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = placeStats.scoreBadgeLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
