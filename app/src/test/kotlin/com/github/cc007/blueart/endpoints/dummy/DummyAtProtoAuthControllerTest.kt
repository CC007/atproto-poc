package com.github.cc007.blueart.endpoints.dummy

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import work.socialhub.kbsky.ATProtocolException
import work.socialhub.kbsky.BlueskyFactory
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionRequest
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionResponse

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

        response.handle shouldBe "dummy.localhost"
        response.accessJwt shouldBe "dummy-access-token"
        response.refreshJwt shouldBe "dummy-refresh-token"
        response.did shouldBe "did:plc:blueart-dummy"
    }

    @Test
    fun `invalid credentials return unauthorized`() {
        val exception = shouldThrow<ATProtocolException> {
            postCreateSession(
                ServerCreateSessionRequest(
                    identifier = "dummy.localhost",
                    password = "wrong-password",
                )
            )
        }

        exception.status shouldBe 401
        exception.body shouldContain "\"error\":\"AuthFailed\""
    }

    private fun postCreateSession(payload: ServerCreateSessionRequest): ServerCreateSessionResponse {
        return BlueskyFactory
            .instance(networkUrl)
            .server()
            .createSessionBlocking(payload)
            .data
    }
}


