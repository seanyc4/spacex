package com.seancoyle.feature.launch.presentation.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.ui.components.progress.CircularProgressBar
import com.seancoyle.core.ui.designsystem.chip.Chip
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.feature.launch.domain.model.*

// ==================== Screen Entry Point ====================

@Composable
fun LaunchScreen(
    launchId: String,
    launchesType: LaunchesType,
    @Suppress("UNUSED_PARAMETER") snackbarHostState: SnackbarHostState,
    @Suppress("UNUSED_PARAMETER") windowSizeClass: androidx.compose.material3.windowsizeclass.WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    val viewModel: LaunchViewModel =
        hiltViewModel<LaunchViewModel, LaunchViewModel.Factory> { factory ->
            factory.create(launchId = launchId, launchType = launchesType)
        }

    val launchState by viewModel.launchState.collectAsStateWithLifecycle()

    LaunchScreenContent(
        launchState = launchState,
        modifier = modifier
    )
}

// ==================== Content States ====================

@Composable
private fun LaunchScreenContent(
    launchState: LaunchUiState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .testTag("launch_screen")
    ) {
        when (launchState) {
            is LaunchUiState.Loading -> LoadingState()
            is LaunchUiState.Success -> SuccessState(launch = launchState.launch)
            is LaunchUiState.Error -> ErrorState(message = launchState.message)
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = "Loading launch details" },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressBar()
    }
}

@Composable
private fun ErrorState(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.dp24)
            .semantics { contentDescription = "Error loading launch: $message" },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.dp16)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = AppTheme.colors.error
            )
            AppText.titleLarge(
                text = "Unable to load launch",
                color = AppTheme.colors.onBackground
            )
            AppText.bodyMedium(
                text = message,
                color = AppTheme.colors.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SuccessState(
    launch: LaunchUI,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = "Launch details for ${launch.missionName}" },
        contentPadding = PaddingValues(bottom = Dimens.dp24)
    ) {
        item { LaunchHeroSection(launch = launch) }
        item { Spacer(modifier = Modifier.height(Dimens.dp16)) }
        item { LaunchDetailsSection(launch = launch) }

        if (launch.launchServiceProvider != null) {
            item { Spacer(modifier = Modifier.height(Dimens.dp16)) }
            item { AgencySection(agency = launch.launchServiceProvider) }
        }

        if (launch.rocket != null) {
            item { Spacer(modifier = Modifier.height(Dimens.dp16)) }
            item { RocketSection(rocket = launch.rocket) }
        }

        if (launch.mission != null) {
            item { Spacer(modifier = Modifier.height(Dimens.dp16)) }
            item { MissionSection(mission = launch.mission) }
        }

        if (launch.pad != null) {
            item { Spacer(modifier = Modifier.height(Dimens.dp16)) }
            item { PadSection(pad = launch.pad) }
        }

        if (launch.updates.isNotEmpty()) {
            item { Spacer(modifier = Modifier.height(Dimens.dp16)) }
            item { UpdatesSection(updates = launch.updates) }
        }

        if (launch.vidUrls.isNotEmpty()) {
            item { Spacer(modifier = Modifier.height(Dimens.dp16)) }
            item { VideoSection(videos = launch.vidUrls) }
        }

        if (launch.missionPatches.isNotEmpty()) {
            item { Spacer(modifier = Modifier.height(Dimens.dp16)) }
            item { MissionPatchesSection(patches = launch.missionPatches) }
        }
    }
}

// ==================== Hero Section ====================

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun LaunchHeroSection(
    launch: LaunchUI,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        // Hero Image with Gradient Overlay
        Box {
            GlideImage(
                model = launch.image.imageUrl,
                contentDescription = "Mission image for ${launch.missionName}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                AppTheme.colors.background.copy(alpha = 0.9f)
                            ),
                            startY = 100f
                        )
                    )
            )
        }

        // Mission Name & Status Overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Dimens.dp24)
                .fillMaxWidth()
        ) {
            AppText.headlineLarge(
                text = launch.missionName,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.onBackground,
                modifier = Modifier.semantics {
                    contentDescription = "Mission name: ${launch.missionName}"
                }
            )

            Spacer(modifier = Modifier.height(Dimens.dp8))

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.dp8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Chip(
                    text = launch.status.label,
                    containerColor = launch.status.containerColor,
                    contentColor = launch.status.contentColor,
                    icon = launch.status.icon
                )

                AppText.bodyLarge(
                    text = launch.launchDate,
                    color = AppTheme.colors.onSurfaceVariant,
                    modifier = Modifier.semantics {
                        contentDescription = "Launch date: ${launch.launchDate}"
                    }
                )
            }
        }
    }
}

// ==================== Details Section ====================

@Composable
private fun LaunchDetailsSection(
    launch: LaunchUI,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = "Launch Details")

            launch.windowStart?.let {
                DetailRow(
                    label = "Window Start",
                    value = it,
                    icon = Icons.Default.DateRange
                )
            }

            launch.windowEnd?.let {
                DetailRow(
                    label = "Window End",
                    value = it,
                    icon = Icons.Default.DateRange
                )
            }

            launch.pad?.let { pad ->
                pad.location?.let { location ->
                    val siteName = pad.name ?: "Unknown"
                    val countryName = location.country?.name ?: ""
                    val countryFlag = location.country?.alpha2Code?.toCountryFlag() ?: ""

                    val siteValue = buildString {
                        append(siteName)
                        if (countryName.isNotEmpty()) {
                            append(" • ")
                            if (countryFlag.isNotEmpty()) {
                                append(countryFlag)
                                append(" ")
                            }
                            append(countryName)
                        }
                    }

                    DetailRow(
                        label = "Launch Site",
                        value = siteValue,
                        icon = Icons.Default.Place
                    )
                }
            }

            if (!launch.failReason.isNullOrEmpty()) {
                DetailRow(
                    label = "Fail Reason",
                    value = launch.failReason,
                    icon = Icons.Default.Warning,
                    valueColor = AppTheme.colors.error
                )
            }
        }
    }
}

// Convert ISO 3166-1 alpha-2 country code to flag emoji
private fun String.toCountryFlag(): String {
    if (this.length != 2) return ""
    val firstChar = Character.codePointAt(this, 0) - 0x41 + 0x1F1E6
    val secondChar = Character.codePointAt(this, 1) - 0x41 + 0x1F1E6
    return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
}

// ==================== Agency Section ====================

@Composable
private fun AgencySection(
    agency: Agency,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = "Launch Provider")

            DetailRow(label = "Name", value = agency.name ?: "Unknown", icon = Icons.Default.AccountCircle)
            agency.abbrev?.let { DetailRow(label = "Abbreviation", value = it, icon = Icons.Default.Star) }
            agency.type?.let { DetailRow(label = "Type", value = it, icon = Icons.Default.Build) }
            agency.description?.let {
                AppText.bodyMedium(
                    text = it,
                    color = AppTheme.colors.onSurfaceVariant,
                    modifier = Modifier.padding(top = Dimens.dp8)
                )
            }
        }
    }
}

// ==================== Rocket Section ====================

@Composable
private fun RocketSection(
    rocket: Rocket,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = "Rocket Configuration")

            rocket.configuration?.let { config ->
                config.name?.let { DetailRow(label = "Name", value = it, icon = Icons.Default.Star) }
                config.fullName?.let { DetailRow(label = "Full Name", value = it, icon = Icons.Default.Info) }
                config.variant?.let { DetailRow(label = "Variant", value = it, icon = Icons.Default.Star) }
            }
        }
    }
}

// ==================== Mission Section ====================

@Composable
private fun MissionSection(
    mission: Mission,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = "Mission Information")

            mission.name?.let { DetailRow(label = "Name", value = it, icon = Icons.Default.Star) }
            mission.type?.let { DetailRow(label = "Type", value = it, icon = Icons.Default.Build) }
            mission.orbit?.name?.let { DetailRow(label = "Orbit", value = it, icon = Icons.Default.Star) }
            mission.description?.let {
                AppText.bodyMedium(
                    text = it,
                    color = AppTheme.colors.onSurfaceVariant,
                    modifier = Modifier.padding(top = Dimens.dp8)
                )
            }
        }
    }
}

// ==================== Pad Section ====================

@Composable
private fun PadSection(
    pad: Pad,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = "Launch Pad")

            pad.name?.let { DetailRow(label = "Name", value = it, icon = Icons.Default.Place) }
            pad.location?.name?.let { DetailRow(label = "Location", value = it, icon = Icons.Default.LocationOn) }
            pad.latitude?.let { lat ->
                pad.longitude?.let { lon ->
                    DetailRow(
                        label = "Coordinates",
                        value = "${lat}, ${lon}",
                        icon = Icons.Default.Place
                    )
                }
            }
        }
    }
}

// ==================== Updates Section ====================

@Composable
private fun UpdatesSection(
    updates: List<LaunchUpdate>,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = "Launch Updates")

            updates.forEach { update ->
                UpdateItem(update = update)
            }
        }
    }
}

@Composable
private fun UpdateItem(
    update: LaunchUpdate,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(Dimens.dp12)
    ) {
        Column(
            modifier = Modifier.padding(Dimens.dp16),
            verticalArrangement = Arrangement.spacedBy(Dimens.dp8)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText.labelMedium(
                    text = update.createdBy ?: "Unknown",
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.primary
                )
                AppText.labelSmall(
                    text = update.createdOn ?: "",
                    color = AppTheme.colors.onSurfaceVariant
                )
            }

            AppText.bodyMedium(
                text = update.comment ?: "",
                color = AppTheme.colors.onSurface
            )
        }
    }
}

// ==================== Video Section ====================

@Composable
private fun VideoSection(
    videos: List<VidUrl>,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = "Videos & Webcasts")

            videos.forEach { video ->
                VideoItem(video = video)
            }
        }
    }
}

@Composable
private fun VideoItem(
    video: VidUrl,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(Dimens.dp12)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.dp16),
            horizontalArrangement = Arrangement.spacedBy(Dimens.dp16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = AppTheme.colors.primary,
                modifier = Modifier.size(32.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                AppText.titleSmall(
                    text = video.title ?: "Video",
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                AppText.bodySmall(
                    text = video.publisher ?: "",
                    color = AppTheme.colors.onSurfaceVariant
                )
            }

            if (video.live == true) {
                AssistChip(
                    onClick = {},
                    label = { AppText.labelSmall(text = "LIVE", color = AppTheme.colors.error) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = AppTheme.colors.error.copy(alpha = 0.2f),
                        labelColor = AppTheme.colors.error
                    )
                )
            }
        }
    }
}

// ==================== Mission Patches Section ====================

@Composable
private fun MissionPatchesSection(
    patches: List<MissionPatch>,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = "Mission Patches")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.dp16)
            ) {
                patches.forEach { patch ->
                    MissionPatchItem(patch = patch, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MissionPatchItem(
    patch: MissionPatch,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.dp8)
    ) {
        GlideImage(
            model = patch.imageUrl,
            contentDescription = "Mission patch: ${patch.name}",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        AppText.labelSmall(
            text = patch.name ?: "Patch",
            color = AppTheme.colors.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ==================== Reusable Components ====================

@Composable
private fun SectionCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.dp16),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(Dimens.dp16)
    ) {
        Column(
            modifier = Modifier.padding(Dimens.dp24),
            content = content
        )
    }
}

@Composable
private fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    AppText.titleLarge(
        text = text,
        fontWeight = FontWeight.Bold,
        color = AppTheme.colors.onSurface,
        modifier = modifier.semantics { contentDescription = "Section: $text" }
    )
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    valueColor: Color = AppTheme.colors.onSurface
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.dp16),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppTheme.colors.primary,
            modifier = Modifier.size(20.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            AppText.labelMedium(
                text = label,
                color = AppTheme.colors.onSurfaceVariant
            )
            AppText.bodyLarge(
                text = value,
                color = valueColor,
                modifier = Modifier.semantics { contentDescription = "$label: $value" }
            )
        }
    }
}
