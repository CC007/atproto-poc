package com.github.cc007.poc.atproto.browse

import com.github.cc007.poc.atproto.auth.AtProtoAuthentication
import jakarta.servlet.http.HttpServletRequest
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class BrowseController {

    @GetMapping("/browse", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun index(
        request: HttpServletRequest
    ): String {
        val csrfToken = (request.getAttribute("_csrf") as? CsrfToken)?.token
        return with(SecurityContextHolder.getContext().authentication as AtProtoAuthentication) {
            createHTML().html {
                head {
                    title("Browse")
                }
                body {
                    h1 { +"Hello world!" }
                    p {
                        +"Access token: $accessToken"
                        br
                        +"Refresh token: $refreshToken"
                    }
                    form(action = "/logout", method = FormMethod.post) {
                        if (csrfToken != null) {
                            input(type = InputType.hidden, name = "_csrf") {
                                value = csrfToken
                            }
                        }
                        p {
                            submitInput { value = "Logout" }
                        }
                    }
                }
            }
        }
    }
}