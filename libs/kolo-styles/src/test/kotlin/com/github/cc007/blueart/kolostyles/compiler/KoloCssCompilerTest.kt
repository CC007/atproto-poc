package com.github.cc007.blueart.kolostyles.compiler

import com.github.cc007.blueart.kolostyles.generator.StyleGeneratorHook
import com.github.cc007.blueart.kolostyles.parser.StyleParserHook
import com.github.cc007.blueart.kolostyles.utility.StyleUtilityDefinition
import kotlin.test.Test
import kotlin.test.assertEquals

class KoloCssCompilerTest {

    @Test
    fun `compile parses and generates css through hooks`() {
        val parserHook = StyleParserHook { token ->
            StyleUtilityDefinition(token = token, cssDeclaration = {
                put("display", "block")
            })
        }
        val generatorHook = StyleGeneratorHook { definition, builder ->
            builder.apply {
                ".${definition.token.replace(":", "\\:")}" {
                    definition.cssDeclaration.let { this.it() }
                }
            }
            true
        }
        val compiler = KoloCssCompiler(
            parserHooks = listOf(parserHook),
            generatorHooks = listOf(generatorHook)
        )

        val result = compiler.compile("flex;mt-2;hover:mt-2;md:mt-2")

        assertEquals(
            """
            .flex {
            display: block;
            }
            .mt-2 {
            display: block;
            }
            .hover\:mt-2 {
            display: block;
            }
            .md\:mt-2 {
            display: block;
            }
            
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `compile preserves token order from input`() {
        val tokens = mutableListOf<String>()
        val recordingParser = StyleParserHook { token ->
            tokens.add(token)
            null
        }
        val noopGenerator = StyleGeneratorHook { _, _ -> false }
        val compiler = KoloCssCompiler(
            parserHooks = listOf(recordingParser),
            generatorHooks = listOf(noopGenerator)
        )

        compiler.compile("md:mt-2;mt-2;flex;mt-2;hover:mt-2")

        // Should preserve order and include duplicates (deduping is client-side)
        assertEquals(listOf("md:mt-2", "mt-2", "flex", "mt-2", "hover:mt-2"), tokens)
    }

    @Test
    fun `compile notes arbitrary value tokens as unparsed comments`() {
        val compiler = KoloCssCompiler()

        val result = compiler.compile("mt-[4px]")

        assertEquals(
            """
            :root {
            --kolo-unparsed-0: "mt-[4px]";
            }
            
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `compile notes malformed tokens with whitespace as comments`() {
        val compiler = KoloCssCompiler()

        val result = compiler.compile("mt- 2")

        assertEquals(
            """
            :root {
            --kolo-unparsed-0: "mt- 2";
            }
            
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `compile produces empty css for empty input`() {
        val compiler = KoloCssCompiler()

        val result = compiler.compile(" ; ; ")

        assertEquals("", result)
    }

    @Test
    fun `compile with no supporting hooks marks tokens unsupported`() {
        val compiler = KoloCssCompiler()

        val result = compiler.compile("flex;mt-2")

        assertEquals(
            """
            :root {
            --kolo-unsupported-0: "flex";
            --kolo-unsupported-1: "mt-2";
            }
            
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `compile uses parser and generator hooks when token is supported via hooks`() {
        val parserHook = StyleParserHook { token ->
            if (token == "x-hook") {
                StyleUtilityDefinition(token = token, cssDeclaration = {
                    put("display", "block")
                })
            } else {
                null
            }
        }
        
        val generatorHook = StyleGeneratorHook { definition, builder ->
            if (definition.token != "x-hook") {
                return@StyleGeneratorHook false
            }
            builder.apply {
                ".x-hook" {
                    definition.cssDeclaration.let { this.it() }
                }
            }
            true
        }

        val compiler = KoloCssCompiler(
            parserHooks = listOf(parserHook),
            generatorHooks = listOf(generatorHook)
        )

        val result = compiler.compile("x-hook")

        assertEquals(
            """
            .x-hook {
            display: block;
            }
            
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `compile marks token unsupported when hooks do not support token`() {
        val parserHook = StyleParserHook { null }
        val generatorHook = StyleGeneratorHook { _, _ -> false }
        val compiler = KoloCssCompiler(
            parserHooks = listOf(parserHook),
            generatorHooks = listOf(generatorHook)
        )

        val result = compiler.compile("mt-2")

        assertEquals(
            """
            :root {
            --kolo-unsupported-0: "mt-2";
            }
            
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `compile escapes comment terminators inside unsupported tokens`() {
        val compiler = KoloCssCompiler()

        val result = compiler.compile("broken*/token")

        assertEquals(
            """
            :root {
            --kolo-unsupported-0: "broken*/token";
            }
            
            """.trimIndent(),
            result
        )
    }
}
