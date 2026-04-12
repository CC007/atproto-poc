package com.github.cc007.poc.atproto.components.overview

import kotlinx.html.*
import work.socialhub.kbsky.model.app.bsky.actor.ActorDefsProfileViewBasic
import work.socialhub.kbsky.model.app.bsky.embed.*
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsPostView
import work.socialhub.kbsky.model.app.bsky.feed.FeedPost
import work.socialhub.kbsky.model.share.RecordUnion

private val BLUR_LABELS = setOf("porn", "sexual")

fun HtmlBlockTag.postSummary(post: FeedDefsPostView, parentPost: FeedDefsPostView?) {
    val labels = (post.labels ?: listOf()).map { it.`val` }
    val blurMedia = labels.any { it in BLUR_LABELS }
    article(classes = "post-card") {
        post.author?.let { authorBanner(it) }
        div(classes = "post-content") {
            record(post.record)
            post.embed?.let { embed(it, blurMedia) }
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
        div(classes = "post-stats") {
            +"Likes: ${post.likeCount} | Quotes: ${post.quoteCount} | Reposts: ${post.repostCount} | Replies: ${post.replyCount}"
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
    blurMedia: Boolean
) {
    when (embed) {
        is EmbedImagesView -> {
            embed.images?.forEach { image ->
                image.thumb?.let {
                    embedThumbnail(it, blurMedia)
                }
            }
        }

        is EmbedVideoView -> {
            embed.thumbnail?.let {
                embedThumbnail(it, blurMedia)
            }
        }

        is EmbedExternalView -> {
            embed.external?.thumb?.let {
                embedThumbnail(it, blurMedia)
            }
        }

        is EmbedRecordView -> {
            div(classes = "embed-record") {
                article(classes = "post-card") {
                    embed.record?.asRecord?.let {
                        it.author?.let { author -> authorBanner(author) }
                        record(it.value)
                        it.embeds?.forEach { embed(it, blurMedia) }
                    }
                }
            }
        }

        is EmbedRecordWithMediaView -> {
            val labels = (embed.record?.record?.asRecord?.labels ?: listOf()).map { it.`val` }
            val blurMedia = labels.any { it in BLUR_LABELS }
            div(classes = "embed-record-with-media") {
                article(classes = "post-card") {
                    embed.record?.record?.asRecord?.let {
                        it.author?.let { author -> authorBanner(author) }
                        record(it.value)
                        it.embeds?.forEach { embed(it, blurMedia) }
                    }
                    embed.media?.let { embed(it, blurMedia) }
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
