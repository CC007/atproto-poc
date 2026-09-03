package com.github.cc007.blueart.components

import com.github.cc007.blueart.kolostyles.dsl.kolo
import com.github.cc007.blueart.kolostyles.dsl.layout.display.flex
import com.github.cc007.blueart.kolostyles.dsl.layout.offset.top
import com.github.cc007.blueart.kolostyles.dsl.layout.sticky
import com.github.cc007.blueart.kolostyles.dsl.layout.z
import com.github.cc007.blueart.kolostyles.dsl.spacing.m
import com.github.cc007.blueart.kolostyles.dsl.spacing.px
import com.github.cc007.blueart.kolostyles.dsl.spacing.py
import kotlinx.html.*

fun BODY.topBanner(csrfToken: String?) {
    header(classes = "top-banner") {
        kolo { flex; sticky; top(0); z(10); px(3); py(5) }
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