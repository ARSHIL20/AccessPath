package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Accessible
import androidx.compose.material.icons.filled.Elevator
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Wc
import androidx.compose.ui.graphics.vector.ImageVector

enum class AccessibilityCategory(
    val id: String,
    val title: String,
    val shortName: String,
    val description: String,
    val icon: ImageVector
) {
    STEP_FREE(
        id = "step_free",
        title = "Step-Free Access",
        shortName = "Step-Free",
        description = "Ramps, zero-threshold doors, level sidewalks, wide automatic entrances",
        icon = Icons.AutoMirrored.Filled.Accessible
    ),
    LIFTS(
        id = "lifts",
        title = "Lifts & Elevators",
        shortName = "Lifts",
        description = "Spacious elevators, tactile buttons, audible floor call announcements",
        icon = Icons.Default.Elevator
    ),
    ACCESSIBLE_WASHROOMS(
        id = "washrooms",
        title = "Accessible Washrooms",
        shortName = "Washrooms",
        description = "Grab bars, emergency cords, roll-in wash basins, wide clearance",
        icon = Icons.Default.Wc
    ),
    SEATING(
        id = "seating",
        title = "Rest & Priority Seating",
        shortName = "Seating",
        description = "Benches at regular intervals, companion spaces, quiet sensory nooks",
        icon = Icons.Default.EventSeat
    ),
    WAYFINDING(
        id = "wayfinding",
        title = "Wayfinding & Signage",
        shortName = "Wayfinding",
        description = "High contrast visual signs, tactile floor paving, audio assistance",
        icon = Icons.Default.Explore
    );

    companion object {
        fun fromId(id: String): AccessibilityCategory? = entries.find { it.id == id }
    }
}
