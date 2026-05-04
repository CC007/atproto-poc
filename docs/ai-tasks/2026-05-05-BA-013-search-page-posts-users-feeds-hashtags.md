# BA-013: Unified search page for posts, users, feeds, and hashtags

## Metadata
- ID: `BA-013`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-05 01:34`
- Updated: `2026-05-05 01:34`
- Related Human Issue: none

## Goal
Add a single server-rendered search page that supports posts, users, feeds, and hashtags with a consistent query flow and result presentation. Ensure each result type has a clear render path and user-visible empty/error states.

## Scope
- In scope:
  - Add search route and page (for example `GET /search?q=...&type=...`).
  - Add per-type fetching for posts, users, feeds, and hashtags via kbsky APIs.
  - Reuse existing post cards for post results and add dedicated renderers for other types.
  - Add type switcher/tabs and parameter validation/defaulting.
  - Add focused tests for routing, type selection, and rendering for each result type.
- Out of scope:
  - Live/instant search or client-side hydration.
  - Saved search history and personalization.
  - Advanced ranking and relevance tuning.

## Plan
- [ ] Define query parameters (`q`, `type`) and default behavior.
- [ ] Implement search controller and SSR page shell.
- [ ] Add fetch adapters for posts/users/feeds/hashtags.
- [ ] Reuse `postSummary` for post results and add simple cards/rows for others.
- [ ] Link hashtag results to the dedicated tag-browse route (BA-011 alignment).
- [ ] Add tests for each search type, empty-state rendering, and invalid `type` fallback.
- [ ] Run targeted tests and `./gradlew test` if shared rendering/controller paths change.
- [ ] Update `docs/ARCHITECTURE.md` and `docs/TESTING.md` when implementation lands.

## Progress Log
- `2026-05-05 01:34`: Task created.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] Consider extracting search result adapters for easier future ranking experiments.
- [ ] Consider adding a dedicated user search profile-card component if the first pass is too minimal.

