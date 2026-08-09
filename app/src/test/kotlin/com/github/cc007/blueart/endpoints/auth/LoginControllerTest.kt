package com.github.cc007.blueart.endpoints.auth

import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.web.csrf.DefaultCsrfToken
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LoginControllerTest {

    @Test
    fun `login form renders expected fields and default pds hint`() {
        val html = LoginController().loginForm(error = null, request = MockHttpServletRequest("GET", "/login"))

        assertTrue(html.contains("<h1>Login</h1>"))
        assertTrue(html.contains("name=\"username\""))
        assertTrue(html.contains("name=\"password\""))
        assertTrue(html.contains("name=\"pdsUrl\""))
        assertTrue(html.contains("placeholder=\"https://bsky.social\""))
        assertTrue(html.contains("(default: https://bsky.social)"))
    }

    @Test
    fun `login form renders error message when present`() {
        val html = LoginController().loginForm(
            error = "bad_credentials",
            request = MockHttpServletRequest("GET", "/login"),
        )

        assertTrue(html.contains("Login failed: bad_credentials"))
    }

    @Test
    fun `login form includes csrf hidden field only when token is available`() {
        val requestWithCsrf = MockHttpServletRequest("GET", "/login").apply {
            setAttribute("_csrf", DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", "csrf-token-value"))
        }
        val htmlWithCsrf = LoginController().loginForm(error = null, request = requestWithCsrf)
        assertTrue(htmlWithCsrf.contains("type=\"hidden\""))
        assertTrue(htmlWithCsrf.contains("name=\"_csrf\""))
        assertTrue(htmlWithCsrf.contains("value=\"csrf-token-value\""))

        val htmlWithoutCsrf = LoginController().loginForm(error = null, request = MockHttpServletRequest("GET", "/login"))
        assertFalse(htmlWithoutCsrf.contains("name=\"_csrf\""))
    }
}
