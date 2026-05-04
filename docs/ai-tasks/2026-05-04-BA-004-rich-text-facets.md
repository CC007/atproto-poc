# BA-004: Rich-text facet rendering (links and hashtags)

## Metadata
- ID: `BA-004`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-04 22:56`
- Updated: `2026-05-04 22:56`
- Related Human Issue: none

## Goal
Render ATProto rich-text facets inside post text so that clickable links and hashtags are displayed as interactive elements rather than plain text.

## Background
ATProto `FeedPost.text` is a plain UTF-8 string. `FeedPost.facets` supplies a list of byte-range annotations (type `AppBskyRichtextFacet`) that describe links (`app.bsky.richtext.facet#link`) and tags (`app.bsky.richtext.facet#tag`). Correct rendering requires slicing the text on UTF-8 byte offsets, not character offsets.

## Scope
- In scope:
  - Parse `FeedPost.facets` byte ranges.
  - Render `facet#link` features as `<a href="...">` elements.
  - Render `facet#tag` features as styled `<span>` (or `<a>` to a future hashtag search route).
  - Handle overlapping or out-of-order facets defensively.
  - Apply in both the browse card (`PostSummary.kt`) and the art detail post text.
- Out of scope:
  - Mentions (`facet#mention`) — defer to a later task.
  - Hashtag search route implementation.
  - External link previews.

## Plan
- [ ] Read `FeedPost` and `AppBskyRichtextFacet` model definitions in kbsky to confirm field names.
- [ ] Implement a `renderFacets(text: String, facets: List<AppBskyRichtextFacet>?): List<TextSegment>` utility that slices text by UTF-8 byte offsets.
- [ ] Add a `kotlinx.html` rendering function that emits plain text spans and link/tag elements.
- [ ] Integrate into `PostSummary.kt` `record()` function.
- [ ] Integrate into the art detail page post text area.
- [ ] Add unit tests for the byte-offset slicing utility.
- [ ] Run `./gradlew test`.

## Progress Log
- `2026-05-04`: Task created.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] Add mention facet rendering.
- [ ] Add hashtag search route to make hashtag links functional.
