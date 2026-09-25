package com.example.ui.screens

import android.content.ClipboardManager
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.sample.SampleData
import com.example.data.sample.SamplePodcast
import com.example.ui.components.ClipCustomizerPanel
import com.example.ui.theme.*
import com.example.ui.viewmodel.ViralClipUiState
import com.example.ui.viewmodel.ViralClipViewModel

@Composable
fun StudioScreen(
    viewModel: ViralClipViewModel,
    uiState: ViralClipUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            viewModel.onUrlChanged("device_video_${System.currentTimeMillis()}.mp4")
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF1E1B4B),
                                Color(0xFF0F172A),
                                Color(0xFF172554)
                            )
                        )
                    )
                    .border(
                        1.dp,
                        Brush.linearGradient(listOf(ViralPurple.copy(alpha = 0.6f), ElectricCyan.copy(alpha = 0.4f))),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ViralGold.copy(alpha = 0.25f))
                                .border(1.dp, ViralGold.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "👑 VIP PRO UNLIMITED • 100% FREE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = ViralGold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ViralPurple.copy(alpha = 0.3f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "✨ GEMINI 3.5 FLASH",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ElectricCyan
                            )
                        }
                    }

                    Text(
                        text = "Turn Long Videos into Viral 9:16 Shorts",
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        color = Color.White,
                        lineHeight = 28.sp
                    )

                    Text(
                        text = "AI analyzes hooks, computes virality scores, crops to vertical, and generates Hormozi-style animated subtitles.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Input Section
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkBorder))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "1. PASTE YOUTUBE URL OR UPLOAD VIDEO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )

                    // URL Input field
                    OutlinedTextField(
                        value = uiState.inputUrl,
                        onValueChange = { viewModel.onUrlChanged(it) },
                        placeholder = { Text("Paste YouTube / Podcast URL...", color = TextMuted) },
                        leadingIcon = {
                            Icon(Icons.Default.Link, contentDescription = null, tint = ElectricCyan)
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clipText = clipboard.primaryClip?.getItemAt(0)?.text?.toString()
                                    if (!clipText.isNullOrBlank()) {
                                        viewModel.onUrlChanged(clipText)
                                    }
                                }
                            ) {
                                Icon(Icons.Default.ContentPaste, contentDescription = "Paste", tint = ViralPurpleLight)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ViralPurple,
                            unfocusedBorderColor = DarkBorder,
                            focusedContainerColor = DarkSurface,
                            unfocusedContainerColor = DarkSurface,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("url_input_field")
                    )

                    // Drag-and-drop / Local File Picker Dropzone
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceElevated)
                            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                            .clickable { filePickerLauncher.launch("video/*") }
                            .padding(vertical = 18.dp, horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = "Upload",
                                tint = ElectricCyan,
                                modifier = Modifier.size(24.dp)
                            )
                            Column {
                                Text(
                                    text = "Drop MP4 / MOV or Tap to Browse",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Supports up to 2 hours 1080p / 4K podcast audio",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }

                    // Popular Sample Podcasts Presets
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "💡 Or Try an Instant Sample Podcast:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ViralGold
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(SampleData.samplePodcasts) { podcast ->
                                val isSelected = uiState.inputUrl == podcast.url
                                Surface(
                                    onClick = { viewModel.loadSamplePodcast(podcast) },
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) ViralPurple.copy(alpha = 0.3f) else DarkSurface,
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = if (isSelected) ViralPurple else DarkBorder
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = podcast.title.take(28) + "...",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else TextPrimary
                                        )
                                        Text(
                                            text = "${podcast.host} • ${podcast.durationText}",
                                            fontSize = 10.sp,
                                            color = TextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Clip Customizer Settings (Platform, Duration, Crop, Subtitle Style)
        item {
            ClipCustomizerPanel(
                selectedPlatform = uiState.selectedPlatform,
                onPlatformSelected = { viewModel.onPlatformChanged(it) },
                selectedDuration = uiState.selectedDuration,
                onDurationSelected = { viewModel.onDurationChanged(it) },
                selectedCrop = uiState.selectedCrop,
                onCropSelected = { viewModel.onCropChanged(it) },
                selectedSubtitleStyle = uiState.selectedSubtitleStyle,
                onSubtitleStyleSelected = { viewModel.onSubtitleStyleChanged(it) }
            )
        }

        // Processing State / Generate CTA Button
        item {
            if (uiState.isAnalyzing) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ViralPurple))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(
                            color = ElectricCyan,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(36.dp)
                        )

                        Text(
                            text = "Processing Video with Gemini 3.5 Flash",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )

                        Text(
                            text = uiState.analysisStep,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        LinearProgressIndicator(
                            progress = { uiState.analysisProgress },
                            color = ViralPurple,
                            trackColor = DarkSurfaceElevated,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp))
                        )
                    }
                }
            } else {
                Button(
                    onClick = { viewModel.generateViralClips() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(ViralPurple, ElectricCyan)
                            )
                        )
                        .testTag("generate_clips_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "⚡ Generate Viral Clips with AI",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
