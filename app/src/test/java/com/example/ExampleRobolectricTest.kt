package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.domain.model.SubtitleStyle
import com.example.domain.model.TargetPlatform
import com.example.domain.model.ViralMoment
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app_name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("ViralClip AI", appName)
    }

    @Test
    fun `verify viral moment calculations`() {
        val moment = ViralMoment(
            id = "test_1",
            title = "Test Viral Moment",
            hook = "Stop doing this now",
            startTimeSec = 10f,
            endTimeSec = 40f,
            viralityScore = 95,
            viralityCategory = "Mind-Blowing",
            viralityReason = "Curiosity gap",
            hookStrengthScore = 98,
            retentionScore = 94,
            shareabilityScore = 95,
            transcriptSnippet = "Stop doing this now"
        )

        assertEquals(30f, moment.durationSec)
        assertEquals("00:10 - 00:40", moment.formattedTimeRange)
        assertEquals("S+ (Guaranteed Viral)", moment.viralityGrade)
    }

    @Test
    fun `verify platform and style presets`() {
        assertEquals("9:16", TargetPlatform.TIKTOK.aspectRatio)
        assertEquals("Alex Hormozi", SubtitleStyle.HORMOZI.displayName)
    }
}
