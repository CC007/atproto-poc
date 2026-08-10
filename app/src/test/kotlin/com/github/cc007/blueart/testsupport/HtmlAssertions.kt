package com.github.cc007.blueart.testsupport

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldContain
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

fun String.parseHtml(): Document = Jsoup.parse(this)

fun Document.selectRequired(selector: String): Element {
    return selectFirst(selector).shouldNotBeNull()
}

infix fun Element.shouldContainText(expected: String) {
    text().shouldContain(expected)
}
