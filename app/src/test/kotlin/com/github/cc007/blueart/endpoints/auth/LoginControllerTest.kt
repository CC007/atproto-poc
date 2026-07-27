package com.github.cc007.blueart.endpoints.auth

import org.springframework.mock.web.MockHttpServletRequest
import kotlin.test.Test
import kotlin.test.assertTrue

class LoginControllerTest {

    @Test
    fun `login form exposes localhost as a network option`() {
        val html = LoginController().loginForm(error = null, request = MockHttpServletRequest("GET", "/login"))

        assertTrue(html.contains("value=\"localhost\""))
        assertTrue(html.contains("localhost uses the dummy account"))
    }
}
