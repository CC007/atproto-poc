package com.github.cc007.poc.atproto.components

import kotlinx.html.*

fun BODY.topBanner(csrfToken: String?) {
    header {
        form(action = "/logout", method = FormMethod.post) {
            csrfToken?.let {
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