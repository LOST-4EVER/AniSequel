package com.example

import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * App Studio metadata must describe capabilities the app actually implements.
 *
 * The Gemini capability flag prompted the platform to treat AniSequel as using
 * server-side AI even though there is no Gemini/Firebase integration. Keeping
 * this check at the metadata boundary prevents a listing/configuration claim
 * from silently drifting away from the app.
 */
class MetadataCapabilityTest {

    @Test
    fun `metadata does not claim server-side Gemini without an implementation`() {
        val metadata = File(findProjectRoot(), "metadata.json").readText()

        assertTrue(
            "metadata.json must not advertise server-side Gemini; this app has no " +
                "Gemini integration and the false declaration can mislead platform tooling",
            !metadata.contains("MAJOR_CAPABILITY_SERVER_SIDE_GEMINI_API")
        )
    }

    private fun findProjectRoot(): File {
        var candidate: File? = File(System.getProperty("user.dir") ?: ".")
        while (candidate != null) {
            if (File(candidate, "gradle.properties").isFile) return candidate
            candidate = candidate.parentFile
        }
        error("could not locate gradle.properties")
    }
}
