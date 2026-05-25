package com.github.cc007.blueart.endpoints.browse

import com.github.cc007.blueart.components.overview.postSummary
import com.github.cc007.blueart.components.topBanner
import com.github.cc007.blueart.endpoints.auth.AtProtoAuthentication
import com.github.cc007.blueart.kolostyles.render.kolo
import com.github.cc007.blueart.kolostyles.render.m
import com.github.cc007.blueart.kolostyles.render.mb
import com.github.cc007.blueart.kolostyles.render.p
import com.github.cc007.blueart.kolostyles.render.koloStylesheetLink
import com.github.cc007.blueart.kolostyles.render.renderKoloHtml
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

            renderKoloHtml {
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
                                val timeline = feed.getTimelineBlocking(FeedGetTimelineRequest(auth))
                                logger.info { timeline.json.pretty() }
                                timeline.data.feed
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
}

