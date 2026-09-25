package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.HeaderBar
import com.example.ui.screens.ClipsResultScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.StudioScreen
import com.example.ui.screens.TemplatesScreen
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.ViralClipViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ViralClipApp()
            }
        }
    }
}

data class NavItem(
    val tab: AppTab,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun ViralClipApp(
    viewModel: ViralClipViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val navItems = listOf(
        NavItem(AppTab.STUDIO, "Studio", Icons.Filled.AddCircle, Icons.Outlined.AddCircleOutline, "nav_studio"),
        NavItem(AppTab.CLIPS, "Clips", Icons.Filled.MovieFilter, Icons.Outlined.MovieFilter, "nav_clips"),
        NavItem(AppTab.PROJECTS, "Projects", Icons.Filled.Folder, Icons.Outlined.Folder, "nav_projects"),
        NavItem(AppTab.TEMPLATES, "Styles", Icons.Filled.Style, Icons.Outlined.Style, "nav_templates")
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        topBar = {
            HeaderBar(
                onOpenProjects = { viewModel.onTabChanged(AppTab.PROJECTS) },
                userStatus = uiState.userPlanStatus
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                tonalElevation = 0.dp,
                modifier = Modifier
                    .border(width = 1.dp, color = DarkBorder)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                navItems.forEach { item ->
                    val isSelected = uiState.activeTab == item.tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.onTabChanged(item.tab) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ViralPurpleLight,
                            selectedTextColor = ViralPurpleLight,
                            indicatorColor = ViralPurple.copy(alpha = 0.2f),
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag(item.testTag)
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBackground)
        ) {
            when (uiState.activeTab) {
                AppTab.STUDIO -> StudioScreen(viewModel = viewModel, uiState = uiState)
                AppTab.CLIPS -> ClipsResultScreen(viewModel = viewModel, uiState = uiState)
                AppTab.PROJECTS -> ProjectsScreen(viewModel = viewModel, uiState = uiState)
                AppTab.TEMPLATES -> TemplatesScreen(viewModel = viewModel, uiState = uiState)
            }
        }
    }
}
