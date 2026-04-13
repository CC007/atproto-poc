package com.github.cc007.poc.atproto.browse

import com.github.cc007.poc.atproto.auth.AtProtoAuthentication
import com.github.cc007.poc.atproto.components.overview.postSummary
import com.github.cc007.poc.atproto.components.topBanner
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import work.socialhub.kbsky.BlueskyFactory
import work.socialhub.kbsky.api.entity.app.bsky.feed.FeedGetTimelineRequest
import work.socialhub.kbsky.auth.BearerTokenAuthProvider

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
        return with(SecurityContextHolder.getContext().authentication as AtProtoAuthentication) {
            val auth = BearerTokenAuthProvider(accessToken)
            val csrfToken = (request.getAttribute("_csrf") as? CsrfToken)?.token
            val feed = BlueskyFactory
                .instance()
                .feed()

            createHTML().html {
                head {
                    title("Browse")
                    meta(name = "viewport", content = "width=device-width, initial-scale=1")
                    link(rel = "stylesheet", href = "/css/browse.css")
                }
                body(classes = "browse-body") {
                    topBanner(csrfToken)
                    main(classes = "browse-layout") {
                        aside(classes = "browse-sidebar") {
                            h2(classes = "sidebar-title") { +"BlueArt" }
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
                                h1 { +"Browse Timeline" }
                                div(classes = "filter-row") {
                                    button(classes = "filter-chip filter-chip-active") { +"Hot" }
                                    button(classes = "filter-chip") { +"New" }
                                    button(classes = "filter-chip") { +"Artists" }
                                    button(classes = "filter-chip") { +"Commissions" }
                                }
                            }
                            section(classes = "feed-grid") {
                                val timeline = feed.getTimelineBlocking(FeedGetTimelineRequest(auth))
                                logger.info { timeline.json.pretty() }
                                timeline.data.feed.forEach {
                                    postSummary(it.post, it.reply?.parent)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

