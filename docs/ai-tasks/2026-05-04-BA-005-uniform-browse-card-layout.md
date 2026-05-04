# BA-005: Uniform browse card height with multi-image layout

## Metadata
- ID: `BA-005`
- Status: `completed`
- Owner: `ai`
- Created: `2026-05-04 22:56`
- Updated: `2026-05-05 00:36`
- Related Human Issue: none

## Goal
Make browse cards a consistent vertical size regardless of content. Apply a deliberately compact, gallery-friendly layout:
- If a post has a single image/video, show it at full card width.
- If a post has 2–4 images, show the first image large (left) and the remaining images stacked smaller to its right.
- Do **not** show post text on cards that have an image or video embed.
- If a text-only post has text longer than the card height, clip the overflow (no expanding).
- Card **width** may remain content-driven (variable), but **height** must be uniform across all cards in a row.

## Scope
- In scope:
  - CSS layout for `.post-card` fixed height.
  - Multi-image grid sub-layout within `.embed-media` wrapper.
  - Conditional text suppression in `PostSummary.kt` when embeds are present.
  - Text overflow clip styling for text-only cards.
- Out of scope:
  - Video playback (thumbnail only).
  - Masonry/variable-height layout modes.
  - Responsive breakpoints beyond basic desktop layout.

## Plan
- [x] Define card fixed height constant in CSS (suggested: CSS custom property `--card-height`).
- [x] Update `.post-card` to enforce `height: var(--card-height)` and `overflow: hidden`.
- [x] Add multi-image grid class: first image fills the left portion, images 2–4 fill a right column stacked.
- [x] Update `embedThumbnail` and `embed(EmbedImagesView)` in `PostSummary.kt` to emit the correct wrapper elements for the grid.
- [x] Suppress `record()` text output when embeds list is non-empty.
- [x] Add `text-overflow: ellipsis` / clip rules for text-only `.post-card`.
- [ ] Visual smoke test against `/browse`.
- [x] Update `docs/ARCHITECTURE.md` if component structure changes significantly.

## Progress Log
- `2026-05-04 22:56`: Task created.
- `2026-05-05 00:50`: Implemented media-aware card markup and gallery wrappers in `PostSummary.kt`.
- `2026-05-05 00:51`: Added fixed browse card height, overflow clipping, and split-gallery CSS in `browse.css`.
- `2026-05-05 00:36`: Added `PostSummaryTest`; ran `./gradlew test` successfully and updated docs.

## How Completed
- Updated `src/main/kotlin/com/github/cc007/blueart/components/overview/PostSummary.kt`:
  - Added media/text-only card classes.
  - Suppressed post text when embeds are present.
  - Added image gallery rendering for `EmbedImagesView` with split layout for 2-4 images.
  - Updated thumbnail helper to accept CSS class variants used by gallery slots.
- Updated `src/main/resources/static/css/browse.css`:
  - Added `--card-height` and fixed card-height behavior.
  - Switched cards to flex-column layout so content area clips while footer stats remain visible.
  - Added text-only line-clamp clipping.
  - Added media-single and split-gallery sizing/layout rules.
- Updated `docs/ARCHITECTURE.md` to reflect the media-aware browse card rendering behavior.
- Updated `docs/TESTING.md` to capture new PostSummary rendering coverage.
- Updated `docs/AI_TASKS.md` to move BA-005 from Active to Completed.

## Verification
- `./gradlew test`
- Manual `/browse` visual smoke test remains pending.

## Follow-ups
- [ ] Consider responsive layout adjustments for narrower viewports.
- [ ] Reassess card height constant after visual review with real data.
