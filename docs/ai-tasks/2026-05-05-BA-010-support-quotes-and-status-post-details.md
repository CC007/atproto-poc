# BA-010: Support quotes and status updates on post details page

## Metadata
- ID: `BA-010`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-05 01:25`
- Updated: `2026-05-05 01:25`
- Related Human Issue: none

## Goal
Extend `/art/{cid}` details rendering so quote-style record embeds and record-only posts without media embeds (status updates) are displayed meaningfully instead of falling back to unsupported placeholders. Preserve existing behavior for image/video/external artwork posts.

## Scope
- In scope:
  - Add quote-record handling for detail-page embed rendering (`EmbedRecordView` and record portions of `EmbedRecordWithMediaView` where relevant).
  - Add a clear no-media status-update presentation for posts whose record is `FeedPost` and `embed` is missing.
  - Reuse existing rich-text rendering patterns for post text and quoted text where available.
  - Add targeted tests for quote and no-embed status detail rendering.
- Out of scope:
  - Redesigning the overall `/art/{cid}` layout.
  - Client-side interactivity for expanding nested quote threads.
  - Full support for every ATProto record union beyond the scoped quote/status cases.

## Plan
- [ ] Inspect `ArtContentController` detail rendering path and map current unsupported cases.
- [ ] Add quote-record rendering in `renderMainEmbed` for `EmbedRecordView` and record segments of `EmbedRecordWithMediaView`.
- [ ] Add status-update fallback when `post.record` is `FeedPost` and `post.embed` is null.
- [ ] Reuse `renderRichText` for primary and quoted text rendering where facets are present.
- [ ] Add/extend tests for quote embed and no-embed status behavior in detail-page rendering code.
- [ ] Run targeted tests first; run `./gradlew test` if shared paths are impacted.
- [ ] Update `docs/ARCHITECTURE.md` and `docs/TESTING.md` if behavior/coverage documentation changes.

## Progress Log
- `2026-05-05 01:25`: Task created.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] Consider rendering lightweight author metadata for quoted records when available.
- [ ] Evaluate whether unsupported-record copy on `/art/{cid}` should include the underlying record type for debugging.

