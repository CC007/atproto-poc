# BA-014: Adaptive media embeds across browse/search/gallery/favorites (BA-005 follow-up)

## Metadata
- ID: `BA-014`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-05 01:44`
- Updated: `2026-05-05 01:44`
- Related Human Issue: none

## Goal
Improve image/video embed presentation quality across browse, search, gallery, and favorites by reducing aggressive cropping, tightening excessive padding, and supporting variable media widths based on aspect ratio and multi-image context. This task is a direct follow-up to `BA-005`.

## Scope
- In scope:
  - Improve shared media card rendering so adaptive behavior applies wherever `postSummary` cards are used.
  - Tune image/video thumbnail treatment to preserve more content (less crop-heavy defaults).
  - Reduce unnecessary whitespace around media blocks.
  - Add aspect-ratio-aware and gallery-context-aware layout classes for variable width behavior.
  - Only start cropping once embed has an aspect ratio of 1:4 or 4:1
  - Ensure multi-image layouts use sensible width rules and spacing for 2-4 images.
  - Add focused tests for new rendering classes/branches in component tests.
- Out of scope:
  - Full masonry/waterfall redesign.
  - New client-side JS measurement/hydration system.
  - New video playback controls beyond thumbnail presentation.

## Plan
- [ ] Define adaptive layout behavior for single-image, video thumbnail, and multi-image cards.
- [ ] Update `PostSummary` media rendering classes to expose aspect and gallery context.
- [ ] Refine CSS in shared media/card styles for less cropping and smaller padding.
- [ ] Validate that browse/search/gallery/favorites surfaces inherit updated behavior.
- [ ] Add/extend tests (for example `PostSummaryTest`) to verify adaptive class emission and key branches.
- [ ] Run targeted tests, then `./gradlew test` if shared rendering paths are broadly impacted.
- [ ] Update `docs/ARCHITECTURE.md` and `docs/TESTING.md` when implementation lands.

## Progress Log
- `2026-05-05 01:44`: Task created as a follow-up to `BA-005`.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] Add responsive breakpoint tuning if adaptive widths reduce readability on narrow screens.
- [ ] Evaluate using API-provided image aspect metadata to reduce CSS-only heuristics.

