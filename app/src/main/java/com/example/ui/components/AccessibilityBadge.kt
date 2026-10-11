package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.model.ScoreLevel
import com.example.ui.theme.ScoreChallenging
import com.example.ui.theme.ScoreChallengingContainer
import com.example.ui.theme.ScoreChallengingText
import com.example.ui.theme.ScoreExcellent
import com.example.ui.theme.ScoreExcellentContainer
import com.example.ui.theme.ScoreExcellentText
import com.example.ui.theme.ScoreGood
import com.example.ui.theme.ScoreGoodContainer
import com.example.ui.theme.ScoreGoodText
import com.example.ui.theme.ScoreModerate
import com.example.ui.theme.ScoreModerateContainer
import com.example.ui.theme.ScoreModerateText
import java.util.Locale

@Composable
fun AccessibilityScoreBadge(
    score: Float,
    label: String,
    scoreLevel: ScoreLevel,
    modifier: Modifier = Modifier,
    isLarge: Boolean = false
) {
    val (bgColor, textColor, starColor) = when (scoreLevel) {
        ScoreLevel.EXCELLENT -> Triple(ScoreExcellentContainer, ScoreExcellentText, ScoreExcellent)
        ScoreLevel.GOOD -> Triple(ScoreGoodContainer, ScoreGoodText, ScoreGood)
        ScoreLevel.MODERATE -> Triple(ScoreModerateContainer, ScoreModerateText, ScoreModerate)
        ScoreLevel.CHALLENGING -> Triple(ScoreChallengingContainer, ScoreChallengingText, ScoreChallenging)
    }

    val formattedScore = String.format(Locale.US, "%.1f", score)

    if (isLarge) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .background(bgColor)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .semantics {
                    contentDescription = "Accessibility Score $formattedScore out of 5: $label"
                }
                .testTag("large_accessibility_badge")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = formattedScore,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = textColor
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = starColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = label.uppercase(Locale.US),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    letterSpacing = 0.8.sp
                )
            }
        }
    } else {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(bgColor)
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .semantics {
                    contentDescription = "Accessibility score $formattedScore: $label"
                }
                .testTag("compact_accessibility_badge")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = formattedScore,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textColor
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = starColor,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "• $label",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
            }
        }
    }
}
