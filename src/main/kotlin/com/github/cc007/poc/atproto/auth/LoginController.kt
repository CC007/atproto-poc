package com.github.cc007.poc.atproto.auth

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.view.RedirectView
import work.socialhub.kbsky.ATProtocolException
import work.socialhub.kbsky.BlueskyFactory
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionRequest
import work.socialhub.kbsky.api.entity.com.atproto.server.ServerCreateSessionResponse
import work.socialhub.kbsky.api.entity.share.Response
import work.socialhub.kbsky.domain.Service
import org.springframework.security.web.csrf.CsrfToken
import jakarta.servlet.http.HttpServletRequest

@Controller
class LoginController {
    @GetMapping("/login", produces = [MediaType.TEXT_HTML_VALUE])
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
                form(action = "/login", method = FormMethod.post) {
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

    @PostMapping("/login")
    fun login(
        @RequestParam username: String,
        @RequestParam password: String,
        @RequestParam(required = false) networkUrl: String?
    ): RedirectView {
        val networkUrl = networkUrl?.takeIf { it.isNotBlank() } ?: Service.BSKY_SOCIAL.uri
        val accessJwt = authenticate(username, password, networkUrl)
        // TODO store token as cookie (maybe encrypted to prevent token cookie stealing)
        return RedirectView("/")
    }
}

private fun authenticate(username: String, password: String, networkUrl: String): String {
    val response: Response<ServerCreateSessionResponse> = try {
        BlueskyFactory
            .instance(networkUrl)
            .server()
            .createSession(
                ServerCreateSessionRequest().also {
                    it.identifier = username
                    it.password = password
                }
            )
    } catch (e: ATProtocolException) {
        println("Failed login attempt: ${e.status}: ${e.message} (${e.body})")
        throw when {
            e.status == 401 -> ResponseStatusException(HttpStatus.UNAUTHORIZED, e.message)
            e.message == "Input must have the property \"password\"" -> ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                e.message
            )

            e.message == "Input must have the property \"identifier\"" -> ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                e.message
            )

            e.status?.div(100) == 4 -> ResponseStatusException(e.status!!, e.message, e.cause)
            else -> ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong. Try again later.")
        }
    }
    println(response.json)
    return response.data.accessJwt
}