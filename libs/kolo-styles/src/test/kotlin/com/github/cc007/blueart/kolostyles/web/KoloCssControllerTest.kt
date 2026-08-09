package com.github.cc007.blueart.kolostyles.web

import com.github.cc007.blueart.kolostyles.compiler.KoloCssCompiler
import com.github.cc007.blueart.kolostyles.compiler.display.DisplayGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.display.DisplayParserHook
import com.github.cc007.blueart.kolostyles.compiler.font.FontGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.font.FontParserHook
import com.github.cc007.blueart.kolostyles.compiler.sizing.SizingGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.sizing.SizingParserHook
import com.github.cc007.blueart.kolostyles.compiler.spacing.SpacingGeneratorHook
import com.github.cc007.blueart.kolostyles.compiler.spacing.SpacingParserHook
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpStatus
import kotlin.test.Test

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
            parserHooks = listOf(SpacingParserHook(), DisplayParserHook(), FontParserHook(), SizingParserHook()),
            generatorHooks = listOf(SpacingGeneratorHook(), DisplayGeneratorHook(), FontGeneratorHook(), SizingGeneratorHook()),
        )
    )

    @Test
    fun `kolo stylesheet with empty hooks emits unsupported comments for valid tokens`() {
        val response = emptyController.koloStylesheet(
            version = "abc123",
            kolo = "mt-2;flex;hover:mt-2"
        )

        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe
            """
            :root {
            --kolo-unsupported-0: "mt-2";
            --kolo-unsupported-1: "flex";
            --kolo-unsupported-2: "hover:mt-2";
            }
            
            """.trimIndent()
    }

    @Test
    fun `kolo stylesheet notes arbitrary value tokens as unparsed comments`() {
        val response = emptyController.koloStylesheet(
            version = "abc123",
            kolo = "mt-[2px]"
        )

        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe
            """
            :root {
            --kolo-unparsed-0: "mt-[2px]";
            }
            
            """.trimIndent()
    }

    @Test
    fun `kolo stylesheet notes malformed tokens as unparsed comments`() {
        val response = emptyController.koloStylesheet(
            version = "abc123",
            kolo = "mt- 2"
        )

        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe
            """
            :root {
            --kolo-unparsed-0: "mt- 2";
            }
            
            """.trimIndent()
    }

    @Test
    fun `kolo stylesheet with spacing hooks generates real CSS for spacing tokens`() {
        val response = spacingController.koloStylesheet(
            version = "abc123",
            kolo = "m-0;p-4"
        )

        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe
            """
            .k-m-0 {
            margin: 0.0rem;
            }
            .k-p-4 {
            padding: 1.0rem;
            }
            
            """.trimIndent()
    }

    @Test
    fun `kolo stylesheet with spacing hooks marks non-spacing tokens as unsupported`() {
        val response = spacingController.koloStylesheet(
            version = "abc123",
            kolo = "flex"
        )
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe
            """
            :root {
            --kolo-unsupported-0: "flex";
            }
            
            """.trimIndent()
    }

    @Test
    fun `kolo stylesheet with spacing display font and sizing hooks compiles mixed utility tokens`() {
        val response = defaultController.koloStylesheet(
            version = "abc123",
            kolo = "md:grid;mt-2;font-semibold;hover:inline-flex;size-full"
        )

        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe
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
            .k-size-full {
            width: 100%;
            height: 100%;
            }
            
            """.trimIndent()
    }
}
