package com.example.domain.model

enum class TargetPlatform(val displayName: String, val aspectRatio: String, val iconName: String, val maxSec: Int) {
    TIKTOK("TikTok", "9:16", "tiktok", 60),
    REELS("Instagram Reels", "9:16", "reels", 90),
    SHORTS("YouTube Shorts", "9:16", "shorts", 60)
}

enum class ClipDurationOption(val displayName: String, val targetSec: Int) {
    SEC_30("30s (Rapid Fire)", 30),
    SEC_60("60s (Deep Story)", 60),
    AUTO("Auto (AI Optimal)", 0)
}

enum class CropAlignmentMode(val displayName: String, val description: String) {
    CENTER_CROP("Smart Center", "Center frames the main action with 9:16 crop"),
    FACE_TRACKING("Face Tracking AI", "Keeps speaker face centered in vertical frame"),
    SPLIT_SCREEN("Split Screen (2x)", "Top: Speaker, Bottom: Gameplay / B-roll"),
    BLURRED_BG("Blurred Letterbox", "Original 16:9 padded with blurred video canvas")
}

enum class SubtitleStyle(
    val displayName: String,
    val primaryColorHex: Long,
    val highlightColorHex: Long,
    val hasBackgroundBox: Boolean,
    val isUppercase: Boolean
) {
    HORMOZI("Alex Hormozi", 0xFFFFFFFF, 0xFFFFE600, false, true),
    MR_BEAST("MrBeast Pop", 0xFF00E5FF, 0xFFFFF000, true, true),
    MINIMALIST("Minimal Clean", 0xFFFFFFFF, 0xFF67E8F9, true, false),
    NEON_GLOW("Neon Cyber", 0xFFEC4899, 0xFF00F0FF, false, true)
}

data class SubtitleWord(
    val word: String,
    val startOffsetSec: Float,
    val endOffsetSec: Float
)

data class ViralMoment(
    val id: String,
    val title: String,
    val hook: String,
    val startTimeSec: Float,
    val endTimeSec: Float,
    val viralityScore: Int, // 0 - 100
    val viralityCategory: String, // "High Emotional Hook", "Mind-Blowing", "Controversial Take", "Life Hack"
    val viralityReason: String,
    val hookStrengthScore: Int,
    val retentionScore: Int,
    val shareabilityScore: Int,
    val transcriptSnippet: String,
    val words: List<SubtitleWord> = emptyList(),
    val hashtags: List<String> = emptyList(),
    val aiCaption: String = ""
) {
    val durationSec: Float get() = (endTimeSec - startTimeSec).coerceAtLeast(1f)
    val formattedTimeRange: String get() = "${formatSec(startTimeSec)} - ${formatSec(endTimeSec)}"

    val viralityGrade: String get() = when {
        viralityScore >= 93 -> "S+ (Guaranteed Viral)"
        viralityScore >= 85 -> "A (High Engagement)"
        viralityScore >= 75 -> "B+ (Solid Performance)"
        else -> "B (Average Hook)"
    }

    companion object {
        fun formatSec(seconds: Float): String {
            val totalSec = seconds.toInt()
            val m = totalSec / 60
            val s = totalSec % 60
            return "%02d:%02d".format(m, s)
        }
    }
}
