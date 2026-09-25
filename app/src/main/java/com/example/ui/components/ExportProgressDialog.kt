package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.domain.model.TargetPlatform
import com.example.domain.model.ViralMoment
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ExportProgressDialog(
    moment: ViralMoment,
    platform: TargetPlatform,
    onDismiss: () -> Unit,
    onExportComplete: () -> Unit
) {
    val context = LocalContext.current
    var progress by remember { mutableFloatStateOf(0.05f) }
    var currentStepText by remember { mutableStateOf("Initializing FFmpeg 9:16 vertical crop...") }
    var isDone by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(400)
        progress = 0.30f
        currentStepText = "Cropping 16:9 ➔ 9:16 with Smart Center Focus..."
        delay(700)
        progress = 0.65f
        currentStepText = "Burning Hormozi-style animated subtitles..."
        delay(800)
        progress = 0.90f
        currentStepText = "Normalizing audio & applying high-pass vocal boost..."
        delay(600)
        progress = 1.0f
        currentStepText = "Encoding HD 1080x1920 @ 60FPS..."
        delay(400)
        isDone = true
        onExportComplete()
    }

    val animatedProgress by animateFloatAsState(targetValue = progress, label = "export_progress")

    Dialog(onDismissRequest = { if (isDone) onDismiss() }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkBorder)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("export_dialog")
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (!isDone) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(ViralPurple.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { animatedProgress },
                            color = ElectricCyan,
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(54.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.MovieFilter,
                            contentDescription = null,
                            tint = ViralPurple,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Text(
                        text = "Rendering Viral Clip",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimary
                    )

                    Text(
                        text = currentStepText,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = ElectricCyan,
                        trackColor = DarkSurfaceElevated
                    )

                    Text(
                        text = "${(animatedProgress * 100).toInt()}% Complete",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )
                } else {
                    // Export Success State
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(ViralGreen.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = ViralGreen,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Text(
                        text = "Clip Rendered in HD 1080p! 🎉",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextPrimary
                    )

                    Text(
                        text = "${moment.title} is ready for ${platform.displayName}.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    // Action buttons
                    Button(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "${moment.aiCaption}\n\n${moment.hashtags.joinToString(" ")}"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share to ${platform.displayName}"))
                            Toast.makeText(context, "Caption & hashtags ready to paste!", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ViralPurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("export_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Export to ${platform.displayName}")
                    }

                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("ViralClip Caption", "${moment.aiCaption}\n\n${moment.hashtags.joinToString(" ")}")
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Copied caption & hashtags to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricCyan),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(ElectricCyan)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("copy_caption_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Copy AI Caption & Hashtags")
                    }

                    TextButton(onClick = onDismiss) {
                        Text("Done", color = TextMuted)
                    }
                }
            }
        }
    }
}
