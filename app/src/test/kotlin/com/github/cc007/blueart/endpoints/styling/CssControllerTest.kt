package com.github.cc007.blueart.endpoints.styling

import kotlin.test.Test
import kotlin.test.assertFalse
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

    @Test
    fun `browse stylesheet omits display declarations for migrated selectors`() {
        val css = controller.browseStylesheet()
        listOf(
            "body.browse-body",
            ".top-banner",
            ".browse-layout",
            ".sidebar-nav",
            ".content-top",
            ".filter-row",
            ".feed-grid",
            ".post-card",
            ".post-author",
            ".author-meta",
            ".author-name",
            ".embed-media",
            ".embed-media-grid",
            ".embed-media-grid-side",
            ".embed-blur-clip",
            ".post-stats",
            ".post-stat-item",
            ".post-stat-icon",
        ).forEach { selector -> assertSelectorHasNoDisplay(css, selector) }
    }

    @Test
    fun `art stylesheet omits display declarations for migrated selectors`() {
        val css = controller.artStylesheet()
        listOf(
            ".art-content",
            ".content-top",
            ".art-image-grid",
            ".art-image",
            ".comments",
            ".comment",
            ".comment-header",
            ".comment-meta",
        ).forEach { selector -> assertSelectorHasNoDisplay(css, selector) }
    }

    @Test
    fun `browse stylesheet omits migrated font declarations`() {
        val css = controller.browseStylesheet()
        listOf(
            "body.browse-body",
            ".richtext-mention",
            ".richtext-tag",
        ).forEach { selector -> assertSelectorHasNoFontDeclarations(css, selector) }
    }

    @Test
    fun `art stylesheet omits migrated font declarations`() {
        val css = controller.artStylesheet()
        listOf(
            "body.art-body",
            ".art-title",
            ".art-description h2, .comments h2",
            ".richtext-mention",
            ".richtext-tag",
        ).forEach { selector -> assertSelectorHasNoFontDeclarations(css, selector) }
    }

    @Test
    fun `stylesheets retain non migrated typography declarations as explicit exceptions`() {
        val browseCss = controller.browseStylesheet()
        val artCss = controller.artStylesheet()

        assertSelectorContainsDeclaration(browseCss, ".brand h1", "font-size:")
        assertSelectorContainsDeclaration(browseCss, ".content-top h1", "font-size:")
        assertSelectorContainsDeclaration(artCss, ".art-byline", "font-size:")
        assertSelectorContainsDeclaration(artCss, ".comment-author", "font-size:")
    }

    @Test
    fun `browse stylesheet omits sizing declarations for migrated selectors`() {
        val css = controller.browseStylesheet()
        listOf(
            "body.browse-body",
            ".browse-layout",
            ".browse-content",
            ".post-card",
            ".parent-post .post-card",
            ".post-content",
            ".embed-media",
            ".embed-blur-clip",
            ".embed-media-grid",
            ".post-card-media .embed-media-single",
            ".post-card-media .embed-blur-clip",
        ).forEach { selector -> assertSelectorHasNoSizingDeclarations(css, selector) }
    }

    @Test
    fun `art stylesheet omits sizing declarations for migrated selectors`() {
        val css = controller.artStylesheet()
        listOf(
            "body.art-body",
            ".art-layout",
            ".art-image",
        ).forEach { selector -> assertSelectorHasNoSizingDeclarations(css, selector) }
    }

    @Test
    fun `stylesheets retain non migrated sizing declarations as explicit exceptions`() {
        val browseCss = controller.browseStylesheet()
        val artCss = controller.artStylesheet()

        assertSelectorContainsDeclaration(
            browseCss,
            ".post-stat-icon",
            "height:"
        )
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

    private fun assertSelectorHasNoDisplay(css: String, selector: String) {
        val declarations = findSelectorDeclarations(css, selector)
        assertFalse(
            declarations.any { it.contains("display:") },
            "Selector $selector should not contain display declarations after migration"
        )
    }

    private fun assertSelectorHasNoFontDeclarations(css: String, selector: String) {
        val declarations = findSelectorDeclarations(css, selector, allowMissing = true).joinToString("\n")
        if (declarations.isEmpty()) {
            return
        }
        assertFalse(
            declarations.contains("font-family:"),
            "Selector $selector should not contain font-family declarations after migration"
        )
        assertFalse(
            declarations.contains("font-size:"),
            "Selector $selector should not contain font-size declarations after migration"
        )
        assertFalse(
            declarations.contains("font-weight:"),
            "Selector $selector should not contain font-weight declarations after migration"
        )
    }

    private fun assertSelectorHasNoSizingDeclarations(css: String, selector: String) {
        val declarations = findSelectorDeclarations(css, selector, allowMissing = true).joinToString("\n")
        if (declarations.isEmpty()) {
            return
        }
        assertFalse(
            declarations.contains("width:"),
            "Selector $selector should not contain width declarations after migration"
        )
        assertFalse(
            declarations.contains("height:"),
            "Selector $selector should not contain height declarations after migration"
        )
        assertFalse(
            declarations.contains("min-width:"),
            "Selector $selector should not contain min-width declarations after migration"
        )
        assertFalse(
            declarations.contains("max-width:"),
            "Selector $selector should not contain max-width declarations after migration"
        )
        assertFalse(
            declarations.contains("min-height:"),
            "Selector $selector should not contain min-height declarations after migration"
        )
        assertFalse(
            declarations.contains("max-height:"),
            "Selector $selector should not contain max-height declarations after migration"
        )
    }

    private fun assertSelectorContainsDeclaration(css: String, selector: String, declarationPrefix: String) {
        val declarations = findSelectorDeclarations(css, selector)
        assertTrue(
            declarations.any { it.contains(declarationPrefix) },
            "Selector $selector should contain $declarationPrefix"
        )
    }

    private fun findSelectorDeclarations(css: String, selector: String, allowMissing: Boolean = false): List<String> {
        val blocks = Regex("([^{}]+)\\{([^}]*)\\}")
            .findAll(css)
            .mapNotNull { match ->
                val selectors = match.groupValues[1]
                    .split(",")
                    .map { it.trim() }
                if (selector in selectors) {
                    match.groupValues[2]
                } else {
                    null
                }
            }
            .toList()

        if (blocks.isEmpty() && !allowMissing) {
            fail("Selector not found in generated CSS: $selector")
        }
        return blocks
    }
}
