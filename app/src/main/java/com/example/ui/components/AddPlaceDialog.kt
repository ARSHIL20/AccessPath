package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AccessibilityCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlaceSheet(
    onDismiss: () -> Unit,
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
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Public Facility") }
    var address by remember { mutableStateOf("") }
    var neighborhood by remember { mutableStateOf("Downtown") }
    var description by remember { mutableStateOf("") }
    var hours by remember { mutableStateOf("8:00 AM – 8:00 PM Daily") }

    // Accessibility Feature Toggles
    var hasStepFree by remember { mutableStateOf(true) }
    var hasLifts by remember { mutableStateOf(true) }
    var hasWashrooms by remember { mutableStateOf(true) }
    var hasSeating by remember { mutableStateOf(false) }
    var hasWayfinding by remember { mutableStateOf(false) }

    // Additional specific facility toggles
    var hasAutoDoors by remember { mutableStateOf(true) }
    var hasAdultChangingTable by remember { mutableStateOf(false) }
    var hasHearingLoop by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier.testTag("add_place_sheet"),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Add Public Venue",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Submit verified venue with accessibility toggles",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Venue / Building Name *") },
                placeholder = { Text("e.g. City Central Postal Hub") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("add_place_name_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category *") },
                    placeholder = { Text("Transit, Museum, Park...") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = neighborhood,
                    onValueChange = { neighborhood = it },
                    label = { Text("District") },
                    placeholder = { Text("Downtown") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Street Address *") },
                placeholder = { Text("e.g. 100 Civic Blvd") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = hours,
                onValueChange = { hours = it },
                label = { Text("Opening Hours") },
                placeholder = { Text("e.g. 9:00 AM – 6:00 PM") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Accessibility Overview") },
                placeholder = { Text("Brief description of the entrance ramps, hallways, and accessible route...") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Accessibility Feature Toggles:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Toggle on every verified accommodation available on site",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Toggle 1: Step-Free
            FacilityToggleItem(
                category = AccessibilityCategory.STEP_FREE,
                isChecked = hasStepFree,
                onCheckedChange = { hasStepFree = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Toggle 2: Lifts
            FacilityToggleItem(
                category = AccessibilityCategory.LIFTS,
                isChecked = hasLifts,
                onCheckedChange = { hasLifts = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Toggle 3: Washrooms
            FacilityToggleItem(
                category = AccessibilityCategory.ACCESSIBLE_WASHROOMS,
                isChecked = hasWashrooms,
                onCheckedChange = { hasWashrooms = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Toggle 4: Seating
            FacilityToggleItem(
                category = AccessibilityCategory.SEATING,
                isChecked = hasSeating,
                onCheckedChange = { hasSeating = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Toggle 5: Wayfinding
            FacilityToggleItem(
                category = AccessibilityCategory.WAYFINDING,
                isChecked = hasWayfinding,
                onCheckedChange = { hasWayfinding = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Extra toggles
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Specialized Amenities:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Automatic Push/Sensor Doors", fontSize = 13.sp)
                        Switch(
                            checked = hasAutoDoors,
                            onCheckedChange = { hasAutoDoors = it }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Adult Changing Table / Hoist", fontSize = 13.sp)
                        Switch(
                            checked = hasAdultChangingTable,
                            onCheckedChange = { hasAdultChangingTable = it }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Hearing Loop Induction System", fontSize = 13.sp)
                        Switch(
                            checked = hasHearingLoop,
                            onCheckedChange = { hasHearingLoop = it }
                        )
                    }
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        if (name.trim().isBlank() || address.trim().isBlank()) {
                            errorMessage = "Venue name and address are required."
                            return@Button
                        }

                        val tags = mutableSetOf<AccessibilityCategory>()
                        if (hasStepFree) tags.add(AccessibilityCategory.STEP_FREE)
                        if (hasLifts) tags.add(AccessibilityCategory.LIFTS)
                        if (hasWashrooms) tags.add(AccessibilityCategory.ACCESSIBLE_WASHROOMS)
                        if (hasSeating) tags.add(AccessibilityCategory.SEATING)
                        if (hasWayfinding) tags.add(AccessibilityCategory.WAYFINDING)

                        val amenities = mutableListOf<String>()
                        if (hasStepFree) amenities.add("Verified step-free level entry")
                        if (hasLifts) amenities.add("Elevator with accessible reach buttons")
                        if (hasWashrooms) amenities.add("Accessible restroom with safety grab rails")
                        if (hasSeating) amenities.add("Frequent resting seats & priority bays")
                        if (hasWayfinding) amenities.add("High-contrast signs & tactile floor strips")
                        if (hasAutoDoors) amenities.add("Automated motion sensor sliding doors")
                        if (hasAdultChangingTable) amenities.add("Changing Places certified adult bench")
                        if (hasHearingLoop) amenities.add("Hearing loop induction headset loop")

                        onAddPlace(
                            name,
                            category,
                            address,
                            neighborhood,
                            description.ifBlank { "Verified community public facility." },
                            tags,
                            amenities,
                            hours
                        )
                    },
                    modifier = Modifier.weight(1.5f).height(48.dp).testTag("confirm_add_place_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Add Venue", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun FacilityToggleItem(
    category: AccessibilityCategory,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isChecked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    tint = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = category.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }

            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.testTag("toggle_${category.id}")
            )
        }
    }
}
