package com.example.data.sample

import com.example.domain.model.SubtitleWord
import com.example.domain.model.ViralMoment

data class SamplePodcast(
    val id: String,
    val title: String,
    val host: String,
    val durationText: String,
    val durationSec: Int,
    val url: String,
    val category: String,
    val summary: String,
    val transcript: String,
    val sampleClips: List<ViralMoment>
)

object SampleData {

    val samplePodcasts: List<SamplePodcast> = listOf(
        SamplePodcast(
            id = "rogan_2154",
            title = "Joe Rogan #2154: Simulation Theory & Deep Oceans",
            host = "Joe Rogan & Lex Fridman",
            durationText = "2 hr 45 min",
            durationSec = 9900,
            url = "https://www.youtube.com/watch?v=sample_rogan_2154",
            category = "Sci-Fi & Mystery",
            summary = "Joe and Lex discuss underwater anomalies, computational universe theories, and why human senses filter out 99% of reality.",
            transcript = """
                Joe: Have you seen the new scans from the Mariana Trench? We know less about the bottom of our own oceans than we do about the surface of Mars. Think about that for a second.
                Lex: It is mathematically plausible that if an advanced intelligence wanted to observe a planetary civilization without interfering, the ocean floor is the optimal location. Extreme pressure, zero sunlight, inaccessible to human radar.
                Joe: That gives me chills man. If you're talking about millions of years of technological evolution, they wouldn't build metal tin cans. They would manipulate physics itself.
                Lex: And that leads directly to Nick Bostrom's simulation argument. If consciousness can be computed, the probability that we are in base reality is practically zero.
            """.trimIndent(),
            sampleClips = listOf(
                ViralMoment(
                    id = "clip_rogan_1",
                    title = "Why Alien Civilizations Would Live in Deep Oceans 🌊",
                    hook = "We know less about our own oceans than the surface of Mars.",
                    startTimeSec = 0f,
                    endTimeSec = 28f,
                    viralityScore = 97,
                    viralityCategory = "Mind-Blowing Fact",
                    viralityReason = "Explosive opening contrast hook comparing ocean depth to Mars, followed by high-retention physics argument.",
                    hookStrengthScore = 98,
                    retentionScore = 95,
                    shareabilityScore = 99,
                    transcriptSnippet = "We know less about the bottom of our own oceans than we do about Mars. Think about that for a second.",
                    words = listOf(
                        SubtitleWord("WE", 0.0f, 0.4f),
                        SubtitleWord("KNOW", 0.4f, 0.8f),
                        SubtitleWord("LESS", 0.8f, 1.2f),
                        SubtitleWord("ABOUT", 1.2f, 1.6f),
                        SubtitleWord("OUR", 1.6f, 1.9f),
                        SubtitleWord("OWN", 1.9f, 2.3f),
                        SubtitleWord("OCEANS", 2.3f, 3.1f),
                        SubtitleWord("THAN", 3.1f, 3.5f),
                        SubtitleWord("MARS!", 3.5f, 4.4f),
                        SubtitleWord("IF", 4.8f, 5.2f),
                        SubtitleWord("AN", 5.2f, 5.5f),
                        SubtitleWord("ADVANCED", 5.5f, 6.2f),
                        SubtitleWord("INTELLIGENCE", 6.2f, 7.3f),
                        SubtitleWord("WANTED", 7.3f, 7.9f),
                        SubtitleWord("TO", 7.9f, 8.2f),
                        SubtitleWord("OBSERVE", 8.2f, 9.0f),
                        SubtitleWord("US,", 9.0f, 9.5f),
                        SubtitleWord("THE", 9.8f, 10.1f),
                        SubtitleWord("OCEAN", 10.1f, 10.7f),
                        SubtitleWord("FLOOR", 10.7f, 11.4f),
                        SubtitleWord("IS", 11.4f, 11.7f),
                        SubtitleWord("OPTIMAL.", 11.7f, 12.8f),
                        SubtitleWord("ZERO", 13.2f, 13.8f),
                        SubtitleWord("SUNLIGHT.", 13.8f, 14.8f),
                        SubtitleWord("ZERO", 15.1f, 15.6f),
                        SubtitleWord("RADAR.", 15.6f, 16.6f),
                        SubtitleWord("THEY", 17.0f, 17.4f),
                        SubtitleWord("WOULD", 17.4f, 17.8f),
                        SubtitleWord("MANIPULATE", 17.8f, 18.9f),
                        SubtitleWord("PHYSICS", 18.9f, 19.8f),
                        SubtitleWord("ITSELF!", 19.8f, 21.0f)
                    ),
                    hashtags = listOf("#joerogan", "#simulationtheory", "#mystery", "#podcastclips", "#mindblown"),
                    aiCaption = "The math behind deep ocean civilization theory will blow your mind 🤯 Watch till the end. #joerogan #jre"
                ),
                ViralMoment(
                    id = "clip_rogan_2",
                    title = "The Terrifying Probability We Are in a Simulation 💻",
                    hook = "The chance we are in base reality is practically zero.",
                    startTimeSec = 28f,
                    endTimeSec = 54f,
                    viralityScore = 93,
                    viralityCategory = "Existential Shock",
                    viralityReason = "Direct philosophical challenge causing viewers to pause, re-watch, and debate in comments.",
                    hookStrengthScore = 94,
                    retentionScore = 92,
                    shareabilityScore = 95,
                    transcriptSnippet = "If consciousness can be computed, the probability that we are in base reality is practically zero.",
                    words = listOf(
                        SubtitleWord("THE", 0.0f, 0.3f),
                        SubtitleWord("PROBABILITY", 0.3f, 1.2f),
                        SubtitleWord("THAT", 1.2f, 1.5f),
                        SubtitleWord("WE", 1.5f, 1.8f),
                        SubtitleWord("ARE", 1.8f, 2.1f),
                        SubtitleWord("IN", 2.1f, 2.3f),
                        SubtitleWord("BASE", 2.3f, 2.8f),
                        SubtitleWord("REALITY", 2.8f, 3.6f),
                        SubtitleWord("IS", 3.6f, 4.0f),
                        SubtitleWord("PRACTICALLY", 4.0f, 4.9f),
                        SubtitleWord("ZERO.", 4.9f, 5.8f),
                        SubtitleWord("THINK", 6.2f, 6.7f),
                        SubtitleWord("ABOUT", 6.7f, 7.2f),
                        SubtitleWord("COMPUTATIONAL", 7.2f, 8.3f),
                        SubtitleWord("POWER", 8.3f, 9.0f),
                        SubtitleWord("IN", 9.0f, 9.3f),
                        SubtitleWord("ONE", 9.3f, 9.7f),
                        SubtitleWord("THOUSAND", 9.7f, 10.6f),
                        SubtitleWord("YEARS.", 10.6f, 11.5f)
                    ),
                    hashtags = listOf("#simulation", "#matrix", "#philosophy", "#futuretech", "#lexfridman"),
                    aiCaption = "Are we living inside someone else's supercomputer? Nick Bostrom's equation explained. #simulation"
                )
            )
        ),
        SamplePodcast(
            id = "hormozi_leads",
            title = "Alex Hormozi: $100M Secret Most Entrepreneurs Miss",
            host = "Alex Hormozi",
            durationText = "1 hr 12 min",
            durationSec = 4320,
            url = "https://www.youtube.com/watch?v=sample_hormozi_leads",
            category = "Business & Wealth",
            summary = "Alex breaks down volume-based outreach, why 'more' always beats 'better' initially, and the rule of 100.",
            transcript = """
                Most people don't fail in business because their product sucks. They fail because nobody knows they exist.
                You want to make a million dollars this year? Here is the brutal math: You need 100 leads a day. Every single day. Without skipping a weekend.
                If you reach out to 100 people a day for 100 days straight, it is statistically impossible not to get clients.
                People spend 6 months perfecting a logo that nobody will ever look at, instead of sending 100 DMs before lunch.
            """.trimIndent(),
            sampleClips = listOf(
                ViralMoment(
                    id = "clip_hormozi_1",
                    title = "The Brutal Math of Making $1,000,000 📈",
                    hook = "Nobody knows you exist. That is why you're broke.",
                    startTimeSec = 0f,
                    endTimeSec = 25f,
                    viralityScore = 96,
                    viralityCategory = "Actionable Life Hack",
                    viralityReason = "Aggressive wake-up call hook followed by simple rule-of-100 arithmetic that drives high bookmarking.",
                    hookStrengthScore = 99,
                    retentionScore = 94,
                    shareabilityScore = 96,
                    transcriptSnippet = "Most people fail because nobody knows they exist. You need 100 leads a day, every single day.",
                    words = listOf(
                        SubtitleWord("NOBODY", 0.0f, 0.6f),
                        SubtitleWord("KNOWS", 0.6f, 1.1f),
                        SubtitleWord("YOU", 1.1f, 1.4f),
                        SubtitleWord("EXIST!", 1.4f, 2.2f),
                        SubtitleWord("THAT", 2.5f, 2.8f),
                        SubtitleWord("IS", 2.8f, 3.1f),
                        SubtitleWord("WHY", 3.1f, 3.4f),
                        SubtitleWord("YOU", 3.4f, 3.7f),
                        SubtitleWord("HAVE", 3.7f, 4.0f),
                        SubtitleWord("NO", 4.0f, 4.4f),
                        SubtitleWord("SALES.", 4.4f, 5.2f),
                        SubtitleWord("THE", 5.6f, 5.9f),
                        SubtitleWord("RULE", 5.9f, 6.4f),
                        SubtitleWord("OF", 6.4f, 6.7f),
                        SubtitleWord("100:", 6.7f, 7.5f),
                        SubtitleWord("100", 7.9f, 8.5f),
                        SubtitleWord("LEADS", 8.5f, 9.2f),
                        SubtitleWord("A", 9.2f, 9.5f),
                        SubtitleWord("DAY.", 9.5f, 10.3f),
                        SubtitleWord("EVERY.", 10.7f, 11.4f),
                        SubtitleWord("SINGLE.", 11.4f, 12.1f),
                        SubtitleWord("DAY.", 12.1f, 12.9f),
                        SubtitleWord("STOP", 13.3f, 13.8f),
                        SubtitleWord("MAKING", 13.8f, 14.5f),
                        SubtitleWord("LOGOS.", 14.5f, 15.5f)
                    ),
                    hashtags = listOf("#alexhormozi", "#business", "#entrepreneur", "#money", "#growth"),
                    aiCaption = "The Rule of 100 will change your business forever. Stop overthinking and start doing the reps. #hormozi"
                )
            )
        ),
        SamplePodcast(
            id = "huberman_dopamine",
            title = "Andrew Huberman: Resetting Your Dopamine Baseline",
            host = "Dr. Andrew Huberman",
            durationText = "2 hr 10 min",
            durationSec = 7800,
            url = "https://www.youtube.com/watch?v=sample_huberman_dopamine",
            category = "Health & Science",
            summary = "Neuroscience protocols for morning sunlight, delaying caffeine by 90 minutes, and avoiding dopamine crashes.",
            transcript = """
                If you drink coffee within 60 minutes of waking up, you are almost guaranteeing an afternoon energy crash at 2 PM.
                Here is the biology: In the morning, adenosine is naturally being cleared from your brain. Caffeine doesn't eliminate adenosine, it merely parks in the receptor.
                When the caffeine wears off around 2 PM, that backlog of adenosine floods your system and you feel like you got hit by a truck.
                Delay your first caffeine intake by 90 to 120 minutes and view morning sunlight. Your baseline energy will double.
            """.trimIndent(),
            sampleClips = listOf(
                ViralMoment(
                    id = "clip_huberman_1",
                    title = "Why Coffee at 7 AM Is Ruining Your Day ☕",
                    hook = "Drinking coffee immediately when you wake up is a trap.",
                    startTimeSec = 0f,
                    endTimeSec = 26f,
                    viralityScore = 95,
                    viralityCategory = "Health Hack & Protocol",
                    viralityReason = "Attacks a universal habit with clear, relatable biology and an actionable solution.",
                    hookStrengthScore = 97,
                    retentionScore = 96,
                    shareabilityScore = 93,
                    transcriptSnippet = "If you drink coffee within 60 minutes of waking up, you guarantee an afternoon crash.",
                    words = listOf(
                        SubtitleWord("STOP", 0.0f, 0.5f),
                        SubtitleWord("DRINKING", 0.5f, 1.2f),
                        SubtitleWord("COFFEE", 1.2f, 1.9f),
                        SubtitleWord("THE", 1.9f, 2.2f),
                        SubtitleWord("SECOND", 2.2f, 2.9f),
                        SubtitleWord("YOU", 2.9f, 3.2f),
                        SubtitleWord("WAKE", 3.2f, 3.7f),
                        SubtitleWord("UP!", 3.7f, 4.4f),
                        SubtitleWord("YOU", 4.8f, 5.1f),
                        SubtitleWord("ARE", 5.1f, 5.4f),
                        SubtitleWord("GUARANTEEING", 5.4f, 6.5f),
                        SubtitleWord("A", 6.5f, 6.7f),
                        SubtitleWord("CRASH", 6.7f, 7.5f),
                        SubtitleWord("AT", 7.5f, 7.8f),
                        SubtitleWord("2", 7.8f, 8.3f),
                        SubtitleWord("PM.", 8.3f, 9.1f),
                        SubtitleWord("DELAY", 9.5f, 10.1f),
                        SubtitleWord("CAFFEINE", 10.1f, 11.0f),
                        SubtitleWord("BY", 11.0f, 11.3f),
                        SubtitleWord("90", 11.3f, 12.0f),
                        SubtitleWord("MINUTES.", 12.0f, 13.0f),
                        SubtitleWord("LET", 13.4f, 13.8f),
                        SubtitleWord("ADENOSINE", 13.8f, 14.9f),
                        SubtitleWord("CLEAR", 14.9f, 15.6f),
                        SubtitleWord("NATURALLY.", 15.6f, 16.8f)
                    ),
                    hashtags = listOf("#hubermanlab", "#biohacking", "#productivity", "#morningroutine", "#health"),
                    aiCaption = "Why you feel exhausted at 2 PM every day explained by Stanford neuroscientist Dr. Huberman ☕"
                )
            )
        )
    )
}
