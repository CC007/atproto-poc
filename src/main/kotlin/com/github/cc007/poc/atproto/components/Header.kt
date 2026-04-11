package com.github.cc007.poc.atproto.components

import kotlinx.html.*

fun BODY.topBanner(csrfToken: String?) {
    header(classes = "top-banner") {
        div(classes = "brand") {
            h1 { +"BlueArt" }
            p { +"BlueSky art stream" }
        }
        form(action = "/logout", method = FormMethod.post, classes = "logout-form") {
            csrfToken?.let {
                input(type = InputType.hidden, name = "_csrf") {
                    value = csrfToken
                }
            }
            submitInput(classes = "logout-button") { value = "Logout" }
        }
    }
}