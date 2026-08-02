package com.github.cc007.blueart.kolostyles.utility

import com.github.cc007.blueart.kolostyles.compiler.KoloCssCompiler
import com.github.cc007.blueart.kolostyles.compiler.Token
import com.github.cc007.blueart.kolostyles.compiler.spacing.SpacingGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.spacing.SpacingParserHook
import kotlinx.css.CssBuilder
import kotlin.test.*

class SpacingUtilitiesTest {

    private val parser = SpacingParserHook()
    private val generator = SpacingGeneratorHook()
    private val compiler = KoloCssCompiler(
        parserHooks = listOf(parser),
        generatorHooks = listOf(generator),
    )

    // --- parser ---

    @Test
    fun `parser accepts m-0 and returns spacing token`() {
        val def = parser.parse("m-0")
        assertNotNull(def)
        assertEquals("m-0", def.raw)
    }

    @Test
    fun `parser accepts mt-4`() {
        val def = parser.parse("mt-4")
        assertNotNull(def)
        assertEquals("mt-4", def.raw)
    }

    @Test
    fun `parser accepts mb-3`() {
        val def = parser.parse("mb-3")
        assertNotNull(def)
        assertEquals("mb-3", def.raw)
    }

    @Test
    fun `parser accepts p-2`() {
        val def = parser.parse("p-2")
        assertNotNull(def)
        assertEquals("p-2", def.raw)
    }

    @Test
    fun `parser accepts mx-4`() {
        val def = parser.parse("mx-4")
        assertNotNull(def)
        assertEquals("mx-4", def.raw)
    }

    @Test
    fun `parser accepts mx-auto`() {
        val def = parser.parse("mx-auto")
        assertNotNull(def)
        assertEquals("mx-auto", def.raw)
    }

    @Test
    fun `parser accepts mt-auto`() {
        val def = parser.parse("mt-auto")
        assertNotNull(def)
        assertEquals("mt-auto", def.raw)
    }

    @Test
    fun `parser accepts p-auto`() {
        val def = parser.parse("p-auto")
        assertNotNull(def)
        assertEquals("p-auto", def.raw)
    }

    @Test
    fun `parser accepts py-2`() {
        val def = parser.parse("py-2")
        assertNotNull(def)
        assertEquals("py-2", def.raw)
    }

    @Test
    fun `parser accepts hover variant`() {
        val def = parser.parse("hover:mt-2")
        assertNotNull(def)
        assertEquals("hover:mt-2", def.raw)
    }

    @Test
    fun `parser accepts md media variant`() {
        val def = parser.parse("md:p-4")
        assertNotNull(def)
        assertEquals("md:p-4", def.raw)
    }

    @Test
    fun `parser rejects multiple media variants`() {
        assertNull(parser.parse("sm:md:p-4"))
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

    @Test
    fun `parser rejects unsupported auto utility`() {
        assertNull(parser.parse("gap-auto"))
    }

    // --- generator ---

    @Test
    fun `generator produces k- prefixed rule for m-0`() {
        val def = parser.parse("m-0")
        assertNotNull(def)
        val cssBuilder = CssBuilder()
        assertTrue(generator.generate(def, cssBuilder))
        assertEquals(
            """
            .k-m-0 {
            margin: 0.0rem;
            }
            
            """.trimIndent(),
            cssBuilder.toString()
        )
    }

    @Test
    fun `generator produces k- prefixed rule with escaped colon for hover variant`() {
        val def = parser.parse("hover:mt-2")
        assertNotNull(def)
        val cssBuilder = CssBuilder()
        assertTrue(generator.generate(def, cssBuilder))
        assertEquals(
            """
            .k-hover\:mt-2:hover {
            margin-top: 0.5rem;
            }
            
            """.trimIndent(),
            cssBuilder.toString()
        )
    }

    @Test
    fun `generator wraps md variant in media query`() {
        val def = parser.parse("md:p-4")
        assertNotNull(def)
        val cssBuilder = CssBuilder()
        assertTrue(generator.generate(def, cssBuilder))
        assertEquals(
            """
            @media (min-width: 48rem) {
            .k-md\:p-4 {
            padding: 1.0rem;
            }
            }
            
            """.trimIndent(),
            cssBuilder.toString()
        )
    }

    @Test
    fun `generator returns false for unsupported token`() {
        val token = UnsupportedToken("flex")
        val cssBuilder = CssBuilder()
        assertFalse(generator.generate(token, cssBuilder))
        assertEquals("", cssBuilder.toString())
    }

    // --- compiler integration ---

    @Test
    fun `compiler generates real CSS for m-0 with spacing hooks`() {
        val css = compiler.compile("m-0")
        assertEquals(
            """
            .k-m-0 {
            margin: 0.0rem;
            }
            
            """.trimIndent(),
            css
        )
    }

    @Test
    fun `compiler generates real CSS for mt-4 with spacing hooks`() {
        val css = compiler.compile("mt-4")
        assertEquals(
            """
            .k-mt-4 {
            margin-top: 1.0rem;
            }
            
            """.trimIndent(),
            css
        )
    }

    @Test
    fun `compiler generates real CSS for p-4 with spacing hooks`() {
        val css = compiler.compile("p-4")
        assertEquals(
            """
            .k-p-4 {
            padding: 1.0rem;
            }
            
            """.trimIndent(),
            css
        )
    }

    @Test
    fun `compiler generates real CSS for p-2 with spacing hooks`() {
        val css = compiler.compile("p-2")
        assertEquals(
            """
            .k-p-2 {
            padding: 0.5rem;
            }
            
            """.trimIndent(),
            css
        )
    }

    @Test
    fun `compiler generates real CSS for mx-auto with spacing hooks`() {
        val css = compiler.compile("mx-auto")
        assertEquals(
            """
            .k-mx-auto {
            margin-left: auto;
            margin-right: auto;
            }
            
            """.trimIndent(),
            css
        )
    }

    @Test
    fun `compiler generates real CSS for mt-auto with spacing hooks`() {
        val css = compiler.compile("mt-auto")
        assertEquals(
            """
            .k-mt-auto {
            margin-top: auto;
            }
            
            """.trimIndent(),
            css
        )
    }

    @Test
    fun `compiler generates real CSS for p-auto with spacing hooks`() {
        val css = compiler.compile("p-auto")
        assertEquals(
            """
            .k-p-auto {
            padding: auto;
            }
            
            """.trimIndent(),
            css
        )
    }

    @Test
    fun `compiler generates hover variant css with spacing hooks`() {
        val css = compiler.compile("hover:mt-2")
        assertEquals(
            """
            .k-hover\:mt-2:hover {
            margin-top: 0.5rem;
            }
            
            """.trimIndent(),
            css
        )
    }

    @Test
    fun `compiler marks flex as unsupported since it is not a spacing token`() {
        val css = compiler.compile("flex")
        assertEquals(
            """
            :root {
            --kolo-unsupported-0: "flex";
            }
            
            """.trimIndent(),
            css
        )
    }

    @Test
    fun `compiler generates multiple tokens in sequence`() {
        val css = compiler.compile("m-0;mb-3")
        assertEquals(
            """
            .k-m-0 {
            margin: 0.0rem;
            }
            .k-mb-3 {
            margin-bottom: 0.75rem;
            }
            
            """.trimIndent(),
            css
        )
    }

    private data class UnsupportedToken(
        override val raw: String
    ) : Token
}
