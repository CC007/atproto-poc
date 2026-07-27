package com.github.cc007.blueart.endpoints.auth

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import java.net.CookieManager
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DummyLoginFlowTest(
    @LocalServerPort private val port: Int,
) {
    private val cookieManager = CookieManager()
    private val client = HttpClient.newBuilder()
        .cookieHandler(cookieManager)
        .followRedirects(HttpClient.Redirect.NEVER)
        .build()

    @Test
    fun `localhost login uses dummy auth and browse detail flow`() {
        val loginPage = get("/login")
        val csrfToken = Regex("""name="_csrf"[^>]*value="([^"]+)"""")
            .find(loginPage.body())
            ?.groupValues
            ?.get(1)
            ?: fail("CSRF token not found on login page")

        val login = postForm(
            "/login",
            mapOf(
                "username" to "dummy.localhost",
                "password" to "1234",
                "pdsUrl" to "localhost",
                "_csrf" to csrfToken,
            ),
        )

        assertEquals(302, login.statusCode())
        val locationHeader = login.headers().firstValue("location")
        assertTrue(locationHeader.isPresent)
        assertTrue(locationHeader.get().contains("/browse"))

        val browse = get("/browse")
        assertEquals(200, browse.statusCode())
        assertContains(browse.body(), "Browse Timeline")
        assertContains(browse.body(), "BlueArt Dummy")
        assertContains(browse.body(), "embed-media-grid")
        assertContains(browse.body(), "Open artwork")

        val detailUri = "at://dummy.localhost/app.bsky.feed.post/image-gallery"
        val detail = get("/art/bafyreidummyimage-gallery?uri=${URLEncoder.encode(detailUri, StandardCharsets.UTF_8)}")
        assertEquals(200, detail.statusCode())
        assertContains(detail.body(), "Multi-image embed to exercise the gallery layout.")
        assertContains(detail.body(), "Description")
        assertContains(detail.body(), "art-image-grid")
    }

    private fun get(path: String): HttpResponse<String> {
        return client.send(
            HttpRequest.newBuilder(uri(path))
                .GET()
                .build(),
            HttpResponse.BodyHandlers.ofString(),
        )
    }

    private fun postForm(path: String, form: Map<String, String>): HttpResponse<String> {
        val body = form.entries.joinToString("&") { (key, value) ->
            "${URLEncoder.encode(key, StandardCharsets.UTF_8)}=${URLEncoder.encode(value, StandardCharsets.UTF_8)}"
        }
        return client.send(
            HttpRequest.newBuilder(uri(path))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build(),
            HttpResponse.BodyHandlers.ofString(),
        )
    }

    private fun uri(path: String): URI = URI.create("http://localhost:$port$path")
}
