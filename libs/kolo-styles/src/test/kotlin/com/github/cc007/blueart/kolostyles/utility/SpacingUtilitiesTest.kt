package com.github.cc007.blueart.kolostyles.utility

import com.github.cc007.blueart.kolostyles.compiler.KoloCssCompiler
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SpacingUtilitiesTest {

    private val parser = SpacingParserHook()
    private val generator = SpacingGeneratorHook()
    private val compiler = KoloCssCompiler(
        parserHooks = listOf(parser),
        generatorHooks = listOf(generator),
    )

    // --- parser ---

    @Test
    fun `parser accepts m-0 and returns zero declaration`() {
        val def = parser.parse("m-0")
        assertNotNull(def)
        assertEquals("m-0", def.token)
        assertEquals("margin: 0;", def.cssDeclaration)
    }

    @Test
    fun `parser accepts mt-4 and returns correct rem value`() {
        val def = parser.parse("mt-4")
        assertNotNull(def)
        assertEquals("margin-top: 1rem;", def.cssDeclaration)
    }

    @Test
    fun `parser accepts mb-3 and returns correct rem value`() {
        val def = parser.parse("mb-3")
        assertNotNull(def)
        assertEquals("margin-bottom: 0.75rem;", def.cssDeclaration)
    }

    @Test
    fun `parser accepts p-2 and returns correct rem value`() {
        val def = parser.parse("p-2")
        assertNotNull(def)
        assertEquals("padding: 0.5rem;", def.cssDeclaration)
    }

    @Test
    fun `parser accepts mx-4 and generates horizontal shorthand`() {
        val def = parser.parse("mx-4")
        assertNotNull(def)
        assertEquals("margin-left: 1rem; margin-right: 1rem;", def.cssDeclaration)
    }

    @Test
    fun `parser accepts py-2 and generates vertical shorthand`() {
        val def = parser.parse("py-2")
        assertNotNull(def)
        assertEquals("padding-top: 0.5rem; padding-bottom: 0.5rem;", def.cssDeclaration)
    }

    @Test
    fun `parser accepts hover variant`() {
        val def = parser.parse("hover:mt-2")
        assertNotNull(def)
        assertEquals("margin-top: 0.5rem;", def.cssDeclaration)
    }

    @Test
    fun `parser accepts md media variant`() {
        val def = parser.parse("md:p-4")
        assertNotNull(def)
        assertEquals("padding: 1rem;", def.cssDeclaration)
    }

    @Test
    fun `parser rejects unknown token`() {
        assertNull(parser.parse("flex"))
    }

    @Test
    fun `parser rejects arbitrary value token`() {
        assertNull(parser.parse("mt-[4px]"))
    }

    @Test
    fun `parser rejects malformed negative step`() {
        assertNull(parser.parse("mt--2"))
    }

    // --- generator ---

    @Test
    fun `generator produces k- prefixed rule for m-0`() {
        val def = StyleUtilityDefinition(token = "m-0", cssDeclaration = "margin: 0;")
        val css = generator.generate(def)
        assertEquals(".k-m-0 { margin: 0; }", css)
    }

    @Test
    fun `generator produces k- prefixed rule with escaped colon for hover variant`() {
        val def = StyleUtilityDefinition(token = "hover:mt-2", cssDeclaration = "margin-top: 0.5rem;")
        val css = generator.generate(def)
        assertEquals(".k-hover\\:mt-2:hover { margin-top: 0.5rem; }", css)
    }

    @Test
    fun `generator wraps md variant in media query`() {
        val def = StyleUtilityDefinition(token = "md:p-4", cssDeclaration = "padding: 1rem;")
        val css = generator.generate(def)
        assertEquals("@media (min-width: 768px) { .k-md\\:p-4 { padding: 1rem; } }", css)
    }

    // --- compiler integration ---

    @Test
    fun `compiler generates real CSS for m-0 with spacing hooks`() {
        val css = compiler.compile("m-0")
        assertEquals(".k-m-0 { margin: 0; }", css)
    }

    @Test
    fun `compiler generates real CSS for mt-4 with spacing hooks`() {
        val css = compiler.compile("mt-4")
        assertEquals(".k-mt-4 { margin-top: 1rem; }", css)
    }

    @Test
    fun `compiler generates real CSS for p-4 with spacing hooks`() {
        val css = compiler.compile("p-4")
        assertEquals(".k-p-4 { padding: 1rem; }", css)
    }

    @Test
    fun `compiler generates real CSS for p-2 with spacing hooks`() {
        val css = compiler.compile("p-2")
        assertEquals(".k-p-2 { padding: 0.5rem; }", css)
    }

    @Test
    fun `compiler generates hover variant css with spacing hooks`() {
        val css = compiler.compile("hover:mt-2")
        assertEquals(".k-hover\\:mt-2:hover { margin-top: 0.5rem; }", css)
    }

    @Test
    fun `compiler marks flex as unsupported since it is not a spacing token`() {
        val css = compiler.compile("flex")
        assertEquals("/* kolo-unsupported: flex */", css)
    }

    @Test
    fun `compiler generates multiple tokens in sequence`() {
        val css = compiler.compile("m-0;mb-3")
        assertEquals(".k-m-0 { margin: 0; }.k-mb-3 { margin-bottom: 0.75rem; }", css)
    }

    // --- utility definitions catalog ---

    @Test
    fun `spacingUtilityDefinitions covers all prefixes and steps 0 to 16`() {
        val defs = spacingUtilityDefinitions()
        val tokens = defs.map { it.token }.toSet()
        assertTrue(tokens.contains("m-0"))
        assertTrue(tokens.contains("m-16"))
        assertTrue(tokens.contains("mt-4"))
        assertTrue(tokens.contains("p-4"))
        assertTrue(tokens.contains("px-2"))
        assertEquals(14 * 17, defs.size) // 14 prefixes × 17 steps (0..16)
    }

    @Test
    fun `spacingUtilityDefinitions cssDeclaration contains only property declarations not a full rule`() {
        val def = spacingUtilityDefinitions().first { it.token == "mt-2" }
        assertEquals("margin-top: 0.5rem;", def.cssDeclaration)
        assertTrue(!def.cssDeclaration.contains("{"), "cssDeclaration should not include a CSS block selector")
    }
}

