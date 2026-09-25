package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ProjectEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.ViralClipUiState
import com.example.ui.viewmodel.ViralClipViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProjectsScreen(
    viewModel: ViralClipViewModel,
    uiState: ViralClipUiState,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredProjects = remember(uiState.savedProjects, searchQuery) {
        if (searchQuery.isBlank()) {
            uiState.savedProjects
        } else {
            uiState.savedProjects.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.targetPlatform.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Title
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "My Video Projects",
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Saved in Room Database • Revisit & re-export anytime",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search projects by name or platform...", color = TextMuted) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = ElectricCyan)
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted)
                        }
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
                    .testTag("project_search_field")
            )
        }

        // Project Items or Empty State
        if (filteredProjects.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderOpen,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Text(
                            text = if (searchQuery.isBlank()) "No saved projects yet" else "No matching projects",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "Paste a YouTube link or tap 'Generate Clips' to start.",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                        Button(
                            onClick = { viewModel.onTabChanged(AppTab.STUDIO) },
                            colors = ButtonDefaults.buttonColors(containerColor = ViralPurple),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Go to Studio")
                        }
                    }
                }
            }
        } else {
            items(filteredProjects) { project ->
                ProjectCard(
                    project = project,
                    onOpen = {
                        viewModel.onTabChanged(AppTab.CLIPS)
                    },
                    onDelete = {
                        viewModel.deleteProject(project.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun ProjectCard(
    project: ProjectEntity,
    onOpen: () -> Unit,
    onDelete: () -> Unit
) {
    val dateString = remember(project.createdAt) {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        sdf.format(Date(project.createdAt))
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkBorder)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail / Icon Box
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ViralPurple.copy(alpha = 0.2f))
                    .border(1.dp, ViralPurple.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SmartDisplay,
                    contentDescription = null,
                    tint = ViralPurpleLight,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Project Details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = project.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextPrimary,
                    maxLines = 1
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(ElectricCyan.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = project.targetPlatform,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan
                        )
                    }

                    Text(
                        text = "• ${project.clipsCount} clips",
                        fontSize = 11.sp,
                        color = TextMuted
                    )

                    Text(
                        text = "• $dateString",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            // Delete action
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete",
                    tint = TextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
