package com.example.ui.screens

import android.util.Patterns
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AccessibilityProfile
import com.example.model.UserProfile
import com.example.ui.components.AccessPathFooter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val AVATAR_COLORS = listOf(
    Pair("Teal Ocean", listOf(Color(0xFF0D9488), Color(0xFF14B8A6))),
    Pair("Royal Indigo", listOf(Color(0xFF4F46E5), Color(0xFF6366F1))),
    Pair("Emerald Forest", listOf(Color(0xFF059669), Color(0xFF10B981))),
    Pair("Sunset Rose", listOf(Color(0xFFE11D48), Color(0xFFF43F5E))),
    Pair("Amber Solar", listOf(Color(0xFFD97706), Color(0xFFF59E0B)))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    userProfile: UserProfile,
    onSaveProfile: (
        fullName: String,
        email: String,
        phone: String,
        location: String,
        avatarIndex: Int,
        preferredProfile: AccessibilityProfile,
        bio: String
    ) -> Unit,
    onBackClicked: () -> Unit,
    onNavigateToVenues: () -> Unit,
    onNavigateToMap: () -> Unit,
    onShowAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBackClicked() }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val coroutineScope = rememberCoroutineScope()

    var isEditing by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    var saveSuccessMessage by remember { mutableStateOf<String?>(null) }
    var validationError by remember { mutableStateOf<String?>(null) }

    // Form fields initialized from current userProfile
    var nameField by remember(userProfile, isEditing) { mutableStateOf(userProfile.fullName) }
    var emailField by remember(userProfile, isEditing) { mutableStateOf(userProfile.email) }
    var phoneField by remember(userProfile, isEditing) { mutableStateOf(userProfile.phone) }
    var locationField by remember(userProfile, isEditing) { mutableStateOf(userProfile.location) }
    var bioField by remember(userProfile, isEditing) { mutableStateOf(userProfile.bio) }
    var selectedAvatarIndex by remember(userProfile, isEditing) { mutableIntStateOf(userProfile.avatarIndex) }
    var selectedProfileType by remember(userProfile, isEditing) { mutableStateOf(userProfile.preferredProfile) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .testTag("user_profile_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "User Profile",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClicked,
                        modifier = Modifier.testTag("profile_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to home"
                        )
                    }
                },
                actions = {
                    if (!isEditing) {
                        IconButton(
                            onClick = {
                                isEditing = true
                                validationError = null
                                saveSuccessMessage = null
                            },
                            modifier = Modifier.testTag("edit_profile_action_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("profile_scroll_column"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Success Notification Banner
            item {
                AnimatedVisibility(
                    visible = saveSuccessMessage != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Surface(
                        color = Color(0xFFD1FAE5),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                            .testTag("profile_save_success_banner")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF047857),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = saveSuccessMessage ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF065F46)
                            )
                        }
                    }
                }
            }

            // Error Banner
            item {
                AnimatedVisibility(
                    visible = validationError != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                            .testTag("profile_validation_error_banner")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = validationError ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }

            // Profile Header Card (Avatar, Name, Verification Badge, Location)
            item {
                val activeColors = AVATAR_COLORS.getOrElse(selectedAvatarIndex) { AVATAR_COLORS.first() }.second
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("profile_header_card"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(activeColors)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userProfile.fullName.split(" ")
                                    .mapNotNull { it.firstOrNull()?.toString() }
                                    .take(2)
                                    .joinToString("")
                                    .ifBlank { "AP" },
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }

                        // Name & Verified Badge
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = userProfile.fullName,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (userProfile.isEmailVerified) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Verified Member",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Text(
                                text = userProfile.email,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (userProfile.location.isNotBlank()) {
                                Row(
                                    modifier = Modifier.padding(top = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = userProfile.location,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        // Avatar Picker (Shown when editing)
                        if (isEditing) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Choose Profile Avatar Palette:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AVATAR_COLORS.indices.forEach { index ->
                                        val isCurrent = index == selectedAvatarIndex
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(Brush.linearGradient(AVATAR_COLORS[index].second))
                                                .border(
                                                    width = if (isCurrent) 3.dp else 1.dp,
                                                    color = if (isCurrent) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                                    shape = CircleShape
                                                )
                                                .clickable { selectedAvatarIndex = index }
                                                .testTag("avatar_option_$index"),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isCurrent) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Community Impact Stats Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("profile_stats_card"),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileStatItem(
                            icon = Icons.Default.RateReview,
                            count = userProfile.reviewsCount.toString(),
                            label = "Reviews"
                        )
                        Box(
                            modifier = Modifier
                                .height(36.dp)
                                .width(1.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )
                        ProfileStatItem(
                            icon = Icons.Default.ThumbUp,
                            count = userProfile.helpfulVotesCount.toString(),
                            label = "Helpful Votes"
                        )
                        Box(
                            modifier = Modifier
                                .height(36.dp)
                                .width(1.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )
                        ProfileStatItem(
                            icon = Icons.Default.Bookmark,
                            count = userProfile.savedPlacesCount.toString(),
                            label = "Saved Venues"
                        )
                    }
                }
            }

            // Editable or View Profile Details Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("profile_details_form_card"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isEditing) "Edit Personal Information" else "Personal Information",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (!isEditing) {
                                Button(
                                    onClick = {
                                        isEditing = true
                                        validationError = null
                                    },
                                    contentPadding = ButtonDefaults.TextButtonContentPadding,
                                    modifier = Modifier.height(34.dp).testTag("edit_profile_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Edit Profile", fontSize = 12.sp)
                                }
                            }
                        }

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                            thickness = 1.dp
                        )

                        // Full Name
                        if (isEditing) {
                            OutlinedTextField(
                                value = nameField,
                                onValueChange = { nameField = it },
                                label = { Text("Full Name *") },
                                singleLine = true,
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null)
                                },
                                modifier = Modifier.fillMaxWidth().testTag("profile_name_input")
                            )
                        } else {
                            ProfileDetailField(
                                icon = Icons.Default.Person,
                                label = "Full Name",
                                value = userProfile.fullName
                            )
                        }

                        // Email Address
                        if (isEditing) {
                            OutlinedTextField(
                                value = emailField,
                                onValueChange = { emailField = it },
                                label = { Text("Email Address *") },
                                singleLine = true,
                                leadingIcon = {
                                    Icon(Icons.Default.Email, contentDescription = null)
                                },
                                modifier = Modifier.fillMaxWidth().testTag("profile_email_input")
                            )
                        } else {
                            ProfileDetailField(
                                icon = Icons.Default.Email,
                                label = "Email Address",
                                value = userProfile.email
                            )
                        }

                        // Phone Number
                        if (isEditing) {
                            OutlinedTextField(
                                value = phoneField,
                                onValueChange = { phoneField = it },
                                label = { Text("Phone Number") },
                                singleLine = true,
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, contentDescription = null)
                                },
                                modifier = Modifier.fillMaxWidth().testTag("profile_phone_input")
                            )
                        } else {
                            ProfileDetailField(
                                icon = Icons.Default.Phone,
                                label = "Phone Number",
                                value = userProfile.phone.ifBlank { "Not provided" }
                            )
                        }

                        // Location
                        if (isEditing) {
                            OutlinedTextField(
                                value = locationField,
                                onValueChange = { locationField = it },
                                label = { Text("City / Region") },
                                singleLine = true,
                                leadingIcon = {
                                    Icon(Icons.Default.LocationOn, contentDescription = null)
                                },
                                modifier = Modifier.fillMaxWidth().testTag("profile_location_input")
                            )
                        } else {
                            ProfileDetailField(
                                icon = Icons.Default.LocationOn,
                                label = "Location",
                                value = userProfile.location.ifBlank { "Not provided" }
                            )
                        }

                        // Bio / Reviewer note
                        if (isEditing) {
                            OutlinedTextField(
                                value = bioField,
                                onValueChange = { bioField = it },
                                label = { Text("About You & Accessibility Needs") },
                                maxLines = 4,
                                modifier = Modifier.fillMaxWidth().testTag("profile_bio_input")
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "About Me",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = userProfile.bio.ifBlank { "No bio added yet." },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp
                                )
                            }
                        }

                        // Preferred Accessibility Perspective
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Preferred Accessibility Scoring Profile:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            if (isEditing) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    AccessibilityProfile.entries.take(3).forEach { profile ->
                                        val isSelected = profile == selectedProfileType
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { selectedProfileType = profile }
                                                .testTag("pref_profile_${profile.id}")
                                        ) {
                                            Text(
                                                text = profile.shortName,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(vertical = 8.dp),
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    AccessibilityProfile.entries.drop(3).forEach { profile ->
                                        val isSelected = profile == selectedProfileType
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { selectedProfileType = profile }
                                                .testTag("pref_profile_${profile.id}")
                                        ) {
                                            Text(
                                                text = profile.shortName,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(vertical = 8.dp),
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${userProfile.preferredProfile.title} (${userProfile.preferredProfile.description})",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }

                        // Save Changes and Cancel Buttons (Requirement 7)
                        if (isEditing) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        isEditing = false
                                        validationError = null
                                    },
                                    modifier = Modifier.weight(1f).testTag("cancel_edit_profile_button"),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Cancel")
                                }

                                Button(
                                    onClick = {
                                        // Validate inputs
                                        if (nameField.trim().length < 2) {
                                            validationError = "Please enter a valid full name (at least 2 characters)."
                                            return@Button
                                        }
                                        if (!Patterns.EMAIL_ADDRESS.matcher(emailField.trim()).matches()) {
                                            validationError = "Please enter a valid email address (e.g. name@domain.com)."
                                            return@Button
                                        }

                                        isSaving = true
                                        validationError = null
                                        coroutineScope.launch {
                                            delay(300L) // smooth processing simulation
                                            onSaveProfile(
                                                nameField,
                                                emailField,
                                                phoneField,
                                                locationField,
                                                selectedAvatarIndex,
                                                selectedProfileType,
                                                bioField
                                            )
                                            isSaving = false
                                            isEditing = false
                                            saveSuccessMessage = "Profile updated and persisted successfully!"
                                            delay(3500L)
                                            saveSuccessMessage = null
                                        }
                                    },
                                    modifier = Modifier.weight(1f).testTag("save_profile_button"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    enabled = !isSaving
                                ) {
                                    if (isSaving) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            strokeWidth = 2.dp
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Save Changes", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Security & Privacy Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "Data Protection & Privacy",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Your personal profile details and accessibility settings are stored securely on this device and applied across all 15 public venues.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Responsive Footer
            item {
                AccessPathFooter(
                    onNavigateToVenues = onNavigateToVenues,
                    onNavigateToMap = onNavigateToMap,
                    onNavigateToProfile = {},
                    onShowAbout = onShowAbout
                )
            }
        }
    }
}

@Composable
private fun ProfileStatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = count,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ProfileDetailField(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
        }
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
