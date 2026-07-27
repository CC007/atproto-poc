package com.github.cc007.blueart.components.overview

import kotlinx.html.div
import kotlinx.html.stream.createHTML
import work.socialhub.kbsky.model.app.bsky.actor.ActorDefsProfileViewBasic
import work.socialhub.kbsky.model.app.bsky.embed.EmbedImagesView
import work.socialhub.kbsky.model.app.bsky.embed.EmbedImagesViewImage
import work.socialhub.kbsky.model.app.bsky.embed.EmbedViewUnion
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsPostView
import work.socialhub.kbsky.model.app.bsky.feed.FeedPost
import work.socialhub.kbsky.model.app.bsky.graph.GraphFollow
import kotlin.test.Test
import kotlin.test.assertTrue

class PostSummaryTest {

    @Test
    fun `renders record text for text-only post`() {
        val html = createHTML().div {
            postSummary(
                post = postView(
                    recordText = "plain text only",
                    embed = null,
                    uri = "at://did:example/post/1",
                    cid = "cid-1"
                ),
                parentPost = null
            )
        }

        assertTrue(html.contains("plain text only"))
    }

    @Test
    fun `renders split media grid for multiple images`() {
        val html = createHTML().div {
            postSummary(
                post = postView(
                    recordText = "caption",
                    embed = imageEmbed(
                        "https://example.com/image-1.jpg",
                        "https://example.com/image-2.jpg",
                        "https://example.com/image-3.jpg"
                    ),
                    uri = "at://did:example/post/3",
                    cid = "cid-3"
                ),
                parentPost = null
            )
        }

        assertTrue(html.contains("embed-media-grid"))
        assertTrue(html.contains("embed-media-grid-primary"))
        assertTrue(html.contains("embed-media-grid-secondary"))
    }

    @Test
    fun `renders follow activity for graph follow record`() {
        val html = createHTML().div {
            postSummary(
                post = FeedDefsPostView().apply {
                    author = ActorDefsProfileViewBasic().apply {
                        handle = "dummy.localhost"
                    }
                    record = GraphFollow(
                        subject = "did:plc:dummy-follow-target",
                        createdAt = "2026-06-01T00:00:00Z",
                    )
                    uri = "at://did:example/post/4"
                    cid = "cid-4"
                },
                parentPost = null
            )
        }

        assertTrue(html.contains("@dummy.localhost followed @dummy-follow-target"))
        assertTrue(!html.contains("This type of post is not yet supported"))
    }

    private fun postView(
        recordText: String,
        embed: EmbedViewUnion?,
        uri: String,
        cid: String,
    ): FeedDefsPostView {
        return FeedDefsPostView().apply {
            record = FeedPost().apply {
                text = recordText
            }
            this.embed = embed
            this.uri = uri
            this.cid = cid
        }
    }

    private fun imageEmbed(vararg thumbs: String): EmbedImagesView {
        return EmbedImagesView().apply {
            images = thumbs.map { thumb -> EmbedImagesViewImage(thumb = thumb) }
        }
    }
}


