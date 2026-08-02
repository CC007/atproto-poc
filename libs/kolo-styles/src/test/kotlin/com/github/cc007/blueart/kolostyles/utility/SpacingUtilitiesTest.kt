package com.github.cc007.blueart.kolostyles.utility

import com.github.cc007.blueart.kolostyles.compiler.KoloCssCompiler
import kotlinx.css.*
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
    fun `parser accepts m-0 and returns zero declaration`() {
        val def = parser.parse("m-0")
        assertNotNull(def)
        assertEquals("m-0", def.token)
        assertEquals("margin: 0rem;", serializeDeclaration(def.cssDeclaration))
    }

    @Test
    fun `parser accepts mt-4 and returns correct rem value`() {
        val def = parser.parse("mt-4")
        assertNotNull(def)
        assertEquals("margin-top: 1rem;", serializeDeclaration(def.cssDeclaration))
    }

    @Test
    fun `parser accepts mb-3 and returns correct rem value`() {
        val def = parser.parse("mb-3")
        assertNotNull(def)
        assertEquals("margin-bottom: 0.75rem;", serializeDeclaration(def.cssDeclaration))
    }

    @Test
    fun `parser accepts p-2 and returns correct rem value`() {
        val def = parser.parse("p-2")
        assertNotNull(def)
        assertEquals("padding: 0.5rem;", serializeDeclaration(def.cssDeclaration))
    }

    @Test
    fun `parser accepts mx-4 and generates horizontal shorthand`() {
        val def = parser.parse("mx-4")
        assertNotNull(def)
        assertEquals("margin-left: 1rem; margin-right: 1rem;", serializeDeclaration(def.cssDeclaration))
    }

    @Test
    fun `parser accepts mx-auto and generates auto horizontal margins`() {
        val def = parser.parse("mx-auto")
        assertNotNull(def)
        assertEquals("mx-auto", def.token)
        assertEquals("margin-left: auto; margin-right: auto;", serializeDeclaration(def.cssDeclaration))
    }

    @Test
    fun `parser accepts mt-auto and generates auto top margin`() {
        val def = parser.parse("mt-auto")
        assertNotNull(def)
        assertEquals("mt-auto", def.token)
        assertEquals("margin-top: auto;", serializeDeclaration(def.cssDeclaration))
    }

    @Test
    fun `parser accepts p-auto and generates auto padding`() {
        val def = parser.parse("p-auto")
        assertNotNull(def)
        assertEquals("p-auto", def.token)
        assertEquals("padding: auto;", serializeDeclaration(def.cssDeclaration))
    }

    @Test
    fun `parser accepts py-2 and generates vertical shorthand`() {
        val def = parser.parse("py-2")
        assertNotNull(def)
        assertEquals("padding-top: 0.5rem; padding-bottom: 0.5rem;", serializeDeclaration(def.cssDeclaration))
    }

    @Test
    fun `parser accepts hover variant`() {
        val def = parser.parse("hover:mt-2")
        assertNotNull(def)
        assertEquals("margin-top: 0.5rem;", serializeDeclaration(def.cssDeclaration))
    }

    @Test
    fun `parser accepts md media variant`() {
        val def = parser.parse("md:p-4")
        assertNotNull(def)
        assertEquals("padding: 1rem;", serializeDeclaration(def.cssDeclaration))
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
        val def = StyleUtilityDefinition(token = "m-0", cssDeclaration = {
            margin = kotlinx.css.Margin(0.px)
        })
        val cssBuilder = CssBuilder()
        assertTrue(generator.generate(def, cssBuilder))
        assertEquals(
            """
            .k-m-0 {
            margin: 0;
            }
            
            """.trimIndent(),
            cssBuilder.toString()
        )
    }

    @Test
    fun `generator produces k- prefixed rule with escaped colon for hover variant`() {
        val def = StyleUtilityDefinition(token = "hover:mt-2", cssDeclaration = {
            marginTop = 0.5.rem
        })
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
        val def = StyleUtilityDefinition(token = "md:p-4", cssDeclaration = {
            padding = kotlinx.css.Padding(1.rem)
        })
        val cssBuilder = CssBuilder()
        assertTrue(generator.generate(def, cssBuilder))
        assertEquals(
            """
            @media (min-width: 768px) {
            .k-md\:p-4 {
            padding: 1rem;
            }
            }
            
            """.trimIndent(),
            cssBuilder.toString()
        )
    }

    @Test
    fun `generator returns false for unsupported token`() {
        val def = StyleUtilityDefinition(token = "flex", cssDeclaration = {
            put("display", "block")
        })
        val cssBuilder = CssBuilder()
        assertFalse(generator.generate(def, cssBuilder))
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

    private fun serializeDeclaration(declaration: CssBuilder.() -> Unit): String {
        val serialized = CssBuilder().apply {
            ".k-temp" {
                declaration()
            }
        }.toString()
        return serialized
            .substringAfter('{')
            .substringBeforeLast('}')
            .replace(Regex("\\s+"), " ")
            .trim()
            .replace("0px;", "0;")
            .replace(Regex("(\\d+)\\.0rem"), "$1rem")
    }
}
