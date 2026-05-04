# BA-012: Feeds, lists, and starter packs page with embed support

## Metadata
- ID: `BA-012`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-05 01:34`
- Updated: `2026-05-05 01:34`
- Related Human Issue: none

## Goal
Create a dedicated discovery page for feeds, lists, and starter packs, and add support for rendering these entities when they appear in embeds. Reduce unsupported-placeholder output in browse/detail experiences for these known ATProto object types.

## Scope
- In scope:
  - Add a route/page for browsing feeds, lists, and starter packs in sections or tabs.
  - Fetch data for all three types from kbsky-backed APIs.
  - Add server-rendered collection cards for each type.
  - Extend `PostSummary` embed rendering for feed/list/starter-pack records where possible.
  - Extend art-detail embed rendering for the same record/embed types where applicable.
  - Add tests for each new rendering branch and collection section.
- Out of scope:
  - Follow/subscribe/join actions or write APIs.
  - Full parity with Bluesky app metadata and interactions.
  - Deep pagination, sorting controls, or personalization.

## Plan
- [ ] Confirm kbsky retrieval endpoints/models for feeds, lists, and starter packs.
- [ ] Implement controller route + SSR page with three sections.
- [ ] Build reusable cards for feed/list/starter-pack rows.
- [ ] Add embed support in `PostSummary` for relevant record/union types.
- [ ] Add embed support in post-details renderer for matching types.
- [ ] Add focused tests per type and fallback behavior.
- [ ] Run targeted tests and full suite if shared components are updated.
- [ ] Update `docs/ARCHITECTURE.md` and `docs/TESTING.md` to document new route and coverage.

## Progress Log
- `2026-05-05 01:34`: Task created.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] Add filter/sort controls once baseline page structure is stable.
- [ ] Evaluate reusable component extraction if card variants diverge.

