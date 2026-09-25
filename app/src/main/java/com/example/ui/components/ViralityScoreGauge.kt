package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.ViralMoment
import com.example.ui.theme.*

@Composable
fun ViralityScoreGauge(
    moment: ViralMoment,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = moment.viralityScore / 100f,
        animationSpec = tween(durationMillis = 900),
        label = "virality_progress"
    )

    val gaugeColor = when {
        moment.viralityScore >= 90 -> ViralGreen
        moment.viralityScore >= 80 -> ViralGold
        else -> ElectricCyan
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurfaceVariant)
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Virality",
                        tint = ViralGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "AI Virality Score",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                }

                // Grade Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(gaugeColor.copy(alpha = 0.2f))
                        .border(1.dp, gaugeColor.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = moment.viralityGrade,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = gaugeColor
                    )
                }
            }

            // Main Score Row with Arc Meter
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Circular Gauge
                Box(
                    modifier = Modifier.size(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(80.dp)) {
                        val strokeWidth = 9.dp.toPx()
                        // Track background
                        drawArc(
                            color = DarkBorder,
                            startAngle = 140f,
                            sweepAngle = 260f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        // Active sweep
                        drawArc(
                            brush = Brush.sweepGradient(
                                colors = listOf(ElectricCyan, ViralPurple, gaugeColor)
                            ),
                            startAngle = 140f,
                            sweepAngle = 260f * animatedProgress,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${moment.viralityScore}%",
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "VIRAL",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = gaugeColor
                        )
                    }
                }

                // AI Reason Card
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = ElectricCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Why this clip goes viral:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ElectricCyan
                        )
                    }
                    Text(
                        text = moment.viralityReason,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = TextSecondary
                    )
                }
            }

            // 3 Metric Progress Bars
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                MetricRow(label = "Hook Strength", score = moment.hookStrengthScore, color = ViralPink)
                MetricRow(label = "Audience Retention", score = moment.retentionScore, color = ElectricCyan)
                MetricRow(label = "Shareability Potential", score = moment.shareabilityScore, color = ViralGold)
            }
        }
    }
}

@Composable
private fun MetricRow(
    label: String,
    score: Int,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextMuted,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "$score/100",
            fontSize = 11.sp,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
    }
    LinearProgressIndicator(
        progress = { score / 100f },
        modifier = Modifier
            .fillMaxWidth()
            .height(5.dp)
            .clip(RoundedCornerShape(3.dp)),
        color = color,
        trackColor = DarkSurfaceElevated
    )
}
