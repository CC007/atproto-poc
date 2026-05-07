package com.github.cc007.blueart.endpoints.styling

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

class CssControllerTest {

    private val controller = CssController()

    @Test
    fun `browse stylesheet encodes all browse css rules in generated output`() {
        val css = controller.browseStylesheet()
        val legacyCss = readResource("/static/css/browse.css")

        assertTrue(!css.contains("@import"), "Generated browse CSS should not contain @import bridge")
        assertContainsAllRuleHeaders(legacyCss, css)
    }

    @Test
    fun `art stylesheet encodes all art css rules in generated output`() {
        val css = controller.artStylesheet()
        val legacyCss = readResource("/static/css/art.css")

        assertTrue(!css.contains("@import"), "Generated art CSS should not contain @import bridge")
        assertContainsAllRuleHeaders(legacyCss, css)
    }

    private fun assertContainsAllRuleHeaders(legacyCss: String, generatedCss: String) {
        val legacyHeaders = extractRuleHeaders(legacyCss)
        val generatedHeaders = extractRuleHeaders(generatedCss)
        val missingHeaders = legacyHeaders - generatedHeaders

        if (missingHeaders.isNotEmpty()) {
            fail("Generated CSS is missing rule headers: ${missingHeaders.joinToString()}")
        }
    }

    private fun readResource(path: String): String {
        return javaClass.getResource(path)?.readText()
            ?: fail("Missing test resource: $path")
    }

    private fun extractRuleHeaders(css: String): Set<String> {
        val headers = mutableSetOf<String>()
        var segmentStart = 0
        var index = 0

        while (index < css.length) {
            when (css[index]) {
                '{' -> {
                    val header = css.substring(segmentStart, index)
                        .trim()
                        .replace(Regex("\\s+"), " ")
                    if (header.isNotEmpty()) {
                        headers.add(header)
                    }
                    segmentStart = index + 1
                }

                '}' -> {
                    segmentStart = index + 1
                }
            }
            index += 1
        }

        return headers
    }
}

