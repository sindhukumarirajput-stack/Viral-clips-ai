package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.CropAlignmentMode
import com.example.domain.model.SubtitleStyle
import com.example.domain.model.TargetPlatform
import com.example.domain.model.ViralMoment
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.sin

@Composable
fun VerticalVideoPlayer(
    moment: ViralMoment,
    platform: TargetPlatform,
    cropMode: CropAlignmentMode,
    subtitleStyle: SubtitleStyle,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(true) }
    var currentOffsetSec by remember { mutableFloatStateOf(0f) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    val clipDuration = moment.durationSec

    // Reset when clip changes
    LaunchedEffect(moment.id) {
        currentOffsetSec = 0f
        isPlaying = true
    }

    // Playback ticker loop
    LaunchedEffect(isPlaying, playbackSpeed, moment.id) {
        while (isPlaying) {
            delay(50)
            currentOffsetSec += (0.05f * playbackSpeed)
            if (currentOffsetSec >= clipDuration) {
                currentOffsetSec = 0f // Loop playback
            }
        }
    }

    // Kinetic animation states for canvas visualizer
    val infiniteTransition = rememberInfiniteTransition(label = "player_infinite")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 9:16 Vertical Video Frame Container
        Box(
            modifier = Modifier
                .width(260.dp)
                .height(462.dp) // 9:16 aspect ratio
                .clip(RoundedCornerShape(24.dp))
                .border(2.dp, DarkBorder, RoundedCornerShape(24.dp))
                .background(Color.Black)
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .testTag("vertical_video_preview")
        ) {
            // Simulated Video Content Layers based on cropMode
            when (cropMode) {
                CropAlignmentMode.SPLIT_SCREEN -> {
                    // Top: Speaker / Podcast simulation
                    // Bottom: Satisfying ASMR / Gameplay simulation
                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0xFF1E1B4B), Color(0xFF0F172A))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            SpeakerScene(
                                pulseScale = pulseScale,
                                isPlaying = isPlaying,
                                label = "🎙️ Host / Speaker"
                            )
                        }

                        // Divider line
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(ViralGold)
                        )

                        // Bottom split: Minecraft / Satisfying game simulation
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0xFF064E3B), Color(0xFF022C22))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            GameplayScene(wavePhase = wavePhase, isPlaying = isPlaying)
                        }
                    }
                }
                CropAlignmentMode.FACE_TRACKING -> {
                    // Speaker centered with dynamic AI face tracking bounding box
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Color(0xFF312E81), Color(0xFF0A0A14))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        SpeakerScene(
                            pulseScale = pulseScale,
                            isPlaying = isPlaying,
                            label = "Speaker (Face Tracked)"
                        )

                        // AI Face Tracking Box overlay
                        Canvas(modifier = Modifier.size(140.dp)) {
                            drawRoundRect(
                                color = ElectricCyan.copy(alpha = 0.8f),
                                topLeft = Offset(0f, 0f),
                                size = Size(size.width, size.height),
                                cornerRadius = CornerRadius(12f, 12f),
                                style = Stroke(width = 3.dp.toPx())
                            )
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 130.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(ElectricCyan)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "AI FACE LOCK 99%",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }
                    }
                }
                CropAlignmentMode.BLURRED_BG -> {
                    // Blurred background + 16:9 centered video
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF4C1D95), Color(0xFF1E1B4B), Color(0xFF090A10))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Blurred canvas pattern
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(
                                brush = Brush.radialGradient(
                                    listOf(ViralPurple.copy(alpha = 0.3f), Color.Transparent)
                                ),
                                radius = size.width * 0.7f,
                                center = Offset(size.width / 2, size.height / 2)
                            )
                        }

                        // Centered 16:9 video frame
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.92f)
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E1E2E)),
                            contentAlignment = Alignment.Center
                        ) {
                            SpeakerScene(
                                pulseScale = pulseScale,
                                isPlaying = isPlaying,
                                label = "Original 16:9 Podcast"
                            )
                        }
                    }
                }
                CropAlignmentMode.CENTER_CROP -> {
                    // Dynamic Center Crop
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF1E1B4B), Color(0xFF0F172A), Color(0xFF020617))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        SpeakerScene(
                            pulseScale = pulseScale,
                            isPlaying = isPlaying,
                            label = "Smart Center Crop"
                        )
                    }
                }
            }

            // Top Overlay: Platform Badge & 9:16 watermark
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${platform.displayName} • 9:16",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Audio Waveform Visualizer
                AudioWaveformIcon(isPlaying = isPlaying)
            }

            // Middle / Lower Third: Word-by-Word Hormozi Subtitles Overlay
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 54.dp)
            ) {
                HormoziSubtitlesOverlay(
                    words = moment.words,
                    currentOffsetSec = currentOffsetSec,
                    style = subtitleStyle
                )
            }

            // Bottom Progress Bar inside video player
            LinearProgressIndicator(
                progress = { (currentOffsetSec / clipDuration).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .align(Alignment.BottomCenter),
                color = ViralPurple,
                trackColor = Color.White.copy(alpha = 0.2f)
            )
        }

        // External Player Control Bar
        Row(
            modifier = Modifier
                .width(260.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(DarkSurfaceVariant)
                .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Replay
            IconButton(
                onClick = { currentOffsetSec = 0f },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Replay,
                    contentDescription = "Restart",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Play / Pause
            IconButton(
                onClick = { isPlaying = !isPlaying },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(ViralPurple)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Time Display
            Text(
                text = "${ViralMoment.formatSec(currentOffsetSec)} / ${ViralMoment.formatSec(clipDuration)}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            // Speed Button (1x / 1.25x / 1.5x)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(DarkSurfaceElevated)
                    .clickable {
                        playbackSpeed = when (playbackSpeed) {
                            1.0f -> 1.25f
                            1.25f -> 1.5f
                            else -> 1.0f
                        }
                    }
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${playbackSpeed}x",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElectricCyan
                )
            }
        }
    }
}

@Composable
private fun SpeakerScene(
    pulseScale: Float,
    isPlaying: Boolean,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(ViralPurple, Color(0xFF4338CA))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }

        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.85f)
        )
    }
}

@Composable
private fun GameplayScene(
    wavePhase: Float,
    isPlaying: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Canvas(modifier = Modifier.size(width = 160.dp, height = 50.dp)) {
            val barCount = 16
            val barWidth = size.width / barCount
            for (i in 0 until barCount) {
                val waveHeight = if (isPlaying) {
                    ((sin(wavePhase + i * 0.4f) + 1f) / 2f) * size.height * 0.8f + 6.dp.toPx()
                } else 10.dp.toPx()

                drawRoundRect(
                    color = ElectricCyanBright.copy(alpha = 0.85f),
                    topLeft = Offset(i * barWidth + 2.dp.toPx(), size.height - waveHeight),
                    size = Size(barWidth - 4.dp.toPx(), waveHeight),
                    cornerRadius = CornerRadius(4f, 4f)
                )
            }
        }

        Text(
            text = "🎮 Kinetic B-Roll / ASMR Canvas",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun AudioWaveformIcon(isPlaying: Boolean) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.height(14.dp)
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "audio_bars")
        listOf(800, 500, 700, 600).forEachIndexed { index, duration ->
            val heightFraction by infiniteTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(duration, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "bar_$index"
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(if (isPlaying) (14 * heightFraction).dp else 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(ViralGold)
            )
        }
    }
}
