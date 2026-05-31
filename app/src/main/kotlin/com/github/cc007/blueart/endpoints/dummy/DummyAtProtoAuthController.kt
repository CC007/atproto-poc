package com.github.cc007.blueart.endpoints.dummy

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionRequest
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionResponse

private const val DUMMY_HANDLE = "dummy.localhost"
private const val DUMMY_PASSWORD = "1234"

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
            accessJwt = "dummy-access-token",
            refreshJwt = "dummy-refresh-token",
            handle = DUMMY_HANDLE,
            did = "did:plc:blueart-dummy",
            email = "dummy.localhost@localhost",
            emailConfirmed = true,
            active = true,
        )
    }
}

