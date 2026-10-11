package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AccessibilityCategory
import com.example.model.Place
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewSubmissionSheet(
    place: Place,
    onDismiss: () -> Unit,
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
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var authorName by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Wheelchair User") }

    var overallQuickRating by remember { mutableIntStateOf(5) }
    var showCategoryFineTune by remember { mutableStateOf(false) }

    var stepFreeRating by remember { mutableIntStateOf(5) }
    var liftsRating by remember { mutableIntStateOf(5) }
    var washroomsRating by remember { mutableIntStateOf(5) }
    var seatingRating by remember { mutableIntStateOf(5) }
    var wayfindingRating by remember { mutableIntStateOf(5) }

    var commentText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val calculatedOverall = (stepFreeRating + liftsRating + washroomsRating + seatingRating + wayfindingRating) / 5.0f

    val roles = listOf(
        "Wheelchair User",
        "Power Chair Traveler",
        "Senior (Walker/Cane)",
        "Low Vision / Guide Dog",
        "Deaf / Hard of Hearing",
        "Sensory / Neurodivergent",
        "Parent with Stroller",
        "General Visitor"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier.testTag("review_submission_sheet"),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Rate & Review Venue",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = place.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_review_sheet_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close review form"
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Star Rating Card (Requirement 6: Rate a venue: stars + short text)
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().testTag("quick_star_rating_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Overall Accessibility Rating",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    // 5 Big interactive stars
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (star in 1..5) {
                            val isSelected = star <= overallQuickRating
                            Icon(
                                imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                contentDescription = "$star stars",
                                tint = if (isSelected) Color(0xFFEAB308) else MaterialTheme.colorScheme.outline,
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple(bounded = true, radius = 24.dp)
                                    ) {
                                        overallQuickRating = star
                                        // Set all category ratings to match quick rating baseline
                                        stepFreeRating = star
                                        liftsRating = star
                                        washroomsRating = star
                                        seatingRating = star
                                        wayfindingRating = star
                                    }
                                    .padding(2.dp)
                                    .testTag("quick_star_$star")
                            )
                        }
                    }

                    Text(
                        text = when (overallQuickRating) {
                            5 -> "5 ★ • Exceptional / Fully Barrier-Free"
                            4 -> "4 ★ • Highly Accessible / Minor Assistance"
                            3 -> "3 ★ • Moderate Access / Some Steps or Obstacles"
                            2 -> "2 ★ • Challenging / Noticeable Barriers"
                            else -> "1 ★ • Inaccessible / Severe Obstacles"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Author Name
            OutlinedTextField(
                value = authorName,
                onValueChange = { authorName = it },
                label = { Text("Your Name or Moniker") },
                placeholder = { Text("e.g. Alex Rivera") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("author_name_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Author Role / Perspective
            Text(
                text = "Your Accessibility Perspective:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                roles.forEach { role ->
                    val isSelected = role == selectedRole
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { selectedRole = role }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .testTag("role_chip_$role")
                    ) {
                        Text(
                            text = role,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Short text review input (Requirement 6: Rate a venue: stars + short text)
            Text(
                text = "Review Experience (Short Text):",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = commentText,
                onValueChange = {
                    commentText = it
                    errorMessage = null
                },
                placeholder = {
                    Text("e.g. Smooth wide entrance, very fast elevators, and clean accessible restrooms with emergency cords...")
                },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("review_comment_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Toggle to customize individual category ratings
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showCategoryFineTune = !showCategoryFineTune }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (showCategoryFineTune) "Hide individual category stars" else "Customize individual category stars (Optional)",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = if (showCategoryFineTune) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            AnimatedVisibility(visible = showCategoryFineTune) {
                Column(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    InteractiveCategoryRatingInput(
                        category = AccessibilityCategory.STEP_FREE,
                        currentRating = stepFreeRating,
                        onRatingChanged = { stepFreeRating = it }
                    )
                    InteractiveCategoryRatingInput(
                        category = AccessibilityCategory.LIFTS,
                        currentRating = liftsRating,
                        onRatingChanged = { liftsRating = it }
                    )
                    InteractiveCategoryRatingInput(
                        category = AccessibilityCategory.ACCESSIBLE_WASHROOMS,
                        currentRating = washroomsRating,
                        onRatingChanged = { washroomsRating = it }
                    )
                    InteractiveCategoryRatingInput(
                        category = AccessibilityCategory.SEATING,
                        currentRating = seatingRating,
                        onRatingChanged = { seatingRating = it }
                    )
                    InteractiveCategoryRatingInput(
                        category = AccessibilityCategory.WAYFINDING,
                        currentRating = wayfindingRating,
                        onRatingChanged = { wayfindingRating = it }
                    )
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        if (commentText.trim().length < 4) {
                            errorMessage = "Please enter a short review sentence."
                            return@Button
                        }
                        onSubmitReview(
                            authorName.ifBlank { "Community Contributor" },
                            selectedRole,
                            stepFreeRating,
                            liftsRating,
                            washroomsRating,
                            seatingRating,
                            wayfindingRating,
                            commentText
                        )
                    },
                    modifier = Modifier
                        .weight(1.5f)
                        .height(48.dp)
                        .testTag("submit_review_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Submit Rating",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
