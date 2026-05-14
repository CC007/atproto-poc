package com.github.cc007.blueart.kolostyles.render

import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.stream.createHTML
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse

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
                        md.mt(2)
                        flex
                        hover.bg.sky(500)
                        md.mt(2)
                    }
                }
            }
        }

        assertContains(html, "/css/generated/kolo.css?version=abc123")
        assertContains(html, "kolo=hover%3Abg-sky-500%3Bflex%3Bmd%3Amt-2")
        assertFalse(html.contains("__blueart_kolo_href__"))
        assertFalse(html.contains("class=\"k-"))
    }

    @Test
    fun `kolo can attach classes via mapper when explicitly provided`() {
        val html = renderKoloHtml(
            version = "abc123",
            classNameMapper = { token -> "k-${token.replace(':', '-')}" }
        ) {
            head {
                koloStylesheetLink()
            }
            body {
                div(classes = "existing") {
                    kolo {
                        flex
                    }
                }
            }
        }

        assertContains(html, "class=\"existing k-flex\"")
    }

    @Test
    fun `kolo noops when called outside kolo rendering context`() {
        val html = createHTML().html {
            body {
                div {
                    kolo {
                        flex
                    }
                }
            }
        }

        assertFalse(html.contains("class=\""))
    }
}




