package com.github.cc007.blueart.kolostyles.utility

import com.github.cc007.blueart.kolostyles.compiler.KoloCssCompiler
import com.github.cc007.blueart.kolostyles.compiler.display.DisplayGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.display.DisplayParserHook
import com.github.cc007.blueart.kolostyles.compiler.spacing.SpacingGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.spacing.SpacingParserHook
import kotlinx.css.CssBuilder
import kotlin.test.*

class DisplayUtilitiesTest {

    private val parser = DisplayParserHook()
    private val generator = DisplayGeneratorHook()
    private val displayOnlyCompiler = KoloCssCompiler(
        parserHooks = listOf(parser),
        generatorHooks = listOf(generator),
    )
    private val mixedCompiler = KoloCssCompiler(
        parserHooks = listOf(SpacingParserHook(), parser),
        generatorHooks = listOf(SpacingGeneratorHook(), generator),
    )

    @Test
    fun `parser accepts full display allow-list`() {
        val tokens = listOf(
            "block", "inline", "inline-block", "flow-root", "flex", "inline-flex", "grid", "inline-grid",
            "contents", "list-item", "hidden", "table", "inline-table", "table-caption", "table-cell",
            "table-column", "table-column-group", "table-header-group", "table-row-group", "table-row",
            "table-footer-group",
        )

        tokens.forEach { token ->
            val parsed = parser.parse(token)
            assertNotNull(parsed, "Expected parser to accept $token")
            assertEquals(token, parsed.raw)
        }
    }

    @Test
    fun `parser rejects unknown and unsupported display-like tokens`() {
        assertNull(parser.parse("sr-only"))
        assertNull(parser.parse("not-sr-only"))
        assertNull(parser.parse("inline-list"))
    }

    @Test
    fun `parser accepts state and media variants and rejects multiple media variants`() {
        assertNotNull(parser.parse("hover:flex"))
        assertNotNull(parser.parse("focus-visible:grid"))
        assertNotNull(parser.parse("md:inline-grid"))
        assertNull(parser.parse("sm:md:flex"))
    }

    @Test
    fun `generator emits base display css for flex`() {
        val token = parser.parse("flex")
        assertNotNull(token)
        val builder = CssBuilder()
        assertTrue(generator.generate(token, builder))
        assertEquals(
            """
            .k-flex {
            display: flex;
            }
            
            """.trimIndent(),
            builder.toString()
        )
    }

    @Test
    fun `generator emits pseudo selector for hover display variant`() {
        val token = parser.parse("hover:inline-flex")
        assertNotNull(token)
        val builder = CssBuilder()
        assertTrue(generator.generate(token, builder))
        assertEquals(
            """
            .k-hover\:inline-flex:hover {
            display: inline-flex;
            }
            
            """.trimIndent(),
            builder.toString()
        )
    }

    @Test
    fun `generator emits media wrapped selector for md display variant`() {
        val token = parser.parse("md:grid")
        assertNotNull(token)
        val builder = CssBuilder()
        assertTrue(generator.generate(token, builder))
        assertEquals(
            """
            @media (min-width: 48rem) {
            .k-md\:grid {
            display: grid;
            }
            }
            
            """.trimIndent(),
            builder.toString()
        )
    }

    @Test
    fun `compiler marks unknown display token unsupported and malformed display token unparsed`() {
        assertEquals(
            """
            :root {
            --kolo-unsupported-0: "sr-only";
            }
            
            """.trimIndent(),
            displayOnlyCompiler.compile("sr-only")
        )
        assertEquals(
            """
            :root {
            --kolo-unparsed-0: "md: flex";
            }
            
            """.trimIndent(),
            displayOnlyCompiler.compile("md: flex")
        )
    }

    @Test
    fun `mixed spacing and display compilation preserves input order`() {
        val css = mixedCompiler.compile("md:grid;mt-2;hover:inline-flex")
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
            .k-hover\:inline-flex:hover {
            display: inline-flex;
            }
            
            """.trimIndent(),
            css
        )
    }
}
