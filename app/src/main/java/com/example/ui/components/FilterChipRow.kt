package com.example.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AccessibilityCategory

@Composable
fun FilterChipRow(
    selectedCategories: Set<AccessibilityCategory>,
    onToggleCategory: (AccessibilityCategory) -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("accessibility_filter_row"),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // "All" Chip
        val isAllSelected = selectedCategories.isEmpty()
        FilterChip(
            selected = isAllSelected,
            onClick = onClearFilters,
            label = {
                Text(
                    text = "All Places",
                    fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Medium
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            modifier = Modifier.testTag("filter_chip_all")
        )

        // Chips for each Accessibility Category
        AccessibilityCategory.entries.forEach { category ->
            val isSelected = selectedCategories.contains(category)
            FilterChip(
                selected = isSelected,
                onClick = { onToggleCategory(category) },
                label = {
                    Text(
                        text = category.shortName,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = if (isSelected) Icons.Default.Check else category.icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedLeadingIconColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.testTag("filter_chip_${category.id}")
            )
        }
    }
}
