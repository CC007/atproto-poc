package com.github.cc007.blueart.components.overview

import kotlinx.html.div
import kotlinx.html.stream.createHTML
import work.socialhub.kbsky.model.app.bsky.embed.EmbedImagesView
import work.socialhub.kbsky.model.app.bsky.embed.EmbedImagesViewImage
import work.socialhub.kbsky.model.app.bsky.embed.EmbedViewUnion
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsPostView
import work.socialhub.kbsky.model.app.bsky.feed.FeedPost
import kotlin.test.Test
import kotlin.test.assertFalse
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
    fun `suppresses record text when embed exists`() {
        val html = createHTML().div {
            postSummary(
                post = postView(
                    recordText = "this should be hidden",
                    embed = imageEmbed("https://example.com/image-1.jpg"),
                    uri = "at://did:example/post/2",
                    cid = "cid-2"
                ),
                parentPost = null
            )
        }

        assertFalse(html.contains("this should be hidden"))
        assertTrue(html.contains("Open artwork"))
        assertTrue(html.contains("post-card-media"))
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



