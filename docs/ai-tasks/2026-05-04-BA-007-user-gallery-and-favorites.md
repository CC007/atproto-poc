# BA-007: User gallery and favorites pages

## Metadata
- ID: `BA-007`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-04 22:56`
- Updated: `2026-05-04 22:56`
- Related Human Issue: none

## Goal
Add two new routes for per-user browsing:
- **Gallery** (`/user/{handle}/gallery`): all art posts by a specific user, displayed in the same browse-card grid.
- **Favorites** (`/user/{handle}/favorites`): posts the user has liked, filtered to art posts.

## Scope
- In scope:
  - `GET /user/{handle}/gallery` — fetch the user's authored feed via ATProto `getAuthorFeed`, filter to art posts.
  - `GET /user/{handle}/favorites` — fetch liked posts via ATProto `getActorLikes`, filter to art posts.
  - Shared page layout reusing existing browse-card grid component.
  - Page heading showing which user's gallery/favorites is being viewed.
  - Links from author banners on browse cards to the relevant gallery page.
  - Update `docs/ARCHITECTURE.md` routes table.
- Out of scope:
  - Pagination (add in a follow-up once the basic routes work).
  - Authenticated viewing of private likes.
  - Upload/edit functionality.

## Plan
- [ ] Review kbsky API for `getAuthorFeed` and `getActorLikes` methods.
- [ ] Create `UserGalleryController` (or extend `BrowseController`) with two route handlers.
- [ ] Reuse existing browse-card grid rendering or extract a shared `postGrid()` component.
- [ ] Add author handle links in `authorBanner()` in `PostSummary.kt`.
- [ ] Apply art-post filter consistent with `BrowseFilter.ART` logic from BA-006.
- [ ] Run `./gradlew test`.
- [ ] Update `docs/ARCHITECTURE.md`.

## Progress Log
- `2026-05-04`: Task created.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] Add pagination to gallery and favorites.
- [ ] Link gallery/favorites from the user profile page (BA-008).
