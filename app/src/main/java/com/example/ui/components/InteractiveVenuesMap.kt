package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AccessibilityProfile
import com.example.model.PlaceWithStats
import com.example.model.ScoreLevel
import com.example.model.resolveImageRes
import com.example.ui.theme.ScoreChallenging
import com.example.ui.theme.ScoreExcellent
import com.example.ui.theme.ScoreGood
import com.example.ui.theme.ScoreModerate
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun InteractiveVenuesMap(
    places: List<PlaceWithStats>,
    userProfile: AccessibilityProfile,
    onPlaceSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPlaceId by remember { mutableStateOf<String?>(places.firstOrNull()?.place?.id) }
    val activePlace = places.find { it.place.id == selectedPlaceId }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .testTag("interactive_venues_map")
    ) {
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()

        // Stylized City Canvas
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(places) {
                    detectTapGestures { tapOffset ->
                        // Find closest venue pin
                        val hitPlace = places.minByOrNull { item ->
                            val px = item.place.mapX * widthPx
                            val py = item.place.mapY * heightPx
                            val dx = px - tapOffset.x
                            val dy = py - tapOffset.y
                            dx * dx + dy * dy
                        }
                        if (hitPlace != null) {
                            val px = hitPlace.place.mapX * widthPx
                            val py = hitPlace.place.mapY * heightPx
                            val distSq = (px - tapOffset.x) * (px - tapOffset.x) + (py - tapOffset.y) * (py - tapOffset.y)
                            if (distSq < 2200f) { // Within tap radius
                                selectedPlaceId = hitPlace.place.id
                            }
                        }
                    }
                }
        ) {
            // Background city grid
            drawRect(color = Color(0xFFE8ECE9))

            // Water bay in North-East
            val waterPath = Path().apply {
                moveTo(size.width * 0.65f, 0f)
                cubicTo(
                    size.width * 0.62f, size.height * 0.25f,
                    size.width * 0.75f, size.height * 0.40f,
                    size.width, size.height * 0.45f
                )
                lineTo(size.width, 0f)
                close()
            }
            drawPath(path = waterPath, color = Color(0xFFBAE6FD))

            // River along East
            val riverPath = Path().apply {
                moveTo(size.width * 0.65f, size.height * 0.40f)
                cubicTo(
                    size.width * 0.58f, size.height * 0.55f,
                    size.width * 0.64f, size.height * 0.80f,
                    size.width * 0.70f, size.height
                )
                lineTo(size.width * 0.76f, size.height)
                cubicTo(
                    size.width * 0.70f, size.height * 0.78f,
                    size.width * 0.66f, size.height * 0.52f,
                    size.width, size.height * 0.45f
                )
                close()
            }
            drawPath(path = riverPath, color = Color(0xFFBAE6FD))

            // Green park areas
            drawCircle(
                color = Color(0xFFD1FAE5),
                radius = size.width * 0.18f,
                center = Offset(size.width * 0.20f, size.height * 0.35f)
            )
            drawCircle(
                color = Color(0xFFD1FAE5),
                radius = size.width * 0.14f,
                center = Offset(size.width * 0.55f, size.height * 0.18f)
            )

            // Street lines
            val streetColor = Color(0xFFDDE3E0)
            for (i in 1..8) {
                val y = size.height * (i / 9f)
                drawLine(
                    color = streetColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 6f
                )
            }
            for (i in 1..6) {
                val x = size.width * (i / 7f)
                drawLine(
                    color = streetColor,
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 6f
                )
            }

            // Transit arterial route
            drawLine(
                color = Color(0xFF0284C7).copy(alpha = 0.4f),
                start = Offset(0f, size.height * 0.48f),
                end = Offset(size.width * 0.85f, size.height * 0.38f),
                strokeWidth = 8f
            )
        }

        // Colored Venue Pins on Map
        places.forEach { placeStats ->
            val place = placeStats.place
            val isSelected = place.id == selectedPlaceId

            val score = placeStats.personalizedScore(userProfile)
            val scoreLevel = placeStats.personalizedScoreLevel(userProfile)

            val pinColor = when (scoreLevel) {
                ScoreLevel.EXCELLENT -> ScoreExcellent
                ScoreLevel.GOOD -> ScoreGood
                ScoreLevel.MODERATE -> ScoreModerate
                ScoreLevel.CHALLENGING -> ScoreChallenging
            }

            // Pin position
            val xPos = (place.mapX * (maxWidth.value - 40)).dp
            val yPos = (place.mapY * (maxHeight.value - 220)).dp

            Box(
                modifier = Modifier
                    .offset(x = xPos, y = yPos)
                    .clickable { selectedPlaceId = place.id }
                    .testTag("pin_${place.id}")
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Pin badge
                    Box(
                        modifier = Modifier
                            .shadow(elevation = if (isSelected) 8.dp else 3.dp, shape = CircleShape)
                            .clip(CircleShape)
                            .background(pinColor)
                            .border(
                                width = if (isSelected) 3.dp else 1.5.dp,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.9f),
                                shape = CircleShape
                            )
                            .padding(horizontal = 7.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = String.format(Locale.US, "%.1f", score),
                                color = Color.White,
                                fontSize = if (isSelected) 12.sp else 11.sp,
                                fontWeight = FontWeight.Black
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFDE047),
                                modifier = Modifier.size(if (isSelected) 13.dp else 11.dp)
                            )
                        }
                    }

                    // Little triangle pointer
                    Box(
                        modifier = Modifier
                            .offset(y = (-2).dp)
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(pinColor)
                    )
                }
            }
        }

        // Top-left Map Legend
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White.copy(alpha = 0.92f),
            shadowElevation = 3.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LegendItem(ScoreExcellent, "4.5+ High")
                LegendItem(ScoreGood, "3.8+ Good")
                LegendItem(ScoreModerate, "2.8+ Moderate")
                LegendItem(ScoreChallenging, "<2.8 Barrier")
            }
        }

        // Bottom Venue Preview Card
        if (activePlace != null) {
            val place = activePlace.place
            val personalizedScore = activePlace.personalizedScore(userProfile)
            val badgeLabel = activePlace.personalizedBadgeLabel(userProfile)
            val reasonLine = activePlace.personalizedReasonLine(userProfile)

            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { onPlaceSelected(place.id) }
                    .testTag("map_preview_card_${place.id}"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Venue thumbnail
                        val thumbImg = place.resolveImageRes()
                        if (thumbImg != 0) {
                            Image(
                                painter = painterResource(id = thumbImg),
                                contentDescription = "Photo of ${place.name}",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(76.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = place.category.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "• ${place.neighborhood}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Text(
                                text = place.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            // Personalized Score Badge + Badge Label
                            Row(
                                modifier = Modifier.padding(top = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Text(
                                            text = String.format(Locale.US, "%.1f ★", personalizedScore),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }

                                Text(
                                    text = badgeLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Reason Line
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "💡 $reasonLine",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "View details & AI summary",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp).padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
    }
}
