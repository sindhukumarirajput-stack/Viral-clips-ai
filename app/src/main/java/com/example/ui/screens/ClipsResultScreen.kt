package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.CropAlignmentMode
import com.example.domain.model.SubtitleStyle
import com.example.domain.model.ViralMoment
import com.example.ui.components.ExportProgressDialog
import com.example.ui.components.VerticalVideoPlayer
import com.example.ui.components.ViralityScoreGauge
import com.example.ui.theme.*
import com.example.ui.viewmodel.ViralClipUiState
import com.example.ui.viewmodel.ViralClipViewModel

@Composable
fun ClipsResultScreen(
    viewModel: ViralClipViewModel,
    uiState: ViralClipUiState,
    modifier: Modifier = Modifier
) {
    val selectedClip = uiState.selectedClip ?: uiState.currentClips.firstOrNull()
    var isEditingTimestamps by remember { mutableStateOf(false) }

    if (selectedClip == null) {
        Box(
            modifier = modifier.fillMaxSize().background(DarkBackground),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MovieFilter,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(48.dp)
                )
                Text("No clips generated yet.", color = TextSecondary, fontSize = 15.sp)
                Button(
                    onClick = { viewModel.generateViralClips() },
                    colors = ButtonDefaults.buttonColors(containerColor = ViralPurple)
                ) {
                    Text("Generate Clips from Studio")
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Clips Selector Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DETECTED VIRAL MOMENTS (${uiState.currentClips.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Ranked by Virality AI",
                        fontSize = 11.sp,
                        color = ElectricCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(uiState.currentClips) { clip ->
                        val isCurrent = clip.id == selectedClip.id
                        Box(
                            modifier = Modifier
                                .width(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isCurrent) ViralPurple.copy(alpha = 0.25f) else DarkSurfaceVariant)
                                .border(
                                    width = if (isCurrent) 1.5.dp else 1.dp,
                                    color = if (isCurrent) ViralPurple else DarkBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.onSelectClip(clip) }
                                .padding(10.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(ViralGold.copy(alpha = 0.2f))
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "${clip.viralityScore}% VIRAL",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ViralGold
                                        )
                                    }
                                    Text(
                                        text = clip.formattedTimeRange,
                                        fontSize = 9.sp,
                                        color = TextMuted
                                    )
                                }
                                Text(
                                    text = clip.title,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent) Color.White else TextPrimary,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }

        // 9:16 Vertical Video Preview Player
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkBorder))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    VerticalVideoPlayer(
                        moment = selectedClip,
                        platform = uiState.selectedPlatform,
                        cropMode = uiState.selectedCrop,
                        subtitleStyle = uiState.selectedSubtitleStyle
                    )

                    // Quick Crop Mode & Subtitle Style Toggle Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CropChip(
                            label = "Center",
                            isSelected = uiState.selectedCrop == CropAlignmentMode.CENTER_CROP,
                            onClick = { viewModel.onCropChanged(CropAlignmentMode.CENTER_CROP) }
                        )
                        CropChip(
                            label = "Face Lock",
                            isSelected = uiState.selectedCrop == CropAlignmentMode.FACE_TRACKING,
                            onClick = { viewModel.onCropChanged(CropAlignmentMode.FACE_TRACKING) }
                        )
                        CropChip(
                            label = "Split 2x",
                            isSelected = uiState.selectedCrop == CropAlignmentMode.SPLIT_SCREEN,
                            onClick = { viewModel.onCropChanged(CropAlignmentMode.SPLIT_SCREEN) }
                        )
                        CropChip(
                            label = "Blur BG",
                            isSelected = uiState.selectedCrop == CropAlignmentMode.BLURRED_BG,
                            onClick = { viewModel.onCropChanged(CropAlignmentMode.BLURRED_BG) }
                        )
                    }
                }
            }
        }

        // Virality Score Meter & AI Breakdown
        item {
            ViralityScoreGauge(moment = selectedClip)
        }

        // Timestamp Trim Editor
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkBorder))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.ContentCut, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                            Text(
                                text = "Trim Timestamps",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = "${ViralMoment.formatSec(selectedClip.startTimeSec)} ➔ ${ViralMoment.formatSec(selectedClip.endTimeSec)} (${selectedClip.durationSec.toInt()}s)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ViralGold
                        )
                    }

                    // Trim Slider Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Start: ${ViralMoment.formatSec(selectedClip.startTimeSec)}", fontSize = 10.sp, color = TextMuted)
                        Slider(
                            value = selectedClip.startTimeSec,
                            onValueChange = { newStart ->
                                if (newStart < selectedClip.endTimeSec - 5f) {
                                    viewModel.updateClipTimestamps(selectedClip.id, newStart, selectedClip.endTimeSec)
                                }
                            },
                            valueRange = 0f..selectedClip.endTimeSec.coerceAtLeast(10f),
                            colors = SliderDefaults.colors(
                                thumbColor = ElectricCyan,
                                activeTrackColor = ElectricCyan
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("End:   ${ViralMoment.formatSec(selectedClip.endTimeSec)}", fontSize = 10.sp, color = TextMuted)
                        Slider(
                            value = selectedClip.endTimeSec,
                            onValueChange = { newEnd ->
                                if (newEnd > selectedClip.startTimeSec + 5f) {
                                    viewModel.updateClipTimestamps(selectedClip.id, selectedClip.startTimeSec, newEnd)
                                }
                            },
                            valueRange = (selectedClip.startTimeSec + 5f)..(selectedClip.startTimeSec + 90f),
                            colors = SliderDefaults.colors(
                                thumbColor = ViralPurple,
                                activeTrackColor = ViralPurple
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Primary Action Export Buttons
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = { viewModel.startExport(selectedClip) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(ViralPurple, ViralPink)
                            )
                        )
                        .testTag("download_clip_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Download Clip (HD 1080p • VIP Free)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                OutlinedButton(
                    onClick = { viewModel.startExport(selectedClip) },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricCyan),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(ElectricCyan)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("export_shorts_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Export to ${uiState.selectedPlatform.displayName}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Export Pipeline Dialog
    if (uiState.isExporting && uiState.exportingMoment != null) {
        ExportProgressDialog(
            moment = uiState.exportingMoment,
            platform = uiState.selectedPlatform,
            onDismiss = { viewModel.dismissExport() },
            onExportComplete = { viewModel.finishExport() }
        )
    }
}

@Composable
private fun CropChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) ElectricCyan.copy(alpha = 0.2f) else DarkSurfaceElevated)
            .border(
                1.dp,
                if (isSelected) ElectricCyan else DarkBorder,
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) ElectricCyan else TextSecondary
        )
    }
}
