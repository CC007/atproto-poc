package com.github.cc007.poc.atproto.components.overview

import kotlinx.html.*
import work.socialhub.kbsky.model.app.bsky.actor.ActorDefsProfileViewBasic
import work.socialhub.kbsky.model.app.bsky.actor.ActorProfile
import work.socialhub.kbsky.model.app.bsky.embed.*
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsPostView
import work.socialhub.kbsky.model.app.bsky.feed.FeedLike
import work.socialhub.kbsky.model.app.bsky.feed.FeedPost
import work.socialhub.kbsky.model.app.bsky.feed.FeedRepost
import work.socialhub.kbsky.model.app.bsky.graph.*

private val BLUR_LABELS = setOf("porn", "sexual")

fun HtmlBlockTag.postSummary(post: FeedDefsPostView, parentPost: FeedDefsPostView?) {
    val labels = (post.labels ?: listOf()).map { it.`val` }
    val blurMedia = labels.any { it in BLUR_LABELS }
    article(classes = "post-card") {
        post.author?.let { summaryAuthor(it) }
        div(classes = "post-content") {
            when (val record = post.record) {
                is FeedPost -> {
                    p(classes = "post-text") {
                        +"${record.text}"
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
            when (val embed = post.embed) {
                is EmbedImagesView -> { imageEmbed(embed, blurMedia) }
                is EmbedVideoView -> { videoEmbed(embed, blurMedia) }
                is EmbedExternalView -> {}
                is EmbedRecordView -> {}
                is EmbedRecordWithMediaView -> {}
            }
        }
        parentPost?.let {
            div(classes = "parent-post") {
                postSummary(it, null)
            }
        }
        div(classes = "post-stats") {
            +"Likes: ${post.likeCount} | Quotes: ${post.quoteCount} | Reposts: ${post.repostCount} | Replies: ${post.replyCount}"
        }
    }
}

private fun HtmlBlockTag.summaryAuthor(author: ActorDefsProfileViewBasic) {
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

private fun HtmlBlockTag.imageEmbed(embed: EmbedImagesView, blur: Boolean = false) {
    embed.images?.forEach { image ->
        image.thumb?.let {
            if (blur) {
                div(classes = "embed-blur-clip") {
                    img(src = it, classes = "embed-media embed-media-blur") {
                        height = "90"
                        width = "160"
                    }
                }
            } else {
                img(src = it, classes = "embed-media") {
                    height = "90"
                    width = "160"
                }
            }
        }
    }
}

private fun HtmlBlockTag.videoEmbed(embed: EmbedVideoView, blur: Boolean = false) {
    embed.thumbnail?.let {
        if (blur) {
            div(classes = "embed-blur-clip") {
                img(src = it, classes = "embed-media embed-media-blur") {
                    height = "90"
                    width = "160"
                }
            }
        } else {
            img(src = it, classes = "embed-media") {
                height = "90"
                width = "160"
            }
        }
    }
}