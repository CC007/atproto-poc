package com.github.cc007.blueart.endpoints.dummy

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import work.socialhub.kbsky.ATProtocolException
import work.socialhub.kbsky.BlueskyFactory
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionRequest
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionResponse
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DummyAtProtoAuthControllerTest(
    @LocalServerPort private val port: Int,
) {
    private lateinit var networkUrl: String

    @BeforeEach
    fun setUp() {
        networkUrl = "http://localhost:$port"
    }

    @Test
    fun `valid dummy credentials return deterministic session response`() {
        val response = postCreateSession(
            ServerCreateSessionRequest(
                identifier = "dummy.localhost",
                password = "1234",
            )
        )

        assertEquals("dummy.localhost", response.handle)
        assertEquals("dummy-access-token", response.accessJwt)
        assertEquals("dummy-refresh-token", response.refreshJwt)
        assertEquals("did:plc:blueart-dummy", response.did)
    }

    @Test
    fun `invalid credentials return unauthorized`() {
        val exception = assertFailsWith<ATProtocolException> {
            postCreateSession(
                ServerCreateSessionRequest(
                    identifier = "dummy.localhost",
                    password = "wrong-password",
                )
            )
        }

        assertEquals(401, exception.status)
        assertTrue(exception.body.orEmpty().contains("\"error\":\"AuthFailed\""))
    }

    private fun postCreateSession(payload: ServerCreateSessionRequest): ServerCreateSessionResponse {
        return BlueskyFactory
            .instance(networkUrl)
            .server()
            .createSessionBlocking(payload)
            .data
    }
}



