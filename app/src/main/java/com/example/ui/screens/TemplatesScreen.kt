package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.SubtitleStyle
import com.example.domain.model.SubtitleWord
import com.example.ui.components.HormoziSubtitlesOverlay
import com.example.ui.theme.*
import com.example.ui.viewmodel.ViralClipUiState
import com.example.ui.viewmodel.ViralClipViewModel
import kotlinx.coroutines.delay

@Composable
fun TemplatesScreen(
    viewModel: ViralClipViewModel,
    uiState: ViralClipUiState,
    modifier: Modifier = Modifier
) {
    var previewOffsetSec by remember { mutableFloatStateOf(0f) }

    // Ticker to animate subtitle preview continuously
    LaunchedEffect(Unit) {
        while (true) {
            delay(120)
            previewOffsetSec = (previewOffsetSec + 0.15f) % 4.5f
        }
    }

    val sampleDemoWords = remember {
        listOf(
            SubtitleWord("THIS", 0.0f, 0.5f),
            SubtitleWord("ONE", 0.5f, 1.0f),
            SubtitleWord("RULE", 1.0f, 1.7f),
            SubtitleWord("MADE", 1.7f, 2.2f),
            SubtitleWord("ME", 2.2f, 2.7f),
            SubtitleWord("ONE", 2.7f, 3.2f),
            SubtitleWord("MILLION", 3.2f, 3.9f),
            SubtitleWord("DOLLARS!", 3.9f, 4.5f)
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Subtitle Styles & Templates",
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                    color = TextPrimary
                )
                Text(
                    text = "High-retention captions tested on millions of views",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
        }

        // Live Subtitle Preview Theater
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkBorder))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LIVE CAPTION PREVIEW",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(ViralGold.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = uiState.selectedSubtitleStyle.displayName,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ViralGold
                            )
                        }
                    }

                    // Simulated Video Preview Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0F172A))
                            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        HormoziSubtitlesOverlay(
                            words = sampleDemoWords,
                            currentOffsetSec = previewOffsetSec,
                            style = uiState.selectedSubtitleStyle
                        )
                    }

                    Text(
                        text = "Real-time word-by-word highlight animation synchronized to speech.",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
        }

        // Style Selector Cards
        items(SubtitleStyle.values().size) { index ->
            val style = SubtitleStyle.values()[index]
            val isSelected = style == uiState.selectedSubtitleStyle
            val highlightColor = Color(style.highlightColorHex)

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) DarkSurfaceVariant else DarkSurface
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) highlightColor else DarkBorder)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.onSubtitleStyleChanged(style) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(highlightColor)
                            )
                            Text(
                                text = style.displayName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = when (style) {
                                SubtitleStyle.HORMOZI -> "Bold uppercase font with signature yellow/green bounce."
                                SubtitleStyle.MR_BEAST -> "Cyan and neon yellow pop-in animations with drop shadow."
                                SubtitleStyle.MINIMALIST -> "Clean modern typography with frosted dark background pill."
                                SubtitleStyle.NEON_GLOW -> "Electric magenta & cyan cyberpunk glow aesthetic."
                            },
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(highlightColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Active",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
