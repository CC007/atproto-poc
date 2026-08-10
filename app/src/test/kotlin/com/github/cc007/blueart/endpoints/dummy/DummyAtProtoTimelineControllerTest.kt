package com.github.cc007.blueart.endpoints.dummy

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import work.socialhub.kbsky.ATProtocolException
import work.socialhub.kbsky.BlueskyFactory
import work.socialhub.kbsky.api.entity.app.bsky.feed.FeedGetTimelineRequest
import work.socialhub.kbsky.api.entity.app.bsky.feed.FeedGetTimelineResponse
import work.socialhub.kbsky.auth.BearerTokenAuthProvider
import work.socialhub.kbsky.model.app.bsky.embed.*
import work.socialhub.kbsky.model.app.bsky.graph.GraphFollow


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DummyAtProtoTimelineControllerTest(
    @LocalServerPort private val port: Int,
) {

    private lateinit var networkUrl: String

    @BeforeEach
    fun setUp() {
        networkUrl = "http://localhost:$port"
    }

    @Test
    fun `dummy timeline endpoint returns deterministic paged feed`() {
        val firstPage = getTimeline(cursor = null)
        firstPage.cursor shouldBe "dummy-cursor-2"
        firstPage.feed.size shouldBe 3

        val secondPage = getTimeline(cursor = firstPage.cursor)
        secondPage.cursor shouldBe "dummy-cursor-3"
        secondPage.feed.size shouldBe 3

        val thirdPage = getTimeline(cursor = secondPage.cursor)
        thirdPage.cursor shouldBe "dummy-cursor-4"
        thirdPage.feed.size shouldBe 3

        val fourthPage = getTimeline(cursor = thirdPage.cursor)
        fourthPage.cursor shouldBe null
        fourthPage.feed.size shouldBe 2

        val feed = firstPage.feed + secondPage.feed + thirdPage.feed + fourthPage.feed
        feed.size shouldBe 11

        // Keep order assertions for deterministic fixture pages.
        feed[0].post.uri shouldBe "at://dummy.localhost/app.bsky.feed.post/text"
        feed[1].post.uri shouldBe "at://dummy.localhost/app.bsky.feed.post/image"
        feed[2].post.uri shouldBe "at://dummy.localhost/app.bsky.feed.post/image-gallery"

        feed.filter { it.post.embed is EmbedImagesView }.shouldNotBeEmpty()
        feed.filter { it.post.embed is EmbedImagesView && (it.post.embed as EmbedImagesView).images.orEmpty().size > 1 }
            .shouldNotBeEmpty()
        feed.filter { it.post.embed is EmbedVideoView }.shouldNotBeEmpty()
        feed.filter { it.post.embed is EmbedExternalView }.shouldNotBeEmpty()
        feed.filter { it.post.embed is EmbedRecordView }.shouldNotBeEmpty()
        feed.filter { it.post.embed is EmbedRecordWithMediaView }.shouldNotBeEmpty()
        feed.filter { it.post.record is GraphFollow }.shouldNotBeEmpty()

        val imageEmbeds = feed.mapNotNull { it.post.embed as? EmbedImagesView }
        val allImages = imageEmbeds.flatMap { it.images.orEmpty() }
        allImages.shouldNotBeEmpty()
        allImages.forEach { image ->
            image.thumb shouldMatch DIRECT_MEDIA_URL_REGEX
            image.fullsize shouldMatch DIRECT_MEDIA_URL_REGEX
        }

        val threadRootUri = "at://dummy.localhost/app.bsky.feed.post/conversation-root"
        val firstReplyUri = "at://dummy.localhost/app.bsky.feed.post/conversation-reply-1"
        val nestedReplyUri = "at://dummy.localhost/app.bsky.feed.post/conversation-reply-2"

        val firstReply = feed.first { it.post.uri == firstReplyUri }
        firstReply.reply?.root?.uri shouldBe threadRootUri
        firstReply.reply?.parent?.uri shouldBe threadRootUri

        val nestedReply = feed.first { it.post.uri == nestedReplyUri }
        nestedReply.reply?.root?.uri shouldBe threadRootUri
        nestedReply.reply?.parent?.uri shouldBe firstReplyUri
    }

    @Test
    fun `timeline endpoint rejects invalid bearer token`() {
        val exception = shouldThrow<ATProtocolException> {
            getTimeline(accessToken = "invalid-token", cursor = null)
        }

        exception.status shouldBe 401
    }

    private fun getTimeline(accessToken: String = DUMMY_ACCESS_TOKEN, cursor: String?): FeedGetTimelineResponse {
        return BlueskyFactory
            .instance(networkUrl)
            .feed()
            .getTimelineBlocking(
                FeedGetTimelineRequest(
                    auth = BearerTokenAuthProvider(accessToken),
                    cursor = cursor,
                )
            )
            .data
    }
}

private val DIRECT_MEDIA_URL_REGEX = Regex(
    pattern = "^https://.+\\.(png|jpg|jpeg|gif|webp)(\\?.*)?$",
    option = RegexOption.IGNORE_CASE,
)
