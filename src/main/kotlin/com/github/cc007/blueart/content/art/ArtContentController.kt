package com.github.cc007.blueart.content.art

import com.github.cc007.blueart.auth.AtProtoAuthentication
import com.github.cc007.blueart.components.topBanner
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import work.socialhub.kbsky.BlueskyFactory
import work.socialhub.kbsky.api.entity.app.bsky.feed.FeedGetPostThreadRequest
import work.socialhub.kbsky.api.entity.app.bsky.feed.FeedGetPostsRequest
import work.socialhub.kbsky.api.entity.app.bsky.feed.FeedGetTimelineRequest
import work.socialhub.kbsky.auth.BearerTokenAuthProvider
import work.socialhub.kbsky.model.app.bsky.embed.*
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsPostView
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsThreadUnion
import work.socialhub.kbsky.model.app.bsky.feed.FeedPost
import work.socialhub.kbsky.model.share.RecordUnion

private val logger = KotlinLogging.logger {}

@Controller
class ArtContentController {

    @GetMapping("/art/{cid}", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun artEndpoint(
        request: HttpServletRequest,
        @PathVariable cid: String,
        @RequestParam("uri", required = false) uri: String?,
    ): String {
        return with(SecurityContextHolder.getContext().authentication as AtProtoAuthentication) {
            val auth = BearerTokenAuthProvider(accessToken)
            val csrfToken = (request.getAttribute("_csrf") as? CsrfToken)?.token

            val feed = BlueskyFactory
                .instance()
                .feed()

            val requestedUri = if (!uri.isNullOrBlank()) uri else null
            val post = fetchPost(feed, auth, cid, requestedUri)
            val threadRoot = post?.uri?.let {
                try {
                    feed.getPostThreadBlocking(
                        FeedGetPostThreadRequest(
                            auth = auth,
                            uri = it,
                            depth = 5,
                        )
                    ).data.thread?.asViewPost
                } catch (ex: Exception) {
                    logger.warn(ex) { "Unable to load thread for uri=$it" }
                    null
                }
            }
            val comments = mutableListOf<CommentView>().also { list ->
                threadRoot?.replies.orEmpty().forEach { reply ->
                    collectComments(reply, 0, list)
                }
            }

            val pageTitle = buildPageTitle(post)

            createHTML().html {
                head {
                    title(pageTitle)
                    meta(name = "viewport", content = "width=device-width, initial-scale=1")
                    link(rel = "stylesheet", href = "/css/art.css")
                }
                body(classes = "art-body") {
                    topBanner(csrfToken)
                    main(classes = "art-layout") {
                        section(classes = "art-content") {
                            div(classes = "content-top") {
                                h1(classes = "art-title") { +pageTitle }
                                post?.author?.handle?.let {
                                    p(classes = "art-byline") { +"by @$it" }
                                }
                            }

                            if (post == null) {
                                section(classes = "art-card") {
                                    p(classes = "art-empty") {
                                        +"Could not load this artwork. Try opening it from browse again."
                                    }
                                }
                            } else {
                                section(classes = "art-card") {
                                    section(classes = "art-embed") {
                                        renderMainEmbed(post.embed)
                                    }

                                    section(classes = "art-description") {
                                        h2 { +"Description" }
                                        p(classes = "art-text") { +(postText(post.record) ?: "No description provided.") }
                                    }
                                }

                                section(classes = "comments") {
                                    h2 { +"Comments" }
                                    if (comments.isEmpty()) {
                                        p(classes = "art-empty") { +"No comments yet." }
                                    } else {
                                        comments.forEach { comment ->
                                            article(classes = "comment depth-${comment.depth.coerceAtMost(4)}") {
                                                div(classes = "comment-header") {
                                                    comment.avatar?.let { avatar ->
                                                        img(src = avatar, classes = "comment-avatar") {
                                                            alt = ""
                                                            width = "28"
                                                            height = "28"
                                                        }
                                                    }
                                                    div(classes = "comment-meta") {
                                                        strong(classes = "comment-author") { +comment.displayName }
                                                        span(classes = "comment-handle") { +"@${comment.handle}" }
                                                    }
                                                }
                                                p(classes = "comment-text") { +comment.text }
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
    }
}

private fun FlowContent.renderMainEmbed(embed: EmbedViewUnion?) {
    when (embed) {
        is EmbedImagesView -> {
            val images = embed.images.orEmpty()
            if (images.isEmpty()) {
                p(classes = "art-empty") { +"No artwork media attached." }
                return
            }
            div(classes = if (images.size > 1) "art-image-grid" else "art-image-single") {
                images.forEach { image ->
                    img(src = image.fullsize ?: image.thumb ?: "", classes = "art-image") {
                        alt = image.alt ?: "Artwork image"
                    }
                }
            }
        }

        is EmbedVideoView -> {
            embed.thumbnail?.let { thumb ->
                img(src = thumb, classes = "art-image") {
                    alt = embed.alt ?: "Artwork video"
                }
            } ?: p(classes = "art-empty") { +"Video embed has no thumbnail available." }
        }

        is EmbedExternalView -> {
            val external = embed.external
            if (external?.thumb != null) {
                img(src = external.thumb!!, classes = "art-image") {
                    alt = external.title.ifBlank { "Artwork link" }
                }
            }
            p(classes = "art-external") {
                +(external?.title?.ifBlank { external.uri } ?: "External embed")
            }
        }

        is EmbedRecordWithMediaView -> renderMainEmbed(embed.media)
        else -> p(classes = "art-empty") { +"This post has no supported artwork embed." }
    }
}

private fun fetchPost(
    feed: work.socialhub.kbsky.api.app.bsky.FeedResource,
    auth: BearerTokenAuthProvider,
    cid: String,
    uri: String?,
): FeedDefsPostView? {
    val targetUri = uri ?: if (cid.startsWith("at://")) cid else null
    if (targetUri != null) {
        try {
            return feed.getPostsBlocking(
                FeedGetPostsRequest(
                    auth = auth,
                    uris = listOf(targetUri)
                )
            ).data.posts.firstOrNull()
        } catch (ex: Exception) {
            logger.warn(ex) { "Unable to load post by uri=$targetUri" }
        }
    }

    // Best-effort fallback when we only have a CID in the route.
    return try {
        feed.getTimelineBlocking(FeedGetTimelineRequest(auth)).data.feed
            .firstOrNull { it.post.cid == cid }
            ?.post
    } catch (ex: Exception) {
        logger.warn(ex) { "Unable to find post by cid=$cid" }
        null
    }
}

private fun postText(record: RecordUnion?): String? = (record as? FeedPost)?.text

private fun buildPageTitle(post: FeedDefsPostView?): String {
    val author = post?.author?.displayName ?: post?.author?.handle ?: "Artwork"
    val text = postText(post?.record).orEmpty().trim().replace("\n", " ")
    val subject = text.takeIf { it.isNotBlank() }?.take(60) ?: "Post"
    return "$author - $subject"
}

private data class CommentView(
    val displayName: String,
    val handle: String,
    val avatar: String?,
    val text: String,
    val depth: Int,
)

private fun collectComments(
    thread: FeedDefsThreadUnion,
    depth: Int,
    into: MutableList<CommentView>,
) {
    val reply = thread.asViewPost ?: return
    val post = reply.post ?: return
    val text = postText(post.record)
    if (!text.isNullOrBlank()) {
        into += CommentView(
            displayName = post.author?.displayName ?: post.author?.handle ?: "Unknown",
            handle = post.author?.handle ?: "unknown",
            avatar = post.author?.avatar,
            text = text,
            depth = depth,
        )
    }
    reply.replies.orEmpty().forEach { child -> collectComments(child, depth + 1, into) }
}