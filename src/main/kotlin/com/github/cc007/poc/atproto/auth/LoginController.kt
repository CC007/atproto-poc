package com.github.cc007.poc.atproto.auth

import jakarta.servlet.http.HttpServletRequest
import kotlinx.html.*
import kotlinx.html.FormMethod.post
import kotlinx.html.stream.createHTML
import org.springframework.http.MediaType
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import work.socialhub.kbsky.domain.Service

@Controller
class LoginController {

    @GetMapping("/login", "", "/", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun loginForm(
        @RequestParam(name = "error", required = false) error: String?,
        request: HttpServletRequest
    ): String {
        val csrfToken = (request.getAttribute("_csrf") as? CsrfToken)?.token
        return createHTML().html {
            head {
                title("Login")
            }
            body {
                h1 { +"Login" }
                if (error != null) {
                    p { style = "color:red;"; +"Login failed: $error" }
                }
                form(action = "/login", method = post) {
                    if (csrfToken != null) {
                        input(type = InputType.hidden, name = "_csrf") {
                            value = csrfToken
                        }
                    }
                    p {
                        label { +"Username: " }
                        textInput(name = "username") { required = true }
                    }
                    p {
                        label { +"Password: " }
                        passwordInput(name = "password") { required = true }
                    }
                    p {
                        label { +"Network URL: " }
                        textInput(name = "serviceUrl") {
                            placeholder = Service.BSKY_SOCIAL.uri
                        }
                        span { +" (default: ${Service.BSKY_SOCIAL.uri})" }
                    }
                    p {
                        submitInput { value = "Login" }
                    }
                }
            }
        }
    }
}