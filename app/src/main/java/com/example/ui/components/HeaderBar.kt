package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun HeaderBar(
    onOpenProjects: () -> Unit,
    userStatus: String = "VIP Pro Unlimited",
    modifier: Modifier = Modifier
) {
    Surface(
        color = DarkSurface,
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = DarkBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Brand Logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(ViralPurple, ElectricCyan)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "ViralClip",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "AI",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = ElectricCyan
                        )
                    }
                    Text(
                        text = "16:9 ➔ 9:16 Shorts",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMuted
                    )
                }
            }

            // Right Action Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // My Projects button
                Surface(
                    onClick = onOpenProjects,
                    shape = RoundedCornerShape(20.dp),
                    color = DarkSurfaceVariant,
                    border = BorderStroke(1.dp, DarkBorder),
                    modifier = Modifier.testTag("my_projects_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = "My Projects",
                            tint = ElectricCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Projects",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                }

                // Permanent VIP Pro Unlimited Badge (No Paywall, 100% Free)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFFF59E0B),
                                    Color(0xFFD97706),
                                    Color(0xFF8B5CF6)
                                )
                            )
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFFDE68A).copy(alpha = 0.6f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 11.dp, vertical = 6.dp)
                        .testTag("vip_pro_badge")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = "VIP Status",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = userStatus,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}
