package com.github.cc007.blueart.components.overview

import com.github.cc007.blueart.testsupport.parseHtml
import com.github.cc007.blueart.testsupport.selectRequired
import com.github.cc007.blueart.testsupport.shouldContainText
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
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

        val document = html.parseHtml()
        document.selectRequired("p.post-text.feed-post") shouldContainText "plain text only"
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

        val document = html.parseHtml()
        document.selectRequired(".embed-media-grid")
        document.select(".embed-media-grid-main img.embed-media-grid-primary").size shouldBe 1
        document.selectRequired(".embed-media-grid-main img.embed-media-grid-primary")
            .attr("src") shouldBe "https://example.com/image-1.jpg"

        val secondarySources = document.select(".embed-media-grid-side img.embed-media-grid-secondary")
            .eachAttr("src")
        secondarySources shouldContainExactly listOf(
            "https://example.com/image-2.jpg",
            "https://example.com/image-3.jpg",
        )
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

        val document = html.parseHtml()
        document.selectRequired("p.post-text.feed-follow") shouldContainText "@dummy.localhost followed @dummy-follow-target"
        document.selectFirst("p.post-text em").shouldBeNull()
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
