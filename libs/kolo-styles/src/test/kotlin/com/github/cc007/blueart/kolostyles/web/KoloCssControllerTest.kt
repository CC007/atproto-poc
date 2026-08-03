package com.github.cc007.blueart.kolostyles.web

import com.github.cc007.blueart.kolostyles.compiler.KoloCssCompiler
import com.github.cc007.blueart.kolostyles.compiler.display.DisplayGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.display.DisplayParserHook
import com.github.cc007.blueart.kolostyles.compiler.spacing.SpacingGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.spacing.SpacingParserHook
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class KoloCssControllerTest {

    // controller with no hooks — tokens appear as unsupported comments (isolated test behaviour)
    private val emptyController = KoloCssController(KoloCssCompiler())

    // controller with spacing hooks only (isolated spacing behavior)
    private val spacingController = KoloCssController(
        KoloCssCompiler(
            parserHooks = listOf(SpacingParserHook()),
            generatorHooks = listOf(SpacingGeneratorHook()),
        )
    )
    // controller with spacing + display hooks wired (mirrors Spring bean list wiring)
    private val defaultController = KoloCssController(
        KoloCssCompiler(
            parserHooks = listOf(SpacingParserHook(), DisplayParserHook()),
            generatorHooks = listOf(SpacingGeneratorHook(), DisplayGeneratorHook()),
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
            """
            :root {
            --kolo-unsupported-0: "mt-2";
            --kolo-unsupported-1: "flex";
            --kolo-unsupported-2: "hover:mt-2";
            }
            
            """.trimIndent(),
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
        assertEquals(
            """
            :root {
            --kolo-unparsed-0: "mt-[2px]";
            }
            
            """.trimIndent(),
            response.body
        )
    }

    @Test
    fun `kolo stylesheet notes malformed tokens as unparsed comments`() {
        val response = emptyController.koloStylesheet(
            version = "abc123",
            kolo = "mt- 2"
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(
            """
            :root {
            --kolo-unparsed-0: "mt- 2";
            }
            
            """.trimIndent(),
            response.body
        )
    }

    @Test
    fun `kolo stylesheet with spacing hooks generates real CSS for spacing tokens`() {
        val response = spacingController.koloStylesheet(
            version = "abc123",
            kolo = "m-0;p-4"
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(
            """
            .k-m-0 {
            margin: 0.0rem;
            }
            .k-p-4 {
            padding: 1.0rem;
            }
            
            """.trimIndent(),
            response.body
        )
    }

    @Test
    fun `kolo stylesheet with spacing hooks marks non-spacing tokens as unsupported`() {
        val response = spacingController.koloStylesheet(
            version = "abc123",
            kolo = "flex"
        )
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(
            """
            :root {
            --kolo-unsupported-0: "flex";
            }
            
            """.trimIndent(),
            response.body
        )
    }

    @Test
    fun `kolo stylesheet with spacing and display hooks compiles mixed utility tokens`() {
        val response = defaultController.koloStylesheet(
            version = "abc123",
            kolo = "md:grid;mt-2;hover:inline-flex"
        )

        assertEquals(HttpStatus.OK, response.statusCode)
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
            response.body
        )
    }
}
