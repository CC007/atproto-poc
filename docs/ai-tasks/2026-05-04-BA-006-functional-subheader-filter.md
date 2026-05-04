# BA-006: Functional subheader filter (post type switcher)

## Metadata
- ID: `BA-006`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-04`
- Updated: `2026-05-04`
- Related Human Issue: none

## Goal
Replace the current non-functional subheader with a working filter bar that lets users switch the browse view between post-type categories: **Art posts**, **Status updates**, **Quote posts**, and **Reposts**.

## Scope
- In scope:
  - Define post-type filter enum/sealed class (`BrowseFilter`: `ART`, `STATUS`, `QUOTE`, `REPOST`).
  - Pass the active filter as a query parameter (e.g. `GET /browse?filter=art`).
  - Filter feed items in `BrowseController` before rendering.
  - Render the subheader as a set of links/tabs highlighting the active selection.
  - Update `docs/ARCHITECTURE.md` routes table.
- Out of scope:
  - Client-side tab switching without page reload (keep server-rendered).
  - Pagination within filtered views (separate task if needed).
  - Persisting the user's filter preference across sessions.

## Plan
- [ ] Inspect the existing subheader component and `BrowseController` to understand current structure.
- [ ] Define `BrowseFilter` enum with `art`, `status`, `quote`, `repost` values.
- [ ] Add `?filter=` query parameter handling to `GET /browse`.
- [ ] Implement filter logic: classify each `FeedDefsPostView` by post type.
- [ ] Update the subheader renderer to emit `<a>` tab links with an `active` class for the current filter.
- [ ] Default to `art` filter when no parameter is provided.
- [ ] Run `./gradlew test` and verify `/browse?filter=art` and `/browse?filter=status` render correctly.
- [ ] Update `docs/ARCHITECTURE.md` primary routes section.

## Progress Log
- `2026-05-04`: Task created.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] Add integration tests for each filter value.
- [ ] Consider session/cookie persistence for the selected filter.

