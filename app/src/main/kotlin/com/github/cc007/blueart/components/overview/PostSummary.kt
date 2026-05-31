package com.github.cc007.blueart.components.overview

import com.github.cc007.blueart.components.richtext.renderRichText
import com.github.cc007.blueart.kolostyles.render.*
import kotlinx.html.*
import work.socialhub.kbsky.model.app.bsky.actor.ActorDefsProfileViewBasic
import work.socialhub.kbsky.model.app.bsky.embed.*
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsPostView
import work.socialhub.kbsky.model.app.bsky.feed.FeedPost
import work.socialhub.kbsky.model.com.atproto.label.LabelDefsLabel
import work.socialhub.kbsky.model.share.RecordUnion
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private val BLUR_LABELS = setOf("porn", "sexual")
private const val LIKE_ICON = """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" aria-hidden="true" focusable="false"><path d="M12 21s-6.7-4.35-9.2-8.1C.8 9.95 2.1 6.5 5.45 5.7 7.5 5.2 9.25 5.95 10.4 7.4L12 9.4l1.6-2c1.15-1.45 2.9-2.2 4.95-1.7 3.35.8 4.65 4.25 2.65 7.2C18.7 16.65 12 21 12 21z"/></svg>"""
private const val QUOTE_ICON = """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" aria-hidden="true" focusable="false"><path d="M7.8 7.5c-1.9 0-3.3 1.5-3.3 3.6V16h4.7v-4H7.6c.08-.96.76-1.78 1.86-2.16L7.8 7.5z"/><path d="M16.3 7.5c-1.9 0-3.3 1.5-3.3 3.6V16h4.7v-4h-1.6c.08-.96.76-1.78 1.86-2.16L16.3 7.5z"/></svg>"""
private const val REPOST_ICON = """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" aria-hidden="true" focusable="false"><path d="M5.5 7h11"/><path d="M13.5 4.5 16 7l-2.5 2.5"/><path d="M18.5 17h-11"/><path d="M10.5 14.5 8 17l2.5 2.5"/></svg>"""
private const val REPLY_ICON = """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" aria-hidden="true" focusable="false"><path d="M5.5 6.5h13A1.5 1.5 0 0 1 20 8v6a1.5 1.5 0 0 1-1.5 1.5h-6.1L7.5 19v-3.5h-2A1.5 1.5 0 0 1 4 14V8a1.5 1.5 0 0 1 1.5-1.5z"/></svg>"""
private const val BOOKMARK_ICON = """<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" aria-hidden="true" focusable="false"><path d="M7 5.5h10a1 1 0 0 1 1 1V20l-6-3.8L6 20V6.5a1 1 0 0 1 1-1z"/></svg>"""

fun HtmlBlockTag.postSummary(post: FeedDefsPostView, parentPost: FeedDefsPostView?) {
    val needsBlur = needsBlur(post.labels)
    postSummary(
        post.author,
        post.record,
        post.embed?.let { listOf(it) } ?: emptyList(),
        needsBlur,
        post.likeCount,
        post.quoteCount,
        post.repostCount,
        post.replyCount,
        post.bookmarkCount,
        post.uri,
        post.cid,
        parentPost
    )
}

private fun HtmlBlockTag.postSummary(
    author: ActorDefsProfileViewBasic?,
    record: RecordUnion?,
    embeds: List<EmbedViewUnion>,
    needsBlur: Boolean,
    likeCount: Int? = null,
    quoteCount: Int? = null,
    repostCount: Int? = null,
    replyCount: Int? = null,
    bookmarkCount: Int? = null,
    postUri: String? = null,
    postCid: String? = null,
    parentPost: FeedDefsPostView? = null
) {
    article(classes = "post-card post-card-media") {
        kolo { p(1) }
        author?.let { authorBanner(it) }
        div(classes = "post-content") {
            embeds.forEach { embed(it, needsBlur) }
            record(record)
        }
        div(classes = "post-stats") {
            kolo { mt(3); pt(2) }
            statItem("Likes", LIKE_ICON, likeCount, "post-stat-icon-like")
            statItem("Quotes", QUOTE_ICON, quoteCount, "post-stat-icon-quote")
            statItem("Reposts", REPOST_ICON, repostCount, "post-stat-icon-repost")
            statItem("Replies", REPLY_ICON, replyCount, "post-stat-icon-reply")
            statItem("Bookmarks", BOOKMARK_ICON, bookmarkCount, "post-stat-icon-bookmark")
        }
        postLink(author?.handle, postUri, postCid, embeds)
        parentPost?.let {
            div(classes = "parent-post") {
                p {
                    kolo { my(2) }
                    em {
                        +"Reply to:"
                    }
                }
                postSummary(it, null)
            }
        }
    }
}

private fun FlowContent.postLink(
    handle: String?,
    uri: String?,
    cid: String?,
    embeds: List<EmbedViewUnion>,
) {
    if (embeds.isEmpty()) {
        return
    }
    val postUri = uri ?: return
    val postCid = cid ?: return
    val encodedUri = URLEncoder.encode(postUri, StandardCharsets.UTF_8)

    p(classes = "post-open-link") {
        kolo { m(2) }
        a(href = "/art/$postCid?uri=$encodedUri") {
            +"Open artwork"
        }
        handle?.let {
            +" by @$it"
        }
    }
}

private fun FlowContent.statItem(label: String, iconSvg: String, count: Int?, iconClass: String) {
    span(classes = "post-stat-item") {
        val statLabel = "$label: ${count ?: 0}"
        attributes["aria-label"] = statLabel
        attributes["title"] = statLabel
        span(classes = "post-stat-icon $iconClass") {
            unsafe {
                +iconSvg
            }
        }
        span(classes = "post-stat-count") {
            +"${count ?: 0}"
        }
    }
}

private fun HtmlBlockTag.authorBanner(author: ActorDefsProfileViewBasic) {
    div(classes = "post-author") {
        kolo { py(1); px(2) }
        author.avatar?.let {
            img(src = it, classes = "author-avatar") {
                height = "30"
                width = "30"
            }
        }
        div(classes = "author-meta") {
            author.displayName?.let { strong(classes = "author-name") { +it } }
            span(classes = "author-handle") { +"@${author.handle}" }
        }
    }
}

private fun HtmlBlockTag.record(record: RecordUnion?) {
    when (record) {
        is FeedPost -> {
            p(classes = "post-text feed-post") {
                kolo { m(2) }
                renderRichText(record.text, record.facets)
            }
        }

        else -> {
            p(classes = "post-text") {
                kolo { m(0) }
                em {
                    +"This type of post is not yet supported${record?.type?.let { ": $it" } ?: ""}"
                }
            }
        }
//                is FeedRepost -> {}
//                is ActorProfile -> {}
//                is GraphFollow -> {}
//                is GraphBlock -> {}
//                is FeedLike -> {}
//                is GraphListItem -> {}
//                is GraphList -> {}
//                is GraphStarterPack -> {}
    }
}

private fun HtmlBlockTag.embed(
    embed: EmbedViewUnion,
    needsBlur: Boolean
) {
    when (embed) {
        is EmbedImagesView -> {
            val imageThumbs = embed.images
                ?.mapNotNull { it.thumb }
                ?: emptyList()
            if (imageThumbs.isNotEmpty()) {
                renderImageGallery(imageThumbs, needsBlur)
            }
        }

        is EmbedVideoView -> {
            embed.thumbnail?.let {
                embedThumbnail(it, needsBlur)
            }
        }

        is EmbedExternalView -> {
            embed.external?.thumb?.let {
                embedThumbnail(it, needsBlur)
            }
        }

        is EmbedRecordView -> {
            div {
                embed.record?.asRecord?.let {
                    val needsBlur = needsBlur(it.labels)
                    postSummary(
                        it.author,
                        it.value,
                        it.embeds ?: emptyList(),
                        needsBlur,
                        it.likeCount,
                        it.quoteCount,
                        it.repostCount,
                        it.replyCount,
                        it.bookmarkCount,
                        it.uri,
                        it.cid,
                    )
                }
            }
        }

        is EmbedRecordWithMediaView -> {
            embed.media?.let { embed(it, needsBlur) }

            div {
                embed.record?.record?.asRecord?.let {
                    val recordNeedsBlur = needsBlur(it.labels)
                    postSummary(
                        it.author,
                        it.value,
                        it.embeds ?: emptyList(),
                        recordNeedsBlur,
                        it.likeCount,
                        it.quoteCount,
                        it.repostCount,
                        it.replyCount,
                        it.bookmarkCount,
                        it.uri,
                        it.cid,
                    )
                }
            }
        }

        else -> {
            div(classes = "embed-media") {
                p {
                    kolo { m(2) }
                    em {
                        +"This type of embed is not supported: ${embed.type}"
                    }
                }
            }
        }
    }
}

private fun needsBlur(labels: List<LabelDefsLabel>?): Boolean {
    val labelsVals = (labels ?: emptyList()).map { it.`val` }
    val blurMedia = labelsVals.any { it in BLUR_LABELS }
    return blurMedia
}

private fun HtmlBlockTag.renderImageGallery(imageThumbs: List<String>, blur: Boolean) {
    if (imageThumbs.size <= 1) {
        embedThumbnail(imageThumbs.first(), blur, "embed-media-single")
        return
    }

    div(classes = "embed-media-grid") {
        kolo { mt(2) }
        div(classes = "embed-media-grid-main") {
            embedThumbnail(imageThumbs.first(), blur, "embed-media-grid-primary")
        }
        div(classes = "embed-media-grid-side") {
            imageThumbs.drop(1).take(3).forEach { thumb ->
                embedThumbnail(thumb, blur, "embed-media-grid-secondary")
            }
        }
    }
}

private fun HtmlBlockTag.embedThumbnail(src: String, blur: Boolean, mediaClass: String = "") {
    val classes = listOf("embed-media", mediaClass)
        .filter { it.isNotBlank() }
        .joinToString(" ")

    if (blur) {
        div(classes = "embed-blur-clip") {
            kolo { m(0) }
            img(src = src, classes = "$classes embed-media-blur")
        }
    } else {
        div {
            kolo { m(0) }
            img(src = src, classes = classes)
        }
    }
}
