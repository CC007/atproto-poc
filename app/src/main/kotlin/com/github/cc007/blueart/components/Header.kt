package com.github.cc007.blueart.components

import com.github.cc007.blueart.kolostyles.render.kolo
import com.github.cc007.blueart.kolostyles.render.m
import com.github.cc007.blueart.kolostyles.render.px
import com.github.cc007.blueart.kolostyles.render.py
import kotlinx.html.*

fun BODY.topBanner(csrfToken: String?) {
    header(classes = "top-banner") {
        kolo { px(3); py(5) }
        div(classes = "brand") {
            h1 {
                kolo { m(0) }
                +"BlueArt"
            }
        }
        form(action = "/logout", method = FormMethod.post, classes = "logout-form") {
            kolo { m(0) }
            csrfToken?.let {
                input(type = InputType.hidden, name = "_csrf") {
                    value = csrfToken
                }
            }
            submitInput(classes = "logout-button") {
                kolo { px(4); py(2) }
                value = "Logout"
            }
        }
    }
}