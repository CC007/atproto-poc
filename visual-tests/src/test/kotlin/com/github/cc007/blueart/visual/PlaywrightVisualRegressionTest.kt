package com.github.cc007.blueart.visual

import com.github.cc007.blueart.BlueArtApplication
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.test.context.SpringBootTest
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@SpringBootTest(
    classes = [BlueArtApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = ["server.port=8080"],
)
class PlaywrightVisualRegressionTest {
    private val baseUrl = "http://localhost:8080"

    @ParameterizedTest(name = "login route snapshot [{0}]")
    @EnumSource(VisualBrowser::class)
    fun loginRouteSnapshot(browser: VisualBrowser) {
        runVisualScenario(browser, "login-route") { page ->
            page.navigate("$baseUrl/login")
            page.waitForSelector("form[action='/login']")
            VisualTestRuntime.disableAnimations(page)
            VisualTestRuntime.waitForStableRendering(page)
            page shouldMatchSnapshot "login-route" forBrowser browser
        }
    }

    @ParameterizedTest(name = "browse route full page snapshot [{0}]")
    @EnumSource(VisualBrowser::class)
    fun browseFullPageSnapshot(browser: VisualBrowser) {
        runVisualScenario(browser, "browse-route") { page ->
            VisualTestRuntime.loginAsDummy(page, baseUrl)
            page.waitForSelector("section.feed-grid article.post-card")
            VisualTestRuntime.disableAnimations(page)
            VisualTestRuntime.waitForStableRendering(page)
            page shouldMatchSnapshot "browse-route" forBrowser browser
        }
    }

    @ParameterizedTest(name = "browse card {1} snapshot [{0}]")
    @MethodSource("browseCardCases")
    fun browseCardSnapshot(browser: VisualBrowser, cardName: String, cardIndex: Int) {
        runVisualScenario(browser, "browse-cards") { page ->
            VisualTestRuntime.loginAsDummy(page, baseUrl)
            page.waitForSelector("section.feed-grid article.post-card")
            VisualTestRuntime.disableAnimations(page)
            VisualTestRuntime.waitForStableRendering(page)
            val cards = page.locator("section.feed-grid > article.post-card")
            cards.nth(cardIndex) shouldMatchSnapshot "browse-card-$cardName" forBrowser browser
        }
    }

    companion object {
        // Card names match the dummy fixture suffixes in DummyAtProtoFixtures.kt.
        // Conversation replies are excluded because the browse route filters them out (reply == null).
        private val browseCardNames = listOf(
            "text",
            "image",
            "image-gallery",
            "video",
            "gif",
            "record-embed",
            "record-with-media",
            "unsupported-post",
            "conversation-root",
        )

        @JvmStatic
        fun browseCardCases(): List<Arguments> =
            VisualBrowser.entries.flatMap { browser ->
                browseCardNames.mapIndexed { index, name ->
                    Arguments.of(browser, name, index)
                }
            }
    }

    @ParameterizedTest(name = "art detail snapshot [{0}]")
    @EnumSource(VisualBrowser::class)
    fun artDetailSnapshot(browser: VisualBrowser) {
        runVisualScenario(browser, "art-detail-route") { page ->
            VisualTestRuntime.loginAsDummy(page, baseUrl)
            val uri = URLEncoder.encode(
                "at://dummy.localhost/app.bsky.feed.post/image-gallery",
                StandardCharsets.UTF_8,
            )
            page.navigate("$baseUrl/art/bafyreidummyimage-gallery?uri=$uri")
            page.waitForSelector(".art-image-grid")
            page.waitForSelector(".art-description")
            VisualTestRuntime.disableAnimations(page)
            VisualTestRuntime.waitForStableRendering(page)
            page shouldMatchSnapshot "art-detail-route" forBrowser browser
        }
    }

    private fun runVisualScenario(browserType: VisualBrowser, name: String, block: (Page) -> Unit) {
        Playwright.create().use { playwright ->
            browserType.launch(playwright).use { browser ->
                VisualTestRuntime.newContext(browser).use { context ->
                    val page = context.newPage()
                    try {
                        block(page)
                    } catch (ex: Exception) {
                        throw IllegalStateException("Visual scenario '$name' failed in ${browserType.id}", ex)
                    }
                }
            }
        }
    }
}
