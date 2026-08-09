package com.github.cc007.blueart.endpoints.auth

import com.github.cc007.blueart.testsupport.parseHtml
import com.github.cc007.blueart.testsupport.selectRequired
import com.github.cc007.blueart.testsupport.shouldContainText
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.web.csrf.DefaultCsrfToken
import kotlin.test.Test

class LoginControllerTest {

    @Test
    fun `login form renders expected fields and default pds hint`() {
        val html = LoginController().loginForm(error = null, request = MockHttpServletRequest("GET", "/login"))
        val document = html.parseHtml()

        document.selectRequired("h1").text() shouldBe "Login"
        document.selectRequired("input[name=username]")
        document.selectRequired("input[name=password]")
        document.selectRequired("input[name=pdsUrl]").attr("placeholder") shouldBe "https://bsky.social"
        document.selectRequired("body") shouldContainText "(default: https://bsky.social)"
    }

    @Test
    fun `login form renders error message when present`() {
        val html = LoginController().loginForm(
            error = "bad_credentials",
            request = MockHttpServletRequest("GET", "/login"),
        )
        val document = html.parseHtml()

        document.selectRequired("body") shouldContainText "Login failed: bad_credentials"
    }

    @Test
    fun `login form includes csrf hidden field only when token is available`() {
        val requestWithCsrf = MockHttpServletRequest("GET", "/login").apply {
            setAttribute("_csrf", DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", "csrf-token-value"))
        }
        val htmlWithCsrf = LoginController().loginForm(error = null, request = requestWithCsrf)
        val documentWithCsrf = htmlWithCsrf.parseHtml()
        documentWithCsrf.selectRequired("input[type=hidden][name=_csrf][value=csrf-token-value]")

        val htmlWithoutCsrf = LoginController().loginForm(error = null, request = MockHttpServletRequest("GET", "/login"))
        val documentWithoutCsrf = htmlWithoutCsrf.parseHtml()
        documentWithoutCsrf.selectFirst("input[name=_csrf]").shouldBeNull()
    }
}
