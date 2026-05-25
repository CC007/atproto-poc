package com.github.cc007.blueart.endpoints.browse

import com.github.cc007.blueart.components.overview.postSummary
import com.github.cc007.blueart.components.topBanner
import com.github.cc007.blueart.endpoints.auth.AtProtoAuthentication
import com.github.cc007.blueart.kolostyles.render.*
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import kotlinx.html.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import work.socialhub.kbsky.BlueskyFactory
import work.socialhub.kbsky.api.app.bsky.FeedResource
import work.socialhub.kbsky.api.entity.app.bsky.feed.FeedGetTimelineRequest
import work.socialhub.kbsky.api.entity.app.bsky.feed.FeedGetTimelineResponse
import work.socialhub.kbsky.auth.BearerTokenAuthProvider
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsFeedViewPost

val logger = KotlinLogging.logger {}
private val prettyPrinter = Json { prettyPrint = true }
fun String.pretty(): String {
    val jsonElement: JsonElement = Json.parseToJsonElement(this)
    return prettyPrinter.encodeToString(JsonElement.serializer(), jsonElement)
}


@Controller
class BrowseController {

    @GetMapping("/browse", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun browseEndpoint(
        request: HttpServletRequest
    ): String {
        val csrfToken = (request.getAttribute("_csrf") as? CsrfToken)?.token

        return renderKoloHtml {
            head {
                title("Browse")
                meta(name = "viewport", content = "width=device-width, initial-scale=1")
                link(rel = "stylesheet", href = "/css/generated/browse.css")
                koloStylesheetLink()
            }
            body(classes = "browse-body") {
                kolo { m(0) }
                topBanner(csrfToken)
                main(classes = "browse-layout") {
                    kolo { p(4) }
                    aside(classes = "browse-sidebar") {
                    h2(classes = "sidebar-title") {
                                kolo { m(0); mb(3) }
                                +"BlueArt"
                            }
                            nav(classes = "sidebar-nav") {
                                a(href = "#") { +"Discover" }
                                a(href = "#") { +"Following" }
                                a(href = "#") { +"Traditional" }
                                a(href = "#") { +"Digital" }
                                a(href = "#") { +"Photography" }
                            }
                        }
                    section(classes = "browse-content") {
                        div(classes = "content-top") {

                            h1 {
                                kolo { m(0) }
                                +"Browse Timeline"
                            }
                            div(classes = "filter-row") {
                                button(classes = "filter-chip filter-chip-active") { +"Hot" }
                                button(classes = "filter-chip") { +"New" }
                                button(classes = "filter-chip") { +"Artists" }
                                button(classes = "filter-chip") { +"Commissions" }
                            }
                        }
                        section(classes = "feed-grid") {
                            getTimelineFeed()
                                .filter { it.reply == null }
                                .forEach {
                                    postSummary(it.post, it.reply?.parent)
                                }
                        }
                    }
                }
            }
        }
    }
}

private fun getTimelineFeed(
): List<FeedDefsFeedViewPost> {
    return with(SecurityContextHolder.getContext().authentication as AtProtoAuthentication) {
        val auth = BearerTokenAuthProvider(accessToken)
        val feed = BlueskyFactory
            .instance()
            .feed()
        val timeline1 = getTimeline(feed, auth, null)
        val timelineFeed = timeline1.feed.toMutableList()
        val timeline2 = getTimeline(feed, auth, timeline1.cursor)
        timelineFeed += timeline2.feed
        val timeline3 = getTimeline(feed, auth, timeline2.cursor)
        timelineFeed += timeline3.feed
        val timeline4 = getTimeline(feed, auth, timeline3.cursor)
        timelineFeed += timeline4.feed
        timelineFeed
    }
}

private fun getTimeline(
    feed: FeedResource,
    auth: BearerTokenAuthProvider,
    cursor: String?
): FeedGetTimelineResponse = feed.getTimelineBlocking(FeedGetTimelineRequest(auth, cursor = cursor)).also {
    logger.info { it.json.pretty() }
    logger.info { "Cursor: ${it.data.cursor}" }
}.data

