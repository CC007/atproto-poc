package com.github.cc007.blueart.kolostyles.utility

import com.github.cc007.blueart.kolostyles.compiler.KoloCssCompiler
import com.github.cc007.blueart.kolostyles.compiler.display.DisplayGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.display.DisplayParserHook
import com.github.cc007.blueart.kolostyles.compiler.font.FontGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.font.FontParserHook
import com.github.cc007.blueart.kolostyles.compiler.spacing.SpacingGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.spacing.SpacingParserHook
import kotlinx.css.CssBuilder
import kotlin.test.*

class FontUtilitiesTest {

    private val parser = FontParserHook()
    private val generator = FontGeneratorHook()
    private val fontOnlyCompiler = KoloCssCompiler(
        parserHooks = listOf(parser),
        generatorHooks = listOf(generator),
    )
    private val mixedCompiler = KoloCssCompiler(
        parserHooks = listOf(SpacingParserHook(), DisplayParserHook(), parser),
        generatorHooks = listOf(SpacingGeneratorHook(), DisplayGeneratorHook(), generator),
    )

    @Test
    fun `parser accepts full font-family allow-list`() {
        val tokens = listOf("font-sans", "font-serif", "font-mono")
        tokens.forEach { token ->
            val parsed = parser.parse(token)
            assertNotNull(parsed, "Expected parser to accept $token")
            assertEquals(token, parsed.raw)
        }
    }

    @Test
    fun `parser accepts full font-size allow-list`() {
        val tokens = listOf(
            "text-xs", "text-sm", "text-base", "text-lg", "text-xl",
            "text-2xl", "text-3xl", "text-4xl", "text-5xl", "text-6xl", "text-7xl", "text-8xl", "text-9xl",
        )
        tokens.forEach { token ->
            val parsed = parser.parse(token)
            assertNotNull(parsed, "Expected parser to accept $token")
            assertEquals(token, parsed.raw)
        }
    }

    @Test
    fun `parser accepts full font-weight allow-list and rejects custom weights`() {
        val accepted = listOf(
            "font-thin", "font-extralight", "font-light", "font-normal", "font-medium",
            "font-semibold", "font-bold", "font-extrabold", "font-black",
        )
        accepted.forEach { token ->
            val parsed = parser.parse(token)
            assertNotNull(parsed, "Expected parser to accept $token")
            assertEquals(token, parsed.raw)
        }

        assertNull(parser.parse("font-100"))
        assertNull(parser.parse("font-950"))
        assertNull(parser.parse("font-[600]"))
    }

    @Test
    fun `parser accepts state and media variants and rejects multiple media variants`() {
        assertNotNull(parser.parse("hover:font-semibold"))
        assertNotNull(parser.parse("focus-visible:text-lg"))
        assertNotNull(parser.parse("md:font-sans"))
        assertNull(parser.parse("sm:md:text-lg"))
    }

    @Test
    fun `generator emits base font family css`() {
        val token = parser.parse("font-sans")
        assertNotNull(token)
        val builder = CssBuilder()
        assertTrue(generator.generate(token, builder))
        assertEquals(
            """
            .k-font-sans {
            font-family: var(--font-sans);
            }
            
            """.trimIndent(),
            builder.toString()
        )
    }

    @Test
    fun `generator emits pseudo selector for hover font weight variant`() {
        val token = parser.parse("hover:font-semibold")
        assertNotNull(token)
        val builder = CssBuilder()
        assertTrue(generator.generate(token, builder))
        assertEquals(
            """
            .k-hover\:font-semibold:hover {
            font-weight: 600;
            }
            
            """.trimIndent(),
            builder.toString()
        )
    }

    @Test
    fun `generator emits media wrapped selector for md font size variant`() {
        val token = parser.parse("md:text-2xl")
        assertNotNull(token)
        val builder = CssBuilder()
        assertTrue(generator.generate(token, builder))
        assertEquals(
            """
            @media (min-width: 48rem) {
            .k-md\:text-2xl {
            font-size: 1.5rem;
            }
            }
            
            """.trimIndent(),
            builder.toString()
        )
    }

    @Test
    fun `compiler marks unknown font token unsupported and malformed font token unparsed`() {
        assertEquals(
            """
            :root {
            --kolo-unsupported-0: "font-950";
            }
            
            """.trimIndent(),
            fontOnlyCompiler.compile("font-950")
        )
        assertEquals(
            """
            :root {
            --kolo-unparsed-0: "md: text-xl";
            }
            
            """.trimIndent(),
            fontOnlyCompiler.compile("md: text-xl")
        )
    }

    @Test
    fun `mixed spacing display and font compilation preserves input order`() {
        val css = mixedCompiler.compile("md:grid;mt-2;font-semibold;hover:inline-flex")
        assertEquals(
            """
            @media (min-width: 48rem) {
            .k-md\:grid {
            display: grid;
            }
            }
            .k-mt-2 {
            margin-top: 0.5rem;
            }
            .k-font-semibold {
            font-weight: 600;
            }
            .k-hover\:inline-flex:hover {
            display: inline-flex;
            }
            
            """.trimIndent(),
            css
        )
    }
}
