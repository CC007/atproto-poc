package com.github.cc007.poc.atproto.browse

import com.github.cc007.poc.atproto.auth.AtProtoAuthentication
import com.github.cc007.poc.atproto.components.overview.postSummary
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
    fun index(
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
                }
                body {
                    header {
                        form(action = "/logout", method = FormMethod.post) {
                            if (csrfToken != null) {
                                input(type = InputType.hidden, name = "_csrf") {
                                    value = csrfToken
                                }
                            }
                            p {
                                submitInput { value = "Logout" }
                            }
                        }
                    }
                    main {
                        h1 { +"Timeline" }
                        section {
                            val timeline = feed.getTimeline(FeedGetTimelineRequest(auth))
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

