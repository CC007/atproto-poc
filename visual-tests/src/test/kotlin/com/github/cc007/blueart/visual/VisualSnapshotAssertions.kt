package com.github.cc007.blueart.visual

import com.github.romankh3.image.comparison.ImageComparison
import com.github.romankh3.image.comparison.model.ImageComparisonState
import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import javax.imageio.ImageIO
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.writeBytes
import kotlin.test.fail

class VisualSnapshotAssertions {
    private val moduleDir: Path = Path.of(
        System.getProperty("blueart.visual.moduleDir")
            ?: fail("Missing system property blueart.visual.moduleDir")
    )
    private val updateBaselines = System.getProperty("blueart.visual.updateBaselines", "false").toBooleanStrictOrNull()
        ?: false
    private val baselineRoot: Path = moduleDir.resolve("src/test/resources/visual-baselines")
    private val reportsRoot: Path = moduleDir.resolve("build/reports/visual-regression")

    fun assertPageMatches(page: Page, browser: VisualBrowser, scenario: String) {
        assertMatches(browser, scenario, page.screenshot(Page.ScreenshotOptions().setFullPage(true)))
    }

    fun assertLocatorMatches(locator: Locator, browser: VisualBrowser, scenario: String) {
        assertMatches(browser, scenario, locator.screenshot())
    }

    private fun assertMatches(browser: VisualBrowser, scenario: String, actualBytes: ByteArray) {
        val expected = baselineRoot.resolve(browser.id).resolve("$scenario.png")
        val reportDir = reportsRoot.resolve(browser.id).resolve(scenario)
        val reportExpected = reportDir.resolve("expected.png")
        val reportActual = reportDir.resolve("actual.png")
        val reportDiff = reportDir.resolve("diff.png")

        Files.createDirectories(reportDir)

        reportActual.writeBytes(actualBytes)

        if (updateBaselines) {
            Files.createDirectories(expected.parent)
            Files.copy(reportActual, expected, StandardCopyOption.REPLACE_EXISTING)
            Files.copy(expected, reportExpected, StandardCopyOption.REPLACE_EXISTING)
            return
        }

        if (!expected.exists()) {
            fail(
                "Missing baseline for ${browser.id}/$scenario. " +
                    "Run ./gradlew updateVisualBaselines -PvisualBaselineApprover=<developer-name>."
            )
        }

        Files.copy(expected, reportExpected, StandardCopyOption.REPLACE_EXISTING)
        Files.deleteIfExists(reportDiff)

        val expectedImage = expected.inputStream().use(ImageIO::read)
        val actualImage = reportActual.inputStream().use(ImageIO::read)
        val result = ImageComparison(expectedImage, actualImage, reportDiff.toFile())
            .apply {
                setPixelToleranceLevel(5.0)
                setRectangleLineWidth(2)
                setAllowingPercentOfDifferentPixels(0.05)
            }
            .compareImages()

        if (result.imageComparisonState != ImageComparisonState.MATCH) {
            fail(
                "Visual mismatch for ${browser.id}/$scenario. " +
                    "Artifacts: expected=$reportExpected actual=$reportActual diff=$reportDiff"
            )
        }
    }
}
