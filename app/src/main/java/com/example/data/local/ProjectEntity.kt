package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val title: String,
    val sourceUrl: String,
    val videoDurationSec: Int,
    val targetPlatform: String,
    val cropMode: String,
    val subtitleStyle: String,
    val createdAt: Long = System.currentTimeMillis(),
    val clipsCount: Int = 0,
    val thumbnailTitle: String = ""
)

@Entity(tableName = "clips")
data class ClipEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val title: String,
    val hook: String,
    val startTimeSec: Float,
    val endTimeSec: Float,
    val viralityScore: Int,
    val viralityCategory: String,
    val viralityReason: String,
    val hookStrengthScore: Int,
    val retentionScore: Int,
    val shareabilityScore: Int,
    val transcriptSnippet: String,
    val hashtags: String, // comma separated
    val aiCaption: String,
    val isExported: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
