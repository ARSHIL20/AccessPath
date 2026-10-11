package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.ui.theme.ScoreChallenging
import com.example.ui.theme.ScoreExcellent
import com.example.ui.theme.ScoreGood
import com.example.ui.theme.ScoreModerate
import java.util.Locale

@Composable
fun CategoryScoreBar(
    category: AccessibilityCategory,
    score: Float,
    modifier: Modifier = Modifier
) {
    val progress = (score / 5.0f).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress")

    val barColor = when {
        score >= 4.5f -> ScoreExcellent
        score >= 3.8f -> ScoreGood
        score >= 2.8f -> ScoreModerate
        else -> ScoreChallenging
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                contentDescription = "${category.title}: ${String.format(Locale.US, "%.1f", score)} out of 5"
            }
            .testTag("score_bar_${category.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = String.format(Locale.US, "%.1f", score),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "/ 5",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = barColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
fun InteractiveCategoryRatingInput(
    category: AccessibilityCategory,
    currentRating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val ratingDescription = when (currentRating) {
        5 -> "Exceptional • Fully Barrier-Free"
        4 -> "Good • Minor Accommodation Needed"
        3 -> "Moderate • Partial Accessibility"
        2 -> "Challenging • Noticeable Obstacles"
        1 -> "Poor • Inaccessible / Severe Barriers"
        else -> "Tap a star to rate"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("interactive_rating_${category.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Star selectors
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (star in 1..5) {
                    val isSelected = star <= currentRating
                    val interactionSource = remember { MutableInteractionSource() }

                    Icon(
                        imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Rate $star stars for ${category.title}",
                        tint = if (isSelected) Color(0xFFEAB308) else MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .clickable(
                                interactionSource = interactionSource,
                                indication = ripple(bounded = true, radius = 20.dp)
                            ) {
                                onRatingChanged(star)
                            }
                            .padding(2.dp)
                            .testTag("star_${category.id}_$star")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = ratingDescription,
                style = MaterialTheme.typography.bodySmall,
                color = if (currentRating > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                fontWeight = if (currentRating > 0) FontWeight.Medium else FontWeight.Normal
            )
            if (currentRating > 0) {
                Text(
                    text = "$currentRating / 5",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
