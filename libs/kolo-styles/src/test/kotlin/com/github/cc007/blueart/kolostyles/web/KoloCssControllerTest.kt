package com.github.cc007.blueart.kolostyles.web

import com.github.cc007.blueart.kolostyles.compiler.KoloCssCompiler
import com.github.cc007.blueart.kolostyles.utility.SpacingGeneratorHook
import com.github.cc007.blueart.kolostyles.utility.SpacingParserHook
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class KoloCssControllerTest {

    // controller with no hooks — tokens appear as unsupported comments (isolated test behaviour)
    private val emptyController = KoloCssController(KoloCssCompiler())

    // controller with spacing hooks wired (mirrors Spring bean list wiring)
    private val defaultController = KoloCssController(
        KoloCssCompiler(
            parserHooks = listOf(SpacingParserHook()),
            generatorHooks = listOf(SpacingGeneratorHook()),
        )
    )

    @Test
    fun `kolo stylesheet with empty hooks emits unsupported comments for valid tokens`() {
        val response = emptyController.koloStylesheet(
            version = "abc123",
            kolo = "mt-2;flex;hover:mt-2"
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(
            "/* kolo-unsupported: mt-2 *//* kolo-unsupported: flex *//* kolo-unsupported: hover:mt-2 */",
            response.body
        )
    }

    @Test
    fun `kolo stylesheet notes arbitrary value tokens as unparsed comments`() {
        val response = emptyController.koloStylesheet(
            version = "abc123",
            kolo = "mt-[2px]"
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("/* kolo-unparsed: mt-[2px] */", response.body)
    }

    @Test
    fun `kolo stylesheet notes malformed tokens as unparsed comments`() {
        val response = emptyController.koloStylesheet(
            version = "abc123",
            kolo = "mt- 2"
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("/* kolo-unparsed: mt- 2 */", response.body)
    }

    @Test
    fun `kolo stylesheet with spacing hooks generates real CSS for spacing tokens`() {
        val response = defaultController.koloStylesheet(
            version = "abc123",
            kolo = "m-0;p-4"
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(".k-m-0 { margin: 0; }.k-p-4 { padding: 1rem; }", response.body)
    }

    @Test
    fun `kolo stylesheet with spacing hooks marks non-spacing tokens as unsupported`() {
        val response = defaultController.koloStylesheet(
            version = "abc123",
            kolo = "flex"
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("/* kolo-unsupported: flex */", response.body)
    }
}

