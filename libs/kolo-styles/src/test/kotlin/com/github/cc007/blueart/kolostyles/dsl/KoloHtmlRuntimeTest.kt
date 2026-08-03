package com.github.cc007.blueart.kolostyles.dsl

import com.github.cc007.blueart.kolostyles.dsl.spacing.*
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
    fun `kolo collects tokens across multiple elements deduplicating overlaps in the stylesheet href`() {
        val html = renderKoloHtml(
            version = "abc123",
            classNameMapper = { token -> "k-$token" },
        ) {
            head {
                koloStylesheetLink()
            }
            body {
                div {
                    kolo {
                        recordBase("flex")  // overlapping
                        recordBase("mt-2")  // unique to this element
                    }
                }
                div {
                    kolo {
                        recordBase("flex")  // overlapping
                        recordBase("px-4")  // unique to this element
                    }
                }
            }
        }

        // The href contains the deduplicated union of all tokens across all elements
        // Per-element classes reflect only that element's own tokens
        val expected = """
            <html>
              <head>
                <link href="/css/generated/kolo.css?version=abc123&kolo=flex%3Bmt-2%3Bpx-4" rel="stylesheet">
              </head>
              <body>
                <div class="k-flex k-mt-2"></div>
                <div class="k-flex k-px-4"></div>
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

    @Test
    fun `kolo spacing dsl supports auto spacing variants`() {
        val html = renderKoloHtml(version = "abc123") {
            head {
                koloStylesheetLink()
            }
            body {
                div {
                    kolo {
                        mAuto
                        mtAuto
                        mrAuto
                        mbAuto
                        mlAuto
                        mxAuto
                        myAuto
                        pAuto
                        ptAuto
                        prAuto
                        pbAuto
                        plAuto
                        pxAuto
                        pyAuto
                        variant("md").mtAuto
                        variant("md").ptAuto
                    }
                }
            }
        }

        val expected = """
            <html>
              <head>
                <link href="/css/generated/kolo.css?version=abc123&kolo=m-auto%3Bmb-auto%3Bml-auto%3Bmr-auto%3Bmt-auto%3Bmd%3Amt-auto%3Bmx-auto%3Bmy-auto%3Bp-auto%3Bpb-auto%3Bpl-auto%3Bpr-auto%3Bpt-auto%3Bmd%3Apt-auto%3Bpx-auto%3Bpy-auto" rel="stylesheet">
              </head>
              <body>
                <div class="k-m-auto k-mt-auto k-mr-auto k-mb-auto k-ml-auto k-mx-auto k-my-auto k-p-auto k-pt-auto k-pr-auto k-pb-auto k-pl-auto k-px-auto k-py-auto k-md:mt-auto k-md:pt-auto"></div>
              </body>
            </html>
        """.trimIndent() + "\n"
        assertEquals(expected, html)
    }
}
