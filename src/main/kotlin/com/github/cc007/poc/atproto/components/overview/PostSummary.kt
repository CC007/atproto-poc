package com.github.cc007.poc.atproto.components.overview

import kotlinx.html.*
import work.socialhub.kbsky.model.app.bsky.actor.ActorDefsProfileViewBasic
import work.socialhub.kbsky.model.app.bsky.embed.*
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsPostView
import work.socialhub.kbsky.model.app.bsky.feed.FeedPost
import work.socialhub.kbsky.model.com.atproto.label.LabelDefsLabel
import work.socialhub.kbsky.model.share.RecordUnion

private val BLUR_LABELS = setOf("porn", "sexual")

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
    parentPost: FeedDefsPostView? = null
) {
    article(classes = "post-card") {
        author?.let { authorBanner(it) }
        div(classes = "post-content") {
            record(record)
            embeds.forEach { embed(it, needsBlur) }
        }
        div(classes = "post-stats") {
            +"Likes: $likeCount | Quotes: $quoteCount | Reposts: $repostCount | Replies: $replyCount"
        }
        parentPost?.let {
            div(classes = "parent-post") {
                br()
                em {
                    +"Reply to:"
                }
                postSummary(it, null)
            }
        }
    }
}

private fun HtmlBlockTag.authorBanner(author: ActorDefsProfileViewBasic) {
    div(classes = "post-author") {
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
                +"${record.text}"
            }
        }

        else -> {
            p(classes = "post-text") {
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
            embed.images?.forEach { image ->
                image.thumb?.let {
                    embedThumbnail(it, needsBlur)
                }
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
            div(classes = "embed-record") {
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
                    )
                }
            }
        }

        is EmbedRecordWithMediaView -> {
            div(classes = "embed-record-with-media") {
                embed.record?.record?.asRecord?.let {
                    val needsBlur = needsBlur(it.labels)
                    val recordEmbeds = it.embeds ?: emptyList()
                    val embeds = embed.media?.let { listOf(*recordEmbeds.toTypedArray(), it) } ?: recordEmbeds
                    postSummary(
                        it.author,
                        it.value,
                        embeds,
                        needsBlur,
                        it.likeCount,
                        it.quoteCount,
                        it.repostCount,
                        it.replyCount,
                    )
                }
            }
        }

        else -> {
            p(classes = "embed-media") {
                em {
                    +"This type of embed is not supported: ${embed.type}"
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

private fun HtmlBlockTag.embedThumbnail(src: String, blur: Boolean) {
    if (blur) {
        div(classes = "embed-blur-clip") {
            img(src = src, classes = "embed-media embed-media-blur") {
                height = "90"
                width = "160"
            }
        }
    } else {
        img(src = src, classes = "embed-media") {
            height = "90"
            width = "160"
        }
    }
}
