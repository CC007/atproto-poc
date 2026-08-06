package com.github.cc007.blueart.kolostyles.utility

import com.github.cc007.blueart.kolostyles.compiler.KoloCssCompiler
import com.github.cc007.blueart.kolostyles.compiler.Token
import com.github.cc007.blueart.kolostyles.compiler.display.DisplayGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.display.DisplayParserHook
import com.github.cc007.blueart.kolostyles.compiler.font.FontGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.font.FontParserHook
import com.github.cc007.blueart.kolostyles.compiler.sizing.SizingGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.sizing.SizingParserHook
import com.github.cc007.blueart.kolostyles.compiler.spacing.SpacingGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.spacing.SpacingParserHook
import kotlinx.css.CssBuilder
import kotlin.test.*

class SizingUtilitiesTest {

    private val parser = SizingParserHook()
    private val generator = SizingGeneratorHook()
    private val sizingOnlyCompiler = KoloCssCompiler(
        parserHooks = listOf(parser),
        generatorHooks = listOf(generator),
    )
    private val mixedCompiler = KoloCssCompiler(
        parserHooks = listOf(SpacingParserHook(), DisplayParserHook(), FontParserHook(), parser),
        generatorHooks = listOf(SpacingGeneratorHook(), DisplayGeneratorHook(), FontGeneratorHook(), generator),
    )

    @Test
    fun `parser accepts tokens for all sizing utility families`() {
        val tokens = listOf(
            "w-full",
            "w-1/2",
            "w-1/1",
            "w-dvw",
            "h-screen",
            "h-svh",
            "min-w-1/2",
            "max-w-3xl",
            "min-h-dvh",
            "max-h-fit",
            "size-3",
            "size-lvw",
            "size-1/3",
            "size-2/7",
            "w-13",
            "hover:w-4",
            "md:max-h-screen",
        )

        tokens.forEach { token ->
            val parsed = parser.parse(token)
            assertNotNull(parsed, "Expected parser to accept $token")
            assertEquals(token, parsed.raw)
        }
    }

    @Test
    fun `parser rejects unsupported sizing tokens and malformed variants`() {
        assertNull(parser.parse("w-[170px]"))
        assertNull(parser.parse("h-72vh"))
        assertNull(parser.parse("max-w-none"))
        assertNull(parser.parse("h-3xl"))
        assertNull(parser.parse("min-h-md"))
        assertNull(parser.parse("max-h-2xl"))
        assertNull(parser.parse("size-xs"))
        assertNull(parser.parse("w-1/0"))
        assertNull(parser.parse("sm:md:w-4"))
        assertNull(parser.parse("unknown:w-4"))
    }

    @Test
    fun `generator emits declarations for all sizing prefixes including size dual output`() {
        val tokens = listOf("w-4", "w-1/1", "h-full", "min-w-0", "max-w-md", "min-h-screen", "max-h-fit", "size-full")
        val css = tokens.joinToString(separator = "") { token ->
            val parsed = parser.parse(token)
            assertNotNull(parsed)
            val builder = CssBuilder()
            assertTrue(generator.generate(parsed, builder))
            builder.toString()
        }

        assertTrue(css.contains(".k-w-4 {\nwidth: 1.0rem;\n}"))
        assertTrue(css.contains(".k-w-1/1 {\nwidth: calc(1/1 * 100%);\n}"))
        assertTrue(css.contains(".k-h-full {\nheight: 100%;\n}"))
        assertTrue(css.contains(".k-min-w-0 {\nmin-width: 0.0rem;\n}"))
        assertTrue(css.contains(".k-max-w-md {\nmax-width: 28rem;\n}"))
        assertTrue(css.contains(".k-min-h-screen {\nmin-height: 100vh;\n}"))
        assertTrue(css.contains(".k-max-h-fit {\nmax-height: fit-content;\n}"))
        assertTrue(css.contains(".k-size-full {\nwidth: 100%;\nheight: 100%;\n}"))
    }

    @Test
    fun `generator emits pseudo and media variants for sizing utilities`() {
        val pseudoToken = parser.parse("hover:w-4")
        assertNotNull(pseudoToken)
        val pseudoBuilder = CssBuilder()
        assertTrue(generator.generate(pseudoToken, pseudoBuilder))
        assertEquals(
            """
            .k-hover\:w-4:hover {
            width: 1.0rem;
            }
            
            """.trimIndent(),
            pseudoBuilder.toString()
        )

        val mediaToken = parser.parse("md:min-h-screen")
        assertNotNull(mediaToken)
        val mediaBuilder = CssBuilder()
        assertTrue(generator.generate(mediaToken, mediaBuilder))
        assertEquals(
            """
            @media (min-width: 48rem) {
            .k-md\:min-h-screen {
            min-height: 100vh;
            }
            }
            
            """.trimIndent(),
            mediaBuilder.toString()
        )
    }

    @Test
    fun `generator returns false for unsupported token type`() {
        val unsupported = UnsupportedToken("w-4")
        val builder = CssBuilder()
        assertFalse(generator.generate(unsupported, builder))
        assertEquals("", builder.toString())
    }

    @Test
    fun `compiler marks unsupported sizing token and generates mixed utility output in order`() {
        assertEquals(
            """
            :root {
            --kolo-unsupported-0: "max-w-none";
            }
            
            """.trimIndent(),
            sizingOnlyCompiler.compile("max-w-none")
        )

        val css = mixedCompiler.compile("md:grid;w-full;mt-2;font-semibold;size-1/2")
        assertEquals(
            """
            @media (min-width: 48rem) {
            .k-md\:grid {
            display: grid;
            }
            }
            .k-w-full {
            width: 100%;
            }
            .k-mt-2 {
            margin-top: 0.5rem;
            }
            .k-font-semibold {
            font-weight: 600;
            }
            .k-size-1/2 {
            width: calc(1/2 * 100%);
            height: calc(1/2 * 100%);
            }
            
            """.trimIndent(),
            css
        )
    }

    private data class UnsupportedToken(
        override val raw: String
    ) : Token
}
