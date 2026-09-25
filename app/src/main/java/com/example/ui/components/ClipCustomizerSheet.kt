package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.ClipDurationOption
import com.example.domain.model.CropAlignmentMode
import com.example.domain.model.SubtitleStyle
import com.example.domain.model.TargetPlatform
import com.example.ui.theme.*

@Composable
fun ClipCustomizerPanel(
    selectedPlatform: TargetPlatform,
    onPlatformSelected: (TargetPlatform) -> Unit,
    selectedDuration: ClipDurationOption,
    onDurationSelected: (ClipDurationOption) -> Unit,
    selectedCrop: CropAlignmentMode,
    onCropSelected: (CropAlignmentMode) -> Unit,
    selectedSubtitleStyle: SubtitleStyle,
    onSubtitleStyleSelected: (SubtitleStyle) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkBorder))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "⚡ Viral Clip Customizer",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextPrimary
            )

            // 1. Target Platform Selection
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "TARGET PLATFORM",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TargetPlatform.values().forEach { platform ->
                        val isSelected = platform == selectedPlatform
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) ViralPurple.copy(alpha = 0.25f) else DarkSurfaceElevated)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) ViralPurple else DarkBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { onPlatformSelected(platform) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = platform.displayName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else TextSecondary
                                )
                                Text(
                                    text = platform.aspectRatio,
                                    fontSize = 10.sp,
                                    color = if (isSelected) ElectricCyan else TextMuted
                                )
                            }
                        }
                    }
                }
            }

            // 2. Preferred Clip Duration
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "TARGET CLIP DURATION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ClipDurationOption.values().forEach { durationOpt ->
                        val isSelected = durationOpt == selectedDuration
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) ElectricCyan.copy(alpha = 0.2f) else DarkSurfaceElevated)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) ElectricCyan else DarkBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { onDurationSelected(durationOpt) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = durationOpt.displayName,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextSecondary
                            )
                        }
                    }
                }
            }

            // 3. Crop Alignment & Framing Mode
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "VIDEO CROP ALIGNMENT (16:9 ➔ 9:16)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    CropAlignmentMode.values().forEach { mode ->
                        val isSelected = mode == selectedCrop
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) DarkSurfaceElevated else DarkSurface)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) ViralGold else DarkBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { onCropSelected(mode) }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = mode.displayName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) ViralGold else TextPrimary
                                )
                                Text(
                                    text = mode.description,
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = ViralGold,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 4. Hormozi Subtitle Style
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "AUTO-SUBTITLES PRESET",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SubtitleStyle.values().forEach { style ->
                        val isSelected = style == selectedSubtitleStyle
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Color(style.highlightColorHex).copy(alpha = 0.2f) else DarkSurfaceElevated)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) Color(style.highlightColorHex) else DarkBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { onSubtitleStyleSelected(style) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = style.displayName,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color(style.highlightColorHex) else TextSecondary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(Color(style.highlightColorHex))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
