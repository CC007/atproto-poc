# BA-011: Hashtag/tag browsing page

## Metadata
- ID: `BA-011`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-05 01:34`
- Updated: `2026-05-05 01:34`
- Related Human Issue: none

## Goal
Add a dedicated tag browsing experience so users can open a hashtag and view matching posts in the existing BlueArt server-rendered style. Keep navigation consistent with existing browse cards and rich-text hashtag links.

## Scope
- In scope:
  - Add a new route and page for tag browsing (for example `GET /tag/{tag}` or `GET /hashtag/{tag}`).
  - Fetch hashtag-matching posts through kbsky search APIs.
  - Reuse existing post card rendering (`postSummary`) for result grids.
  - Update rich-text hashtag link handling to point to the new in-app route.
  - Add tests for link generation and page rendering (empty and non-empty results).
- Out of scope:
  - Personalized ranking or recommendation tuning.
  - Infinite scrolling and advanced pagination UX.
  - Follow/mute hashtag account-level controls.

## Plan
- [ ] Confirm endpoint/model support for hashtag queries in kbsky.
- [ ] Implement controller route + SSR layout for tag browsing.
- [ ] Render fetched posts through existing browse-card components.
- [ ] Update hashtag URL generation in rich-text rendering to internal route.
- [ ] Add focused tests for hashtag routing and link rendering.
- [ ] Run targeted tests and broad test suite if shared rendering paths change.
- [ ] Update `docs/ARCHITECTURE.md` and `docs/TESTING.md` if behavior/coverage docs shift.

## Progress Log
- `2026-05-05 01:34`: Task created.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] Consider canonical URL format (`/tag/{tag}` vs `/hashtag/{tag}`) and redirects.
- [ ] Consider adding tag metadata header (result count, sort mode) after first release.

