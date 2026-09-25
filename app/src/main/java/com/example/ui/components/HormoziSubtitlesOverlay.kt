package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.SubtitleStyle
import com.example.domain.model.SubtitleWord

@Composable
fun HormoziSubtitlesOverlay(
    words: List<SubtitleWord>,
    currentOffsetSec: Float,
    style: SubtitleStyle,
    modifier: Modifier = Modifier
) {
    if (words.isEmpty()) return

    // Find the currently active word index
    val activeIndex = words.indexOfFirst {
        currentOffsetSec >= it.startOffsetSec && currentOffsetSec <= it.endOffsetSec
    }.let { if (it == -1) words.indexOfLast { w -> currentOffsetSec >= w.endOffsetSec }.coerceAtLeast(0) else it }

    // Group into chunks of 3-4 words centered around activeIndex
    val chunkStart = (activeIndex / 4) * 4
    val chunkEnd = (chunkStart + 4).coerceAtMost(words.size)
    val displayWords = words.subList(chunkStart, chunkEnd)

    val primaryColor = Color(style.primaryColorHex)
    val highlightColor = Color(style.highlightColorHex)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Optional pill background
        val contentBoxModifier = if (style.hasBackgroundBox) {
            Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black.copy(alpha = 0.75f))
                .border(1.dp, highlightColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        } else {
            Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
        }

        Row(
            modifier = contentBoxModifier,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            displayWords.forEach { wordItem ->
                val isActive = currentOffsetSec >= wordItem.startOffsetSec && currentOffsetSec <= wordItem.endOffsetSec
                val animatedScale by animateFloatAsState(
                    targetValue = if (isActive) 1.15f else 1.0f,
                    animationSpec = spring(dampingRatio = 0.6f, stiffness = 800f),
                    label = "word_scale"
                )

                val wordText = if (style.isUppercase) wordItem.word.uppercase() else wordItem.word

                Text(
                    text = wordText,
                    fontSize = if (isActive) 19.sp else 17.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    color = if (isActive) highlightColor else primaryColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .scale(animatedScale)
                        .then(
                            if (isActive && style == SubtitleStyle.HORMOZI) {
                                Modifier.shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(4.dp),
                                    ambientColor = highlightColor,
                                    spotColor = highlightColor
                                )
                            } else Modifier
                        )
                )
            }
        }
    }
}
