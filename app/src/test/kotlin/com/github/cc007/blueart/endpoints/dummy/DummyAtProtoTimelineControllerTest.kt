package com.github.cc007.blueart.endpoints.dummy

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
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsFeedViewPost
import work.socialhub.kbsky.model.app.bsky.graph.GraphFollow
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

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
        val feed = getAllTimelinePosts()

        // Keep order assertions for deterministic fixture pages.
        assertEquals("at://dummy.localhost/app.bsky.feed.post/text", feed[0].post.uri)
        assertEquals("at://dummy.localhost/app.bsky.feed.post/image", feed[1].post.uri)
        assertEquals("at://dummy.localhost/app.bsky.feed.post/video", feed[2].post.uri)

        assertTrue(feed.any { it.post.embed is EmbedImagesView })
        assertTrue(feed.any { it.post.embed is EmbedVideoView })
        assertTrue(feed.any { it.post.embed is EmbedExternalView })
        assertTrue(feed.any { it.post.embed is EmbedRecordView })
        assertTrue(feed.any { it.post.embed is EmbedRecordWithMediaView })
        assertTrue(feed.any { it.post.record is GraphFollow })

        val threadRootUri = "at://dummy.localhost/app.bsky.feed.post/conversation-root"
        val firstReplyUri = "at://dummy.localhost/app.bsky.feed.post/conversation-reply-1"
        val nestedReplyUri = "at://dummy.localhost/app.bsky.feed.post/conversation-reply-2"

        val firstReply = feed.first { it.post.uri == firstReplyUri }
        assertEquals(threadRootUri, firstReply.reply?.root?.uri)
        assertEquals(threadRootUri, firstReply.reply?.parent?.uri)

        val nestedReply = feed.first { it.post.uri == nestedReplyUri }
        assertEquals(threadRootUri, nestedReply.reply?.root?.uri)
        assertEquals(firstReplyUri, nestedReply.reply?.parent?.uri)
    }

    @Test
    fun `timeline endpoint rejects invalid bearer token`() {
        val exception = assertFailsWith<ATProtocolException> {
            getTimeline(accessToken = "invalid-token", cursor = null)
        }

        assertEquals(401, exception.status)
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

    private fun getAllTimelinePosts(): List<FeedDefsFeedViewPost> {
        val all = mutableListOf<FeedDefsFeedViewPost>()
        var cursor: String? = null

        while (true) {
            val page = getTimeline(cursor = cursor)
            all += page.feed
            cursor = page.cursor
            if (cursor == null) {
                break
            }
        }

        return all
    }
}
