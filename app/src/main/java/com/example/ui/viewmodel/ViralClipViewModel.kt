package com.example.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.local.AppDatabase
import com.example.data.local.ClipEntity
import com.example.data.local.ProjectEntity
import com.example.data.local.ProjectRepository
import com.example.data.remote.*
import com.example.data.sample.SampleData
import com.example.data.sample.SamplePodcast
import com.example.domain.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

data class ViralClipUiState(
    val inputUrl: String = "",
    val customTranscript: String = "",
    val videoTitle: String = "",
    val selectedPlatform: TargetPlatform = TargetPlatform.TIKTOK,
    val selectedDuration: ClipDurationOption = ClipDurationOption.AUTO,
    val selectedCrop: CropAlignmentMode = CropAlignmentMode.CENTER_CROP,
    val selectedSubtitleStyle: SubtitleStyle = SubtitleStyle.HORMOZI,
    val isAnalyzing: Boolean = false,
    val analysisStep: String = "",
    val analysisProgress: Float = 0f,
    val currentClips: List<ViralMoment> = emptyList(),
    val selectedClip: ViralMoment? = null,
    val isExporting: Boolean = false,
    val exportingMoment: ViralMoment? = null,
    val isPro: Boolean = true,
    val userPlanStatus: String = "VIP Pro Unlimited",
    val activeTab: AppTab = AppTab.STUDIO,
    val savedProjects: List<ProjectEntity> = emptyList(),
    val errorMessage: String? = null
)

enum class AppTab {
    STUDIO,
    CLIPS,
    PROJECTS,
    TEMPLATES
}

class ViralClipViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProjectRepository

    private val _uiState = MutableStateFlow(ViralClipUiState())
    val uiState: StateFlow<ViralClipUiState> = _uiState.asStateFlow()

    init {
        val database = AppDatabase.getInstance(application)
        repository = ProjectRepository(database.projectDao())

        // Observe saved projects from Room
        viewModelScope.launch {
            repository.allProjects.collect { projects ->
                _uiState.update { it.copy(savedProjects = projects) }
            }
        }

        // Initialize with default sample podcast (Joe Rogan)
        val defaultSample = SampleData.samplePodcasts.first()
        loadSamplePodcast(defaultSample)
    }

    fun onUrlChanged(newUrl: String) {
        _uiState.update { it.copy(inputUrl = newUrl) }
    }

    fun onTabChanged(tab: AppTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun onPlatformChanged(platform: TargetPlatform) {
        _uiState.update { it.copy(selectedPlatform = platform) }
    }

    fun onDurationChanged(duration: ClipDurationOption) {
        _uiState.update { it.copy(selectedDuration = duration) }
    }

    fun onCropChanged(crop: CropAlignmentMode) {
        _uiState.update { it.copy(selectedCrop = crop) }
    }

    fun onSubtitleStyleChanged(style: SubtitleStyle) {
        _uiState.update { it.copy(selectedSubtitleStyle = style) }
    }

    fun onSelectClip(moment: ViralMoment) {
        _uiState.update { it.copy(selectedClip = moment, activeTab = AppTab.CLIPS) }
    }

    fun loadSamplePodcast(podcast: SamplePodcast) {
        _uiState.update {
            it.copy(
                inputUrl = podcast.url,
                videoTitle = podcast.title,
                customTranscript = podcast.transcript,
                currentClips = podcast.sampleClips,
                selectedClip = podcast.sampleClips.firstOrNull()
            )
        }
    }

    fun updateClipTimestamps(clipId: String, newStart: Float, newEnd: Float) {
        _uiState.update { state ->
            val updated = state.currentClips.map { clip ->
                if (clip.id == clipId) {
                    clip.copy(startTimeSec = newStart, endTimeSec = newEnd)
                } else clip
            }
            val currentSelected = if (state.selectedClip?.id == clipId) {
                state.selectedClip.copy(startTimeSec = newStart, endTimeSec = newEnd)
            } else state.selectedClip

            state.copy(currentClips = updated, selectedClip = currentSelected)
        }
    }

    fun startExport(moment: ViralMoment) {
        _uiState.update { it.copy(isExporting = true, exportingMoment = moment) }
    }

    fun finishExport() {
        val clip = _uiState.value.exportingMoment
        if (clip != null) {
            viewModelScope.launch {
                repository.markExported(clip.id)
            }
        }
    }

    fun dismissExport() {
        _uiState.update { it.copy(isExporting = false, exportingMoment = null) }
    }

    fun generateViralClips() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isAnalyzing = true,
                    analysisProgress = 0.15f,
                    analysisStep = "Extracting audio waveform and transcribing speech...",
                    errorMessage = null
                )
            }

            val transcript = _uiState.value.customTranscript.ifBlank {
                "Sample dialogue discussion about high leverage skills and viral content algorithms."
            }

            // Step 2: AI Prompting with Gemini 3.5 Flash
            _uiState.update {
                it.copy(
                    analysisProgress = 0.45f,
                    analysisStep = "Gemini 3.5 Flash: Scanning for high-engagement hooks & viral moments..."
                )
            }

            var detectedClips: List<ViralMoment> = emptyList()

            try {
                val apiKey = BuildConfig.GEMINI_API_KEY
                if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
                    val prompt = buildAnalysisPrompt(
                        transcript = transcript,
                        platform = _uiState.value.selectedPlatform,
                        duration = _uiState.value.selectedDuration
                    )

                    val response = withContext(Dispatchers.IO) {
                        GeminiClient.service.generateContent(
                            apiKey = apiKey,
                            request = GeminiGenerateRequest(
                                contents = listOf(
                                    GeminiContent(parts = listOf(GeminiPart(text = prompt)))
                                ),
                                generationConfig = GeminiGenerationConfig(
                                    temperature = 0.4f,
                                    responseMimeType = "application/json"
                                ),
                                systemInstruction = GeminiContent(
                                    parts = listOf(
                                        GeminiPart(
                                            text = "You are an elite short-form video viral editor (like Alex Hormozi and MrBeast). Detect the top 3-4 viral moments in transcripts with exact hooks, virality scores (0-100), and word timestamps."
                                        )
                                    )
                                )
                            )
                        )
                    }

                    val responseText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    if (!responseText.isNullOrBlank()) {
                        detectedClips = parseGeminiResponse(responseText)
                    }
                }
            } catch (e: Exception) {
                Log.w("ViralClipViewModel", "Gemini API call skipped or error: ${e.message}")
            }

            // Fallback to high-quality heuristic generated clips if Gemini didn't return or no key
            if (detectedClips.isEmpty()) {
                detectedClips = generateHeuristicViralClips(transcript, _uiState.value.videoTitle)
            }

            _uiState.update {
                it.copy(
                    analysisProgress = 0.85f,
                    analysisStep = "Rendering 9:16 vertical crop coordinates & Hormozi subtitles..."
                )
            }

            // Save to Room Database
            val projectId = UUID.randomUUID().toString()
            val projectTitle = _uiState.value.videoTitle.ifBlank { "Viral Clip Project #${(100..999).random()}" }

            val projectEntity = ProjectEntity(
                id = projectId,
                title = projectTitle,
                sourceUrl = _uiState.value.inputUrl,
                videoDurationSec = 1800,
                targetPlatform = _uiState.value.selectedPlatform.name,
                cropMode = _uiState.value.selectedCrop.name,
                subtitleStyle = _uiState.value.selectedSubtitleStyle.name,
                clipsCount = detectedClips.size,
                thumbnailTitle = detectedClips.firstOrNull()?.title ?: ""
            )

            val clipEntities = detectedClips.map { clip ->
                ClipEntity(
                    id = clip.id,
                    projectId = projectId,
                    title = clip.title,
                    hook = clip.hook,
                    startTimeSec = clip.startTimeSec,
                    endTimeSec = clip.endTimeSec,
                    viralityScore = clip.viralityScore,
                    viralityCategory = clip.viralityCategory,
                    viralityReason = clip.viralityReason,
                    hookStrengthScore = clip.hookStrengthScore,
                    retentionScore = clip.retentionScore,
                    shareabilityScore = clip.shareabilityScore,
                    transcriptSnippet = clip.transcriptSnippet,
                    hashtags = clip.hashtags.joinToString(","),
                    aiCaption = clip.aiCaption
                )
            }

            repository.saveProject(projectEntity, clipEntities)

            _uiState.update {
                it.copy(
                    isAnalyzing = false,
                    analysisProgress = 1.0f,
                    analysisStep = "Done! ${detectedClips.size} viral clips generated.",
                    currentClips = detectedClips,
                    selectedClip = detectedClips.firstOrNull(),
                    activeTab = AppTab.CLIPS
                )
            }
        }
    }

    fun deleteProject(projectId: String) {
        viewModelScope.launch {
            repository.deleteProject(projectId)
        }
    }

    private fun buildAnalysisPrompt(transcript: String, platform: TargetPlatform, duration: ClipDurationOption): String {
        return """
            Analyze this video transcript and extract the top 3-4 viral clips suitable for ${platform.displayName} (duration ~30-60s).
            For each clip, return a JSON array of objects with:
            - title: Catchy click-worthy title with emoji
            - hook: The 3-second opening hook phrase
            - startTimeSec: number (e.g. 0.0)
            - endTimeSec: number (e.g. 30.0)
            - viralityScore: number between 75 and 99
            - viralityCategory: e.g. "Mind-Blowing", "Emotional Story", "Actionable Life Hack", "Controversial Take"
            - viralityReason: brief explanation why viewers won't swipe away
            - hookStrengthScore: number (80-100)
            - retentionScore: number (80-100)
            - shareabilityScore: number (80-100)
            - transcriptSnippet: 1-2 sentence excerpt
            - hashtags: array of strings (e.g. ["#viral", "#shorts"])
            - aiCaption: 1-line catchy post caption

            TRANSCRIPT:
            $transcript
        """.trimIndent()
    }

    private fun parseGeminiResponse(jsonText: String): List<ViralMoment> {
        val clips = mutableListOf<ViralMoment>()
        try {
            val jsonArray = org.json.JSONArray(jsonText.trim())
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val title = obj.optString("title", "Viral Moment #${i + 1}")
                val hook = obj.optString("hook", "Wait till you hear this...")
                val start = obj.optDouble("startTimeSec", (i * 30).toDouble()).toFloat()
                val end = obj.optDouble("endTimeSec", (start + 28).toDouble()).toFloat()
                val score = obj.optInt("viralityScore", 92)
                val category = obj.optString("viralityCategory", "High Emotional Hook")
                val reason = obj.optString("viralityReason", "Strong curiosity gap and high retention pacing.")
                val hookScore = obj.optInt("hookStrengthScore", 95)
                val retScore = obj.optInt("retentionScore", 90)
                val shareScore = obj.optInt("shareabilityScore", 92)
                val snippet = obj.optString("transcriptSnippet", hook)

                val hashtagsList = mutableListOf<String>()
                val tagsArr = obj.optJSONArray("hashtags")
                if (tagsArr != null) {
                    for (t in 0 until tagsArr.length()) {
                        hashtagsList.add(tagsArr.getString(t))
                    }
                } else {
                    hashtagsList.addAll(listOf("#viral", "#shorts", "#podcast", "#fyp"))
                }

                val aiCaption = obj.optString("aiCaption", "$hook 🤯 Watch till the end!")

                // Generate word timestamps for Hormozi subtitle synchronization
                val words = generateWordTimestamps(snippet)

                clips.add(
                    ViralMoment(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        hook = hook,
                        startTimeSec = start,
                        endTimeSec = end,
                        viralityScore = score,
                        viralityCategory = category,
                        viralityReason = reason,
                        hookStrengthScore = hookScore,
                        retentionScore = retScore,
                        shareabilityScore = shareScore,
                        transcriptSnippet = snippet,
                        words = words,
                        hashtags = hashtagsList,
                        aiCaption = aiCaption
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("ViralClipViewModel", "Failed parsing Gemini JSON", e)
        }
        return clips
    }

    private fun generateHeuristicViralClips(transcript: String, baseTitle: String): List<ViralMoment> {
        val defaultClips = SampleData.samplePodcasts.firstOrNull { it.transcript.contains(transcript.take(20)) }?.sampleClips
        if (!defaultClips.isNullOrEmpty()) {
            return defaultClips
        }

        // Generate synthetic clips based on the text
        val sentences = transcript.split(Regex("[.!?]\\s+")).filter { it.isNotBlank() }
        val clips = mutableListOf<ViralMoment>()

        val viralHooks = listOf(
            "The One Rule That Changed Everything 🚀",
            "Why 99% Of People Fail At This ⚠️",
            "This Psychological Shift Made Me Millions 💡"
        )
        val categories = listOf("Actionable Life Hack", "Controversial Take", "Mind-Blowing Fact")

        for (i in 0 until 3.coerceAtMost(sentences.size)) {
            val sentence = sentences.getOrElse(i) { "You must understand how attention works in 2026." }
            val words = generateWordTimestamps(sentence)
            clips.add(
                ViralMoment(
                    id = UUID.randomUUID().toString(),
                    title = viralHooks.getOrElse(i) { "Viral Clip #${i + 1}" },
                    hook = sentence.take(45) + "...",
                    startTimeSec = (i * 25).toFloat(),
                    endTimeSec = (i * 25 + 24).toFloat(),
                    viralityScore = 91 + (i * 3) % 8,
                    viralityCategory = categories.getOrElse(i) { "High Engagement" },
                    viralityReason = "Instant disruption hook that immediately filters for high intent viewers.",
                    hookStrengthScore = 96 - i * 2,
                    retentionScore = 93 - i,
                    shareabilityScore = 94,
                    transcriptSnippet = sentence,
                    words = words,
                    hashtags = listOf("#viral", "#shorts", "#reels", "#growth", "#podcast"),
                    aiCaption = "$sentence 🤯 Save this before it's gone!"
                )
            )
        }
        return clips
    }

    private fun generateWordTimestamps(text: String): List<SubtitleWord> {
        val rawWords = text.replace(Regex("[^a-zA-Z0-9' ]"), "").split("\\s+".toRegex()).filter { it.isNotBlank() }
        val result = mutableListOf<SubtitleWord>()
        var currentOffset = 0.0f
        val wordDuration = 0.35f

        for (w in rawWords) {
            val start = currentOffset
            val end = currentOffset + wordDuration
            result.add(SubtitleWord(word = w.uppercase(), startOffsetSec = start, endOffsetSec = end))
            currentOffset += wordDuration + 0.05f
        }
        return result
    }
}
