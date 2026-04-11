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

fun HtmlBlockTag.postSummary(post: FeedDefsPostView, parentPost: FeedDefsPostView?) {
    val labels = (post.labels ?: listOf()).map { it.`val` }
    article {
        style = "border: 1px solid black; border-radius: 6px; padding: 5px; margin-bottom: 5px;"
        post.author?.let { summaryAuthor(it) }
        div {
            when (val record = post.record) {
                is FeedPost -> {
                    p {
                        +"${record.text}"
                    }
                    if (labels.isNotEmpty()) {
                        p {
                            +"Labels: ${labels.joinToString()}"
                        }
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
                is EmbedImagesView -> { imageEmbed(embed) }
                is EmbedVideoView -> { videoEmbed(embed) }
                is EmbedExternalView -> {}
                is EmbedRecordView -> {}
                is EmbedRecordWithMediaView -> {}
            }
        }
        parentPost?.let { postSummary(it, null) }
        div {
            +"Likes: ${post.likeCount} | Quotes: ${post.quoteCount} | Reposts: ${post.repostCount} | Replies: ${post.replyCount}"
        }
    }
}

private fun HtmlBlockTag.summaryAuthor(author: ActorDefsProfileViewBasic) {
    div {
        style = "display: flex; align-items: center;"
        author.avatar?.let {
            img(src = it) {
                height = "30"
                width = "30"
                style = "flex: 0;"
            }
        }
        div {
            style = "flex: 1; padding: 5px;"
            author.displayName?.let { strong { +it } }
            +"@${author.handle}"
        }
    }
}

private fun HtmlBlockTag.imageEmbed(embed: EmbedImagesView) {
    embed.images?.forEach { image ->
        image.thumb?.let {
            img(src = it) {
                height = "90"
                width = "160"
            }
        }
    }
}

private fun HtmlBlockTag.videoEmbed(embed: EmbedVideoView) {
    embed.thumbnail?.let {
        img(src = it) {
            height = "90"
            width = "160"
        }
    }
}