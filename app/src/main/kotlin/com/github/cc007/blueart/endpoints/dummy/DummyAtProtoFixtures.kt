package com.github.cc007.blueart.endpoints.dummy

import work.socialhub.kbsky.api.entity.app.bsky.feed.FeedGetTimelineResponse
import work.socialhub.kbsky.model.app.bsky.actor.ActorDefsProfileViewBasic
import work.socialhub.kbsky.model.app.bsky.embed.*
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsFeedViewPost
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsPostView
import work.socialhub.kbsky.model.app.bsky.feed.FeedDefsReplyRef
import work.socialhub.kbsky.model.app.bsky.feed.FeedPost
import work.socialhub.kbsky.model.app.bsky.graph.GraphFollow
import work.socialhub.kbsky.model.share.RecordUnion

internal const val DUMMY_HANDLE = "dummy.localhost"
internal const val DUMMY_PASSWORD = "1234"
internal const val DUMMY_ACCESS_TOKEN = "dummy-access-token"
internal const val DUMMY_REFRESH_TOKEN = "dummy-refresh-token"
internal const val DUMMY_DID = "did:plc:blueart-dummy"
internal const val DUMMY_EMAIL = "dummy.localhost@localhost"

private const val CURSOR_PAGE_2 = "dummy-cursor-2"
private const val CURSOR_PAGE_3 = "dummy-cursor-3"
private const val CURSOR_PAGE_4 = "dummy-cursor-4"

private const val FIXTURE_TIMESTAMP = "2026-06-01T00:00:00Z"

private val dummyAuthor = ActorDefsProfileViewBasic(
    did = DUMMY_DID,
    handle = DUMMY_HANDLE,
    displayName = "BlueArt Dummy",
    avatar = "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png",
)

private val conversationRootPost = postView(
    suffix = "conversation-root",
    text = "Thread root: this one has two replies in fixtures.",
    embed = EmbedImagesView(
        images = listOf(
            EmbedImagesViewImage(
                thumb = "https://raw.githubusercontent.com/github/explore/main/topics/github/github.png",
                fullsize = "https://raw.githubusercontent.com/github/explore/main/topics/github/github.png",
                alt = "Thread root artwork fixture",
            )
        )
    ),
    replyCount = 2,
)

private val conversationReplyPost = postView(
    suffix = "conversation-reply-1",
    text = "First reply in the dummy thread.",
)

private val conversationReplyToReplyPost = postView(
    suffix = "conversation-reply-2",
    text = "Nested reply (reply to the first reply).",
)

private val timelinePosts = listOf(
    timelinePost(
        suffix = "text",
        text = "Text-only post for deterministic preview coverage.",
        embed = null,
    ),
    timelinePost(
        suffix = "image",
        text = "Single image embed using a stable remote URL.",
        embed = EmbedImagesView(
            images = listOf(
                EmbedImagesViewImage(
                    thumb = "https://raw.githubusercontent.com/github/explore/main/topics/kotlin/kotlin.png",
                    fullsize = "https://raw.githubusercontent.com/github/explore/main/topics/kotlin/kotlin.png",
                    alt = "Example image fixture",
                )
            )
        ),
    ),
    timelinePost(
        suffix = "image-gallery",
        text = "Multi-image embed to exercise the gallery layout.",
        embed = EmbedImagesView(
            images = listOf(
                EmbedImagesViewImage(
                    thumb = "https://raw.githubusercontent.com/github/explore/main/topics/spring-boot/spring-boot.png",
                    fullsize = "https://raw.githubusercontent.com/github/explore/main/topics/spring-boot/spring-boot.png",
                    alt = "Gallery image one fixture",
                ),
                EmbedImagesViewImage(
                    thumb = "https://raw.githubusercontent.com/github/explore/main/topics/docker/docker.png",
                    fullsize = "https://raw.githubusercontent.com/github/explore/main/topics/docker/docker.png",
                    alt = "Gallery image two fixture",
                ),
            )
        ),
    ),
    timelinePost(
        suffix = "video",
        text = "Video-style post represented by a stable thumbnail.",
        embed = EmbedVideoView(
            cid = "bafyreidummytimelinevideo",
            playlist = "https://media.w3.org/2010/05/sintel/trailer.mp4",
            thumbnail = "https://raw.githubusercontent.com/github/explore/main/topics/java/java.png",
            alt = "Video thumbnail fixture",
        ),
    ),
    timelinePost(
        suffix = "gif",
        text = "GIF-style preview represented through an external embed.",
        embed = EmbedExternalView(
            external = EmbedExternalViewExternal(
                uri = "https://upload.wikimedia.org/wikipedia/commons/2/2c/Rotating_earth_%28large%29.gif",
                title = "Rotating earth GIF fixture",
                description = "Stable remote GIF used for dummy timeline previews.",
                thumb = "https://upload.wikimedia.org/wikipedia/commons/2/2c/Rotating_earth_%28large%29.gif",
            )
        ),
    ),
    timelinePost(
        suffix = "record-embed",
        text = "Quote/embed-record coverage fixture.",
        embed = EmbedRecordView(
            record = EmbedRecordViewRecord(
                uri = "at://$DUMMY_HANDLE/app.bsky.feed.post/quoted-record",
                cid = "bafyreidummyquotedrecord",
                author = dummyAuthor,
                value = FeedPost(
                    text = "Quoted record fixture text.",
                    createdAt = FIXTURE_TIMESTAMP,
                ),
                likeCount = 3,
                quoteCount = 1,
                repostCount = 2,
                replyCount = 0,
                bookmarkCount = 0,
                indexedAt = FIXTURE_TIMESTAMP,
            )
        ),
    ),
    timelinePost(
        suffix = "record-with-media",
        text = "EmbedRecordWithMedia coverage fixture.",
        embed = EmbedRecordWithMediaView(
            media = EmbedImagesView(
                images = listOf(
                    EmbedImagesViewImage(
                        thumb = "https://raw.githubusercontent.com/github/explore/main/topics/gradle/gradle.png",
                        fullsize = "https://raw.githubusercontent.com/github/explore/main/topics/gradle/gradle.png",
                        alt = "Record-with-media image fixture",
                    )
                )
            ),
            record = EmbedRecordView(
                record = EmbedRecordViewRecord(
                    uri = "at://$DUMMY_HANDLE/app.bsky.feed.post/record-with-media-quoted",
                    cid = "bafyreidummyrecordwithmediaquoted",
                    author = dummyAuthor,
                    value = FeedPost(
                        text = "Quoted record for record-with-media fixture.",
                        createdAt = FIXTURE_TIMESTAMP,
                    ),
                    likeCount = 2,
                    quoteCount = 0,
                    repostCount = 1,
                    replyCount = 1,
                    bookmarkCount = 0,
                    indexedAt = FIXTURE_TIMESTAMP,
                )
            ),
        ),
    ),
    timelinePost(
        suffix = "unsupported-post",
        text = "Unsupported post-type fixture (non-FeedPost record).",
        embed = null,
        record = GraphFollow(
            subject = "did:plc:dummy-follow-target",
            createdAt = FIXTURE_TIMESTAMP,
        ),
    ),
    FeedDefsFeedViewPost(post = conversationRootPost),
    FeedDefsFeedViewPost(
        post = conversationReplyPost,
        reply = FeedDefsReplyRef(
            root = conversationRootPost,
            parent = conversationRootPost,
        )
    ),
    FeedDefsFeedViewPost(
        post = conversationReplyToReplyPost,
        reply = FeedDefsReplyRef(
            root = conversationRootPost,
            parent = conversationReplyPost,
        )
    ),
)

internal fun timelineFixtureForCursor(cursor: String?): FeedGetTimelineResponse {
    return when (cursor) {
        null -> FeedGetTimelineResponse(CURSOR_PAGE_2, timelinePosts.subList(0, 3))
        CURSOR_PAGE_2 -> FeedGetTimelineResponse(CURSOR_PAGE_3, timelinePosts.subList(3, 6))
        CURSOR_PAGE_3 -> FeedGetTimelineResponse(CURSOR_PAGE_4, timelinePosts.subList(6, 9))
        CURSOR_PAGE_4 -> FeedGetTimelineResponse(null, timelinePosts.subList(9, 11))
        else -> FeedGetTimelineResponse(null, emptyList())
    }
}

private fun timelinePost(
    suffix: String,
    text: String,
    embed: EmbedViewUnion?,
    record: RecordUnion = FeedPost(
        text = text,
        createdAt = FIXTURE_TIMESTAMP,
    ),
): FeedDefsFeedViewPost {
    return FeedDefsFeedViewPost(
        post = postView(
            suffix = suffix,
            text = text,
            embed = embed,
            record = record,
        )
    )
}

private fun postView(
    suffix: String,
    text: String,
    embed: EmbedViewUnion? = null,
    record: RecordUnion = FeedPost(
        text = text,
        createdAt = FIXTURE_TIMESTAMP,
    ),
    replyCount: Int = 0,
): FeedDefsPostView {
    val uri = "at://$DUMMY_HANDLE/app.bsky.feed.post/$suffix"
    return FeedDefsPostView(
        uri = uri,
        cid = "bafyreidummy$suffix",
        author = dummyAuthor,
        record = record,
        embed = embed,
        replyCount = replyCount,
        repostCount = 0,
        likeCount = 1,
        bookmarkCount = 0,
        quoteCount = 0,
        indexedAt = FIXTURE_TIMESTAMP,
    )
}
