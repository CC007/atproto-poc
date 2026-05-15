package com.github.cc007.blueart.kolostyles.render

import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.stream.createHTML
import kotlin.test.Test
import kotlin.test.assertEquals

class KoloHtmlRuntimeTest {

    @Test
    fun `canonicalize dedupes sorts and drops unsupported tokens`() {
        val canonical = canonicalizeKoloTokens(
            listOf(
                " md:mt-2 ",
                "flex",
                "hover:bg-sky-500",
                "md:mt-2",
                "mt-[2px]",
                "invalid;token",
                "",
            )
        )

        assertEquals("hover:bg-sky-500;flex;invalid;md:mt-2;token", canonical)
    }

    @Test
    fun `renderKoloHtml emits canonicalized stylesheet href from collected tokens`() {
        val html = renderKoloHtml(version = "abc123") {
            head {
                koloStylesheetLink()
            }
            body {
                div {
                    kolo {
                        variant("md").recordBase("mt-2")
                        recordBase("flex")
                        variant("hover").recordBase("bg-sky-500")
                        variant("md").recordBase("mt-2")
                    }
                }
            }
        }

        val expected = """
            <html>
              <head>
                <link href="/css/generated/kolo.css?version=abc123&kolo=hover%3Abg-sky-500%3Bflex%3Bmd%3Amt-2" rel="stylesheet">
              </head>
              <body>
                <div class="k-md:mt-2 k-flex k-hover:bg-sky-500"></div>
              </body>
            </html>
        """.trimIndent() + "\n"
        assertEquals(expected, html)
    }

    @Test
    fun `kolo can attach classes via mapper when explicitly provided`() {
        val html = renderKoloHtml(
            version = "abc123",
            classNameMapper = { token -> "k-${token}" }
        ) {
            head {
                koloStylesheetLink()
            }
            body {
                div(classes = "existing") {
                    kolo {
                        recordBase("flex")
                    }
                }
            }
        }

        val expected = """
            <html>
              <head>
                <link href="/css/generated/kolo.css?version=abc123&kolo=flex" rel="stylesheet">
              </head>
              <body>
                <div class="existing k-flex"></div>
              </body>
            </html>
        """.trimIndent() + "\n"
        assertEquals(expected, html)
    }

    @Test
    fun `kolo variant scaffold can compose nested variant tokens`() {
        val html = renderKoloHtml(version = "abc123") {
            head {
                koloStylesheetLink()
            }
            body {
                div {
                    kolo {
                        variant("dark").variant("md").recordBase("bg-sky-500")
                    }
                }
            }
        }

        val expected = """
            <html>
              <head>
                <link href="/css/generated/kolo.css?version=abc123&kolo=dark%3Amd%3Abg-sky-500" rel="stylesheet">
              </head>
              <body>
                <div class="k-dark:md:bg-sky-500"></div>
              </body>
            </html>
        """.trimIndent() + "\n"
        assertEquals(expected, html)
    }

    @Test
    fun `kolo noops when called outside kolo rendering context`() {
        val html = createHTML().html {
            body {
                div {
                    kolo {
                        recordBase("flex")
                    }
                }
            }
        }

        val expected = """
            <html>
              <body>
                <div></div>
              </body>
            </html>
        """.trimIndent() + "\n"
        assertEquals(expected, html)
    }
}

private fun KoloScope.recordBase(token: String) {
    recordBaseToken(token)
}

private fun KoloScope.variant(name: String): KoloVariantScope {
    return KoloVariantScope(withVariant(name))
}

private fun KoloVariantScope.recordBase(token: String) {
    recordBaseToken(token)
}

private fun KoloVariantScope.variant(name: String): KoloVariantScope {
    return withVariant(name)
}

