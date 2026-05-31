package com.github.cc007.blueart.endpoints.dummy

import kotlinx.serialization.json.Json
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import work.socialhub.kbsky.api.entity.app.bsky.feed.FeedGetTimelineRequest
import work.socialhub.kbsky.api.entity.app.bsky.feed.FeedGetTimelineResponse
import work.socialhub.kbsky.auth.BearerTokenAuthProvider

private val xrpcJson = Json {
    encodeDefaults = true
    explicitNulls = false
}

@RestController
class DummyAtProtoTimelineController {

    @GetMapping(
        "/xrpc/app.bsky.feed.getTimeline",
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun getTimeline(
        @RequestHeader(HttpHeaders.AUTHORIZATION, required = false) authorization: String?,
        @RequestParam(required = false) algorithm: String?,
        @RequestParam(required = false) limit: Int?,
        @RequestParam(required = false) cursor: String?,
    ): ResponseEntity<String> {
        val accessToken = authorization
            ?.takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")
            ?.trim()
            ?.takeIf { it == DUMMY_ACCESS_TOKEN }
            ?: return unauthorizedResponse()

        val request = FeedGetTimelineRequest(
            auth = BearerTokenAuthProvider(accessToken),
            algorithm = algorithm,
            limit = limit,
            cursor = cursor,
        )

        val body = xrpcJson.encodeToString(FeedGetTimelineResponse.serializer(), getTimeline(request))
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
    }

    internal fun getTimeline(request: FeedGetTimelineRequest): FeedGetTimelineResponse {
        return timelineFixtureForCursor(request.cursor)
    }

    private fun unauthorizedResponse(): ResponseEntity<String> {
        val body = """{"error":"AuthRequired","message":"Valid dummy access token is required."}"""
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
    }
}
