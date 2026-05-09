package com.github.cc007.blueart.endpoints.styling

import com.github.cc007.blueart.kolostyles.compiler.KoloCssCompiler
import kotlinx.css.*
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class CssController(
    private val koloCssCompiler: KoloCssCompiler
) {

    @GetMapping("/css/generated/browse.css", produces = ["text/css"])
    @ResponseBody
    fun browseStylesheet(): String = CssBuilder().apply { buildBrowseStyles() }.toString()

    @GetMapping("/css/generated/art.css", produces = ["text/css"])
    @ResponseBody
    fun artStylesheet(): String = CssBuilder().apply { buildArtStyles() }.toString()

    @GetMapping("/css/generated/kolo.css", produces = ["text/css"])
    @ResponseBody
    fun koloStylesheet(
        @RequestParam(required = false) version: String?,
        @RequestParam(required = false) kolo: String?
    ): ResponseEntity<String> {
        version?.length // kept for endpoint contract compatibility (`version` participates in URL cache keys)
        val css = koloCssCompiler.compile(kolo)

        return ResponseEntity
            .ok()
            .contentType(MediaType.valueOf("text/css"))
            .body(css)
    }

    private fun CssBuilder.buildBrowseStyles() {
        root {
            varDef("bg", "#c2dff5")
            varDef("surface", "#d4eaf9")
            varDef("surface-2", "#dff1fe")
            varDef("author-surface", "#eaf6ff")
            varDef("border", "#93b9d9")
            varDef("text", "#0e2333")
            varDef("muted", "#3d6680")
            varDef("banner", "#0a5fa8")
            varDef("primary", "#0f85fe")
            varDef("primary-2", "#3ca0ff")
            varDef("accent", "#0a62a8")
            varDef("hashtag", "#085898")
            varDef("card-height", "360px")
        }

        universal {
            boxSizing = BoxSizing.borderBox
        }

        "body.browse-body" {
            margin = Margin(0.px)
            height = 100.vh
            overflow = Overflow.hidden
            display = Display.flex
            flexDirection = FlexDirection.column
            background = "radial-gradient(circle at top, #d8effe 0%, ${cssVar("bg")} 48%)"
            color = cssColorVar("text")
            fontFamily = "Inter, \"Segoe UI\", Roboto, Helvetica, Arial, sans-serif"
        }

        a {
            color = Color.inherit
            raw("text-decoration", "none")
        }

        ".top-banner" {
            position = Position.sticky
            top = 0.px
            zIndex = 10
            display = Display.flex
            justifyContent = JustifyContent.spaceBetween
            alignItems = Align.center
            padding = Padding(0.85.rem, 1.2.rem)
            raw("border-bottom", "1px solid #1a6eb5")
            background = "linear-gradient(90deg, #0a5fa8 0%, #1478cc 50%, #0e6ec0 100%)"
            backdropFilter = "blur(10px)"
        }
        ".top-banner, .top-banner .brand p, .top-banner .brand h1" {
            color = Color("#f2f8ff")
        }
        ".brand h1" {
            margin = Margin(0.px)
            fontSize = 1.15.rem
            letterSpacing = 0.04.em
        }
        ".brand p" {
            margin = Margin(0.1.rem, 0.px, 0.px)
            color = Color("#b8dbff")
            fontSize = 0.8.rem
        }
        ".logout-button" {
            raw("border", "1px solid #9bcfff")
            borderRadius = 999.px
            background = "linear-gradient(135deg, #0f73e0, ${cssVar("primary")})"
            color = Color("#eef6ff")
            padding = Padding(0.45.rem, 0.95.rem)
            cursor = Cursor.pointer
        }
        ".browse-layout" {
            display = Display.grid
            raw("grid-template-columns", "245px minmax(0, 1fr)")
            gap = 1.rem
            width = 100.pct
            padding = Padding(1.rem)
            raw("flex", "1")
            minHeight = 0.px
        }
        ".browse-sidebar" {
            raw("align-self", "start")
            position = Position.sticky
            top = 4.5.rem
            raw("border", "1px solid ${cssVar("border")}")
            borderRadius = 12.px
            background = "linear-gradient(180deg, #dbe8f4, #d3e2f0)"
            padding = Padding(0.9.rem)
        }
        ".sidebar-title" {
            margin = Margin(0.px, 0.px, 0.75.rem)
            fontSize = 0.9.rem
            color = cssColorVar("accent")
            textTransform = TextTransform.uppercase
            letterSpacing = 0.08.em
        }
        ".sidebar-nav" {
            display = Display.grid
            gap = 0.35.rem
        }
        ".sidebar-nav a" {
            padding = Padding(0.5.rem, 0.65.rem)
            borderRadius = 8.px
            color = cssColorVar("muted")
        }
        ".sidebar-nav a:hover, .sidebar-nav a:focus-visible" {
            raw("outline", "none")
            color = cssColorVar("text")
            background = "rgba(17, 133, 254, 0.14)"
        }
        ".browse-content" {
            overflow = Overflow.auto
            minWidth = 0.px
            minHeight = 0.px
        }
        ".content-top" {
            display = Display.flex
            justifyContent = JustifyContent.spaceBetween
            gap = 1.rem
            alignItems = Align.center
            raw("margin-bottom", "0.9rem")
        }
        ".content-top h1" {
            margin = Margin(0.px)
            fontSize = 1.35.rem
        }
        ".filter-row" {
            display = Display.flex
            gap = 0.4.rem
            flexWrap = FlexWrap.wrap
        }
        ".filter-chip" {
            raw("border", "1px solid ${cssVar("border")}")
            background = "#e5eef6"
            color = cssColorVar("muted")
            borderRadius = 999.px
            padding = Padding(0.35.rem, 0.75.rem)
            cursor = Cursor.pointer
        }
        ".filter-chip-active, .filter-chip:hover, .filter-chip:focus-visible" {
            raw("outline", "none")
            color = Color("#f4f9ff")
            raw("border-color", cssVar("primary-2"))
            background = "rgba(17, 133, 254, 0.16)"
        }
        ".feed-grid" {
            display = Display.grid
            raw("grid-template-columns", "repeat(auto-fill, minmax(300px, 1fr))")
            gap = 0.8.rem
            raw("align-items", "stretch")
        }
        ".post-card" {
            raw("border", "1px solid ${cssVar("border")}")
            borderRadius = 12.px
            padding = Padding(0.75.rem)
            background = "linear-gradient(180deg, ${cssVar("surface-2")}, ${cssVar("surface")})"
            raw("box-shadow", "0 8px 20px rgba(32, 58, 83, 0.12)")
            raw("height", cssVar("card-height"))
            overflow = Overflow.hidden
            display = Display.flex
            flexDirection = FlexDirection.column
        }
        ".post-author" {
            display = Display.flex
            alignItems = Align.center
            gap = 0.55.rem
            background = cssVar("author-surface")
            raw("border", "1px solid #d2e0eb")
            borderRadius = 10.px
            padding = Padding(0.35.rem, 0.45.rem)
        }
        ".author-avatar" {
            raw("border-radius", "50%")
            raw("border", "1px solid #89a9c4")
        }
        ".author-meta" {
            display = Display.grid
            gap = 0.1.rem
        }
        ".author-name" {
            display = Display.block
            fontSize = 0.93.rem
        }
        ".author-handle" {
            color = cssColorVar("muted")
            fontSize = 0.8.rem
        }
        ".post-content" {
            marginTop = 0.6.rem
            raw("flex", "1")
            minHeight = 0.px
            overflow = Overflow.hidden
        }
        ".post-text" {
            margin = Margin(0.px)
            raw("line-height", "1.4")
            raw("overflow-wrap", "anywhere")
            raw("word-break", "break-word")
        }
        ".post-card-text-only .post-text" {
            raw("display", "-webkit-box")
            raw("-webkit-box-orient", "vertical")
            raw("-webkit-line-clamp", "11")
            overflow = Overflow.hidden
        }
        ".richtext-link, .richtext-mention, .richtext-tag" {
            raw("text-decoration", "underline")
            raw("text-underline-offset", "2px")
        }
        ".richtext-link, .richtext-mention" {
            color = cssColorVar("accent")
        }
        ".richtext-mention" {
            raw("font-weight", "600")
        }
        ".richtext-tag" {
            color = cssColorVar("hashtag")
            raw("font-weight", "600")
        }
        ".richtext-link:hover, .richtext-link:focus-visible, .richtext-mention:hover, .richtext-mention:focus-visible, .richtext-tag:hover, .richtext-tag:focus-visible" {
            opacity = 0.85
        }
        ".post-labels" {
            margin = Margin(0.55.rem, 0.px, 0.px)
            color = cssColorVar("muted")
            fontSize = 0.83.rem
        }
        ".embed-media" {
            marginTop = 0.px
            borderRadius = 8.px
            width = 100.pct
            height = 100.pct
            raw("border", "1px solid #a6bfd2")
            display = Display.block
            objectFit = ObjectFit.cover
        }
        ".post-card-media .embed-media-single, .post-card-media .embed-blur-clip" {
            marginTop = 0.5.rem
            height = 170.px
        }
        ".embed-media-grid" {
            marginTop = 0.5.rem
            display = Display.grid
            raw("grid-template-columns", "2fr 1fr")
            gap = 0.35.rem
            height = 180.px
        }
        ".embed-media-grid-main, .embed-media-grid-side" {
            minWidth = 0.px
            minHeight = 0.px
        }
        ".embed-media-grid-side" {
            display = Display.grid
            gap = 0.35.rem
            raw("grid-template-rows", "repeat(3, minmax(0, 1fr))")
        }
        ".embed-media-grid-main .embed-blur-clip, .embed-media-grid-side .embed-blur-clip" {
            marginTop = 0.px
            height = 100.pct
        }
        ".embed-blur-clip" {
            display = Display.block
            borderRadius = 8.px
            overflow = Overflow.hidden
            width = 100.pct
        }
        ".embed-blur-clip .embed-media" {
            marginTop = 0.px
        }
        ".embed-media-blur" {
            filter = "blur(18px)"
            raw("transition", "filter 0.25s ease")
        }
        ".embed-blur-clip:hover .embed-media-blur" {
            filter = "blur(0)"
        }
        ".embed-record" {
            marginTop = 0.5.rem
        }
        ".parent-post .post-card" {
            marginTop = 0.65.rem
            background = "#eaf3fa"
            raw("height", "auto")
            overflow = Overflow.visible
        }
        ".post-stats" {
            marginTop = 0.7.rem
            display = Display.flex
            alignItems = Align.center
            gap = 0.85.rem
            flexWrap = FlexWrap.wrap
            color = cssColorVar("muted")
            fontSize = 0.78.rem
            raw("border-top", "1px solid rgba(90, 115, 137, 0.26)")
            raw("padding-top", "0.5rem")
            raw("flex-shrink", "0")
        }
        ".post-open-link" {
            margin = Margin(0.55.rem, 0.px, 0.px)
            fontSize = 0.8.rem
            color = cssColorVar("accent")
        }
        ".post-open-link a" {
            raw("text-decoration", "underline")
            raw("text-underline-offset", "2px")
        }
        ".post-stat-item" {
            display = Display.inlineFlex
            alignItems = Align.center
            gap = 0.28.rem
            raw("line-height", "1")
        }
        ".post-stat-icon" {
            display = Display.inlineFlex
            width = 1.2.rem
            height = 0.9.rem
        }
        ".post-stat-icon svg" {
            width = 100.pct
            height = 100.pct
        }
        ".post-stat-icon path" {
            raw("fill", "none")
            raw("stroke", "currentColor")
            raw("stroke-width", "2")
            raw("stroke-linecap", "round")
            raw("stroke-linejoin", "round")
        }
        ".post-stat-icon-like svg" {
            raw("transform", "translateY(0.5px)")
        }
        ".post-stat-icon-quote svg" {
            raw("transform", "translateY(1px)")
        }
        ".post-stat-icon-quote path" {
            raw("fill", "currentColor")
            raw("stroke", "none")
        }
        ".post-stat-icon-repost svg" {
            raw("transform", "translateY(1px)")
        }
        ".post-stat-icon-reply svg" {
            raw("transform", "translateY(1px)")
        }
        ".post-stat-icon-bookmark svg" {
            raw("transform", "translateY(1px)")
        }

        media("(max-width: 960px)") {
            ".browse-layout" {
                raw("grid-template-columns", "1fr")
            }
            ".browse-sidebar" {
                position = Position.static
            }
        }
        media("(max-width: 640px)") {
            ".content-top" {
                flexDirection = FlexDirection.column
                raw("align-items", "flex-start")
            }
        }
    }

    private fun CssBuilder.buildArtStyles() {
        root {
            varDef("bg", "#c2dff5")
            varDef("surface", "#d4eaf9")
            varDef("surface-2", "#dff1fe")
            varDef("border", "#93b9d9")
            varDef("text", "#0e2333")
            varDef("muted", "#3d6680")
            varDef("accent", "#0a62a8")
            varDef("hashtag", "#085898")
        }

        universal {
            boxSizing = BoxSizing.borderBox
        }

        "body.art-body" {
            margin = Margin(0.px)
            minHeight = 100.vh
            background = "radial-gradient(circle at top, #d8effe 0%, ${cssVar("bg")} 48%)"
            color = cssColorVar("text")
            fontFamily = "Inter, \"Segoe UI\", Roboto, Helvetica, Arial, sans-serif"
        }

        ".art-layout" {
            maxWidth = 1080.px
            margin = Margin(0.px, LinearDimension.auto)
            padding = Padding(1.rem)
        }
        ".art-content" {
            display = Display.grid
            gap = 1.rem
        }
        ".content-top" {
            display = Display.grid
            gap = 0.2.rem
        }
        ".art-title" {
            margin = Margin(0.px)
            fontSize = 1.5.rem
        }
        ".art-byline" {
            margin = Margin(0.px)
            color = cssColorVar("muted")
            fontSize = 0.9.rem
        }
        ".art-card, .comments" {
            raw("border", "1px solid ${cssVar("border")}")
            borderRadius = 12.px
            background = "linear-gradient(180deg, ${cssVar("surface-2")}, ${cssVar("surface")})"
            raw("box-shadow", "0 8px 20px rgba(32, 58, 83, 0.12)")
            padding = Padding(1.rem)
        }
        ".art-embed" {
            borderRadius = 10.px
            background = "#cfe4f4"
            raw("border", "1px solid #b9d3e7")
            padding = Padding(0.5.rem)
        }
        ".art-image-grid" {
            display = Display.grid
            raw("grid-template-columns", "repeat(auto-fit, minmax(250px, 1fr))")
            gap = 0.5.rem
        }
        ".art-image-single" {
            display = Display.block
        }
        ".art-image" {
            display = Display.block
            width = 100.pct
            maxHeight = 72.vh
            objectFit = ObjectFit.contain
            borderRadius = 8.px
            background = "#d7e8f6"
        }
        ".art-description" {
            marginTop = 0.9.rem
        }
        ".art-description h2, .comments h2" {
            margin = Margin(0.px, 0.px, 0.6.rem)
            fontSize = 1.rem
            color = cssColorVar("accent")
        }
        ".art-text, .comment-text, .art-empty, .art-external" {
            margin = Margin(0.px)
            raw("line-height", "1.45")
            raw("overflow-wrap", "anywhere")
            raw("word-break", "break-word")
        }
        ".richtext-link, .richtext-mention, .richtext-tag" {
            raw("text-decoration", "underline")
            raw("text-underline-offset", "2px")
        }
        ".richtext-link, .richtext-mention" {
            color = cssColorVar("accent")
        }
        ".richtext-mention" {
            raw("font-weight", "600")
        }
        ".richtext-tag" {
            color = cssColorVar("hashtag")
            raw("font-weight", "600")
        }
        ".richtext-link:hover, .richtext-link:focus-visible, .richtext-mention:hover, .richtext-mention:focus-visible, .richtext-tag:hover, .richtext-tag:focus-visible" {
            opacity = 0.85
        }
        ".comments" {
            display = Display.grid
            gap = 0.7.rem
        }
        ".comment" {
            raw("border", "1px solid #bcd4e5")
            borderRadius = 10.px
            background = "#e8f3fc"
            padding = Padding(0.6.rem)
            display = Display.grid
            gap = 0.45.rem
        }
        ".comment.depth-1" {
            raw("margin-left", "0.8rem")
        }
        ".comment.depth-2" {
            raw("margin-left", "1.6rem")
        }
        ".comment.depth-3" {
            raw("margin-left", "2.4rem")
        }
        ".comment.depth-4" {
            raw("margin-left", "3.2rem")
        }
        ".comment-header" {
            display = Display.flex
            alignItems = Align.center
            gap = 0.45.rem
        }
        ".comment-avatar" {
            raw("border-radius", "50%")
            raw("border", "1px solid #89a9c4")
        }
        ".comment-meta" {
            display = Display.grid
            raw("line-height", "1.15")
        }
        ".comment-author" {
            fontSize = 0.88.rem
        }
        ".comment-handle" {
            color = cssColorVar("muted")
            fontSize = 0.76.rem
        }

        media("(max-width: 700px)") {
            ".art-layout" {
                padding = Padding(0.75.rem)
            }
            ".art-card, .comments" {
                padding = Padding(0.8.rem)
            }
        }
    }

    private fun CssBuilder.varDef(name: String, value: String) {
        put("--$name", value)
    }

    private fun CssBuilder.raw(name: String, value: String) {
        put(name, value)
    }

    private fun cssVar(name: String): String = "var(--$name)"

    private fun cssColorVar(name: String): Color = Color(cssVar(name))
}

