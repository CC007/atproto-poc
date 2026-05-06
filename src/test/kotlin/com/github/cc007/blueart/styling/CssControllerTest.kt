package com.github.cc007.blueart.styling

import kotlin.test.Test
import kotlin.test.assertTrue

class CssControllerTest {

    private val controller = CssController()

    @Test
    fun `browse stylesheet imports legacy browse css`() {
        val css = controller.browseStylesheet()

        assertTrue(css.contains("@import"))
        assertTrue(css.contains("/css/browse.css"))
    }

    @Test
    fun `art stylesheet imports legacy art css`() {
        val css = controller.artStylesheet()

        assertTrue(css.contains("@import"))
        assertTrue(css.contains("/css/art.css"))
    }
}

