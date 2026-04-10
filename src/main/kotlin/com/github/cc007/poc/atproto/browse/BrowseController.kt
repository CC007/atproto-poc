package com.github.cc007.poc.atproto.browse

import com.github.cc007.poc.atproto.auth.AtProtoAuthentication
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
import work.socialhub.kbsky.model.app.bsky.actor.ActorProfile
import work.socialhub.kbsky.model.app.bsky.embed.*
import work.socialhub.kbsky.model.app.bsky.feed.FeedLike
import work.socialhub.kbsky.model.app.bsky.feed.FeedPost
import work.socialhub.kbsky.model.app.bsky.feed.FeedRepost
import work.socialhub.kbsky.model.app.bsky.graph.*

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
                                article {
                                    style = "border: 1px solid black; border-radius: 6px; padding: 5px; margin-bottom: 5px;"
                                    div {
                                        it.post.author?.displayName?.let { strong { +it } }
                                        +"@${it.post.author?.handle}"
                                    }
                                    div {
                                        when (val record = it.post.record) {
                                            is FeedPost -> {
                                                p {
                                                    +"${record.text}"
                                                }
                                                record.labels?.values?.joinToString(", ") { it.`val` }?.let { label ->
                                                    p {
                                                        +"Labels: $label"
                                                    }
                                                }
                                                when (val embed = it.post.embed) {
                                                    is EmbedImagesView -> {
                                                        embed.images?.forEach { image ->
                                                            image.thumb?.let {
                                                                img(src = it) {
                                                                    height = "90"
                                                                    width = "160"
                                                                }
                                                            }
                                                        }
                                                    }
                                                    is EmbedVideoView -> {
                                                        embed.thumbnail?.let {
                                                            img(src = it) {
                                                                height = "90"
                                                                width = "160"
                                                            }
                                                        }
                                                    }
                                                    is EmbedExternalView -> {}
                                                    is EmbedRecordView -> {}
                                                    is EmbedRecordWithMediaView -> {}
                                                }
                                            }

                                            is FeedRepost -> {}
                                            is ActorProfile -> {}
                                            is GraphFollow -> {}
                                            is GraphBlock -> {}
                                            is FeedLike -> {}
                                            is GraphListItem -> {}
                                            is GraphList -> {}
                                            is GraphStarterPack -> {}
                                        }

                                    }
                                    div {
                                        +"Likes: ${it.post.likeCount} | Quotes: ${it.post.quoteCount} | Reposts: ${it.post.repostCount} | Replies: ${it.post.replyCount}"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}