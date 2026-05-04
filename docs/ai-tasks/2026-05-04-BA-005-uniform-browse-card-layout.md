# BA-005: Uniform browse card height with multi-image layout

## Metadata
- ID: `BA-005`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-04`
- Updated: `2026-05-04`
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
- [ ] Define card fixed height constant in CSS (suggested: CSS custom property `--card-height`).
- [ ] Update `.post-card` to enforce `height: var(--card-height)` and `overflow: hidden`.
- [ ] Add multi-image grid class: first image fills the left portion, images 2–4 fill a right column stacked.
- [ ] Update `embedThumbnail` and `embed(EmbedImagesView)` in `PostSummary.kt` to emit the correct wrapper elements for the grid.
- [ ] Suppress `record()` text output when embeds list is non-empty.
- [ ] Add `text-overflow: ellipsis` / clip rules for text-only `.post-card`.
- [ ] Visual smoke test against `/browse`.
- [ ] Update `docs/ARCHITECTURE.md` if component structure changes significantly.

## Progress Log
- `2026-05-04`: Task created.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] Consider responsive layout adjustments for narrower viewports.
- [ ] Reassess card height constant after visual review with real data.

