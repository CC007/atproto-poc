package com.github.cc007.blueart.endpoints.dummy

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionRequest
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionResponse

@RestController
class DummyAtProtoAuthController {

    @PostMapping(
        "/xrpc/com.atproto.server.createSession",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun createSession(
        @RequestBody request: ServerCreateSessionRequest,
    ): ResponseEntity<Any> {
        if (request.identifier == DUMMY_HANDLE && request.password == DUMMY_PASSWORD) {
            return ResponseEntity.ok(dummySessionResponse())
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            mapOf(
                "error" to "AuthFailed",
                "message" to "Invalid identifier or password.",
            )
        )
    }

    private fun dummySessionResponse(): ServerCreateSessionResponse {
        return ServerCreateSessionResponse(
            accessJwt = DUMMY_ACCESS_TOKEN,
            refreshJwt = DUMMY_REFRESH_TOKEN,
            handle = DUMMY_HANDLE,
            did = DUMMY_DID,
            email = DUMMY_EMAIL,
            emailConfirmed = true,
            active = true,
        )
    }
}
