package com.github.cc007.blueart.components.richtext

import kotlinx.html.p
import kotlinx.html.stream.createHTML
import work.socialhub.kbsky.model.app.bsky.richtext.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RichTextFacetRendererTest {

    @Test
    fun `renders utf8 byte ranges for link and tag facets`() {
        val text = "A😀 check #art at https://example.com"
        val tagRange = byteRangeOf(text, "#art")
        val linkRange = byteRangeOf(text, "https://example.com")

        val segments = renderFacets(
            text = text,
            facets = listOf(
                tagFacet(tagRange.first, tagRange.second, "art"),
                linkFacet(linkRange.first, linkRange.second, "https://example.com")
            )
        )

        assertEquals(4, segments.size)
        assertEquals("A😀 check ", segments[0].text)
        assertNull(segments[0].href)
        assertEquals("#art", segments[1].text)
        assertEquals("https://bsky.app/hashtag/art", segments[1].href)
        assertEquals(" at ", segments[2].text)
        assertNull(segments[2].href)
        assertEquals("https://example.com", segments[3].text)
        assertEquals("https://example.com", segments[3].href)
    }

    @Test
    fun `ignores overlapping and malformed facets while keeping valid ranges`() {
        val text = "one two three"
        val twoRange = byteRangeOf(text, "two")
        val overlapRange = byteRangeOf(text, "two three")

        val malformed = linkFacet(40, 45, "https://invalid.example")
        val overlapping = tagFacet(overlapRange.first, overlapRange.second, "overlap")
        val valid = linkFacet(twoRange.first, twoRange.second, "https://two.example")

        val segments = renderFacets(text, listOf(malformed, overlapping, valid))

        assertEquals(3, segments.size)
        assertEquals("one ", segments[0].text)
        assertEquals("two", segments[1].text)
        assertEquals("https://two.example", segments[1].href)
        assertEquals(" three", segments[2].text)
    }

    @Test
    fun `filters non-http link facets`() {
        val text = "visit ftp://example.com"
        val linkRange = byteRangeOf(text, "ftp://example.com")

        val segments = renderFacets(
            text,
            listOf(linkFacet(linkRange.first, linkRange.second, "ftp://example.com"))
        )

        assertEquals(1, segments.size)
        assertEquals(text, segments[0].text)
        assertNull(segments[0].href)
    }

    @Test
    fun `renders mention facets as bluesky profile links`() {
        val text = "hi @alice.example"
        val mentionRange = byteRangeOf(text, "@alice.example")

        val segments = renderFacets(
            text,
            listOf(mentionFacet(mentionRange.first, mentionRange.second, "did:plc:alice123"))
        )

        assertEquals(2, segments.size)
        assertEquals("hi ", segments[0].text)
        assertNull(segments[0].href)
        assertEquals("@alice.example", segments[1].text)
        assertEquals("https://bsky.app/profile/did:plc:alice123", segments[1].href)
        assertEquals("richtext-mention", segments[1].cssClass)
    }

    @Test
    fun `renders mixed mention tag and link facets in order`() {
        val text = "hi @alice.example check #art https://example.com"
        val mentionRange = byteRangeOf(text, "@alice.example")
        val tagRange = byteRangeOf(text, "#art")
        val linkRange = byteRangeOf(text, "https://example.com")

        val segments = renderFacets(
            text,
            listOf(
                linkFacet(linkRange.first, linkRange.second, "https://example.com"),
                mentionFacet(mentionRange.first, mentionRange.second, "did:plc:alice123"),
                tagFacet(tagRange.first, tagRange.second, "art")
            )
        )

        assertEquals(
            listOf(
                TextSegment("hi "),
                TextSegment("@alice.example", "https://bsky.app/profile/did:plc:alice123", "richtext-mention"),
                TextSegment(" check "),
                TextSegment("#art", "https://bsky.app/hashtag/art", "richtext-tag"),
                TextSegment(" "),
                TextSegment("https://example.com", "https://example.com", "richtext-link")
            ),
            segments
        )
    }

    @Test
    fun `filters malformed mention facets`() {
        val text = "hi @alice.example"
        val mentionRange = byteRangeOf(text, "@alice.example")

        val segments = renderFacets(
            text,
            listOf(mentionFacet(mentionRange.first, mentionRange.second, "alice.example"))
        )

        assertEquals(1, segments.size)
        assertEquals(text, segments[0].text)
        assertNull(segments[0].href)
    }

    @Test
    fun `renders newlines as br tags for plain text segments`() {
        val html = createHTML().p {
            renderRichText("line one\nline two", null)
        }

        assertTrue(html.contains("line one<br"))
        assertTrue(html.contains(">line two<"))
    }

    @Test
    fun `renders newlines as br tags inside linked segments`() {
        val text = "https://example.com/path\nmore"
        val linkRange = 0 to text.toByteArray(Charsets.UTF_8).size
        val html = createHTML().p {
            renderRichText(text, listOf(linkFacet(linkRange.first, linkRange.second, "https://example.com/path")))
        }

        assertTrue(html.contains("richtext-link"))
        assertTrue(html.contains("https://example.com/path<br"))
        assertTrue(html.contains(">more</a>"))
    }

    private fun linkFacet(start: Int, end: Int, uri: String): RichtextFacet {
        return RichtextFacet().apply {
            index = RichtextFacetByteSlice().apply {
                byteStart = start
                byteEnd = end
            }
            features = mutableListOf(
                RichtextFacetLink().apply {
                    this.uri = uri
                }
            )
        }
    }

    private fun mentionFacet(start: Int, end: Int, did: String): RichtextFacet {
        return RichtextFacet().apply {
            index = RichtextFacetByteSlice().apply {
                byteStart = start
                byteEnd = end
            }
            features = mutableListOf(
                RichtextFacetMention().apply {
                    this.did = did
                }
            )
        }
    }

    private fun tagFacet(start: Int, end: Int, tag: String): RichtextFacet {
        return RichtextFacet().apply {
            index = RichtextFacetByteSlice().apply {
                byteStart = start
                byteEnd = end
            }
            features = mutableListOf(
                RichtextFacetTag().apply {
                    this.tag = tag
                }
            )
        }
    }

    private fun byteRangeOf(text: String, part: String): Pair<Int, Int> {
        val startChar = text.indexOf(part)
        require(startChar >= 0) { "Missing fragment '$part' in '$text'" }
        val endChar = startChar + part.length
        val startByte = text.substring(0, startChar).toByteArray(Charsets.UTF_8).size
        val endByte = text.substring(0, endChar).toByteArray(Charsets.UTF_8).size
        return startByte to endByte
    }
}


