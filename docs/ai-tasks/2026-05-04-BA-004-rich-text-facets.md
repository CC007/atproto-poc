# BA-004: Rich-text facet rendering (links, hashtags, and mentions)

## Metadata
- ID: `BA-004`
- Status: `completed`
- Owner: `ai`
- Created: `2026-05-04 22:56`
- Updated: `2026-05-05 00:19`
- Related Human Issue: none

## Goal
Render ATProto rich-text facets inside post text so that clickable links, hashtags, and mentions are displayed as interactive elements rather than plain text.

## Background
ATProto `FeedPost.text` is a plain UTF-8 string. `FeedPost.facets` supplies a list of byte-range annotations (type `AppBskyRichtextFacet`) that describe links (`app.bsky.richtext.facet#link`), tags (`app.bsky.richtext.facet#tag`), and mentions (`app.bsky.richtext.facet#mention`). Correct rendering requires slicing the text on UTF-8 byte offsets, not character offsets.

## Scope
- In scope:
  - Parse `FeedPost.facets` byte ranges.
  - Render `facet#link` features as `<a href="...">` elements.
  - Render `facet#tag` features as Bluesky hashtag links.
  - Render `facet#mention` features as Bluesky profile links by DID.
  - Render facets in art detail comments (not only post description).
  - Render newline characters as HTML `<br>` in shared rich-text output.
  - Handle overlapping or out-of-order facets defensively.
  - Apply in both the browse card (`PostSummary.kt`) and the art detail post text.
- Out of scope:
  - Hashtag search route implementation.
  - Internal BlueArt profile routes for mention targets.
  - External link previews.

## Plan
- [x] Read `FeedPost` and `AppBskyRichtextFacet` model definitions in kbsky to confirm field names.
- [x] Implement a `renderFacets(text: String, facets: List<AppBskyRichtextFacet>?): List<TextSegment>` utility that slices text by UTF-8 byte offsets.
- [x] Add a `kotlinx.html` rendering function that emits plain text spans and link/tag elements.
- [x] Integrate into `PostSummary.kt` `record()` function.
- [x] Integrate into the art detail page post text area.
- [x] Integrate into art detail comments.
- [x] Render rich text newlines as `<br>`.
- [x] Move tag color to CSS variable-based styling.
- [x] Add unit tests for the byte-offset slicing utility.
- [x] Run `./gradlew test`.

## Progress Log
- `2026-05-04 22:56`: Task created.
- `2026-05-04 23:29`: Added shared rich-text facet renderer, wired browse/detail rendering, added CSS styles, and covered UTF-8 byte-offset behavior with unit tests.
- `2026-05-04 23:55`: Extended the shared renderer and tests to support `facet#mention` as external Bluesky profile links.
- `2026-05-05 00:16`: Applied renderer to art detail comments, added newline-to-`<br>` output, and switched rich-text tag color to CSS variables.

## How Completed
- Added shared renderer in `src/main/kotlin/com/github/cc007/blueart/components/richtext/RichTextFacetRenderer.kt`.
- Extended the renderer to map `facet#mention` to external Bluesky profile URLs using the facet DID.
- Updated browse card text rendering in `src/main/kotlin/com/github/cc007/blueart/components/overview/PostSummary.kt`.
- Updated art description rendering in `src/main/kotlin/com/github/cc007/blueart/content/art/ArtContentController.kt`.
- Updated art comment rendering in `src/main/kotlin/com/github/cc007/blueart/content/art/ArtContentController.kt` to call `renderRichText` with comment facets.
- Updated `renderRichText` in `src/main/kotlin/com/github/cc007/blueart/components/richtext/RichTextFacetRenderer.kt` to output `<br>` for newline characters in plain and linked segments.
- Added facet link/tag/mention styles in `src/main/resources/static/css/browse.css` and `src/main/resources/static/css/art.css`.
- Replaced hardcoded rich-text tag colors with CSS variables in `src/main/resources/static/css/browse.css` and `src/main/resources/static/css/art.css`.
- Added tests in `src/test/kotlin/com/github/cc007/blueart/components/richtext/RichTextFacetRendererTest.kt`, including mixed mention/tag/link coverage and malformed mention rejection.
- Added renderer tests for newline-to-`<br>` behavior in `src/test/kotlin/com/github/cc007/blueart/components/richtext/RichTextFacetRendererTest.kt`.

## Verification
- `./gradlew test` (pass)
- `./gradlew test --tests com.github.cc007.blueart.components.richtext.RichTextFacetRendererTest` (pass)
- `./gradlew test --tests com.github.cc007.blueart.components.richtext.RichTextFacetRendererTest` (pass, rerun `2026-05-05 00:16`)
- Notes: Existing shutdown warnings about missing `EVENT_PUBLICATION` table still appear in test logs, but build/test task completes successfully.

## Follow-ups
- [ ] Add hashtag search route to make hashtag links functional.
- [ ] Consider internal BlueArt profile routes so mention links can stay in-app.
