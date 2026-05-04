# BA-008: User profile page (DeviantArt-style)

## Metadata
- ID: `BA-008`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-04 22:56`
- Updated: `2026-05-05 00:19`
- Related Human Issue: none

## Goal
Add a `GET /user/{handle}` profile page inspired by DeviantArt's layout. The page aggregates multiple facets of a user's presence in a single view.

## Intended Page Sections

| Section | Content | Source |
| --- | --- | --- |
| **Profile header** | Avatar, display name, handle, bio/description, follower/following counts | `getProfile` |
| **Gallery preview** | Small grid of the user's latest art posts (up to ~9) with a "View all" link → `/user/{handle}/gallery` | `getAuthorFeed` filtered to art |
| **Favorites preview** | Small grid of the user's liked art posts (up to ~6) with a "View all" link → `/user/{handle}/favorites` | `getActorLikes` filtered to art |
| **Following / Followers** | Compact avatar-list rows with counts and "View all" links | `getFollows` / `getFollowers` |
| **Profile comments** | Flat list of posts that mention or reply to the user directly (nearest ATProto equivalent) | `searchPosts` or `listNotifications` scoped to mentions |

## Scope
- In scope:
  - `GET /user/{handle}` route and controller.
  - All five sections above as distinct `kotlinx.html` sub-components.
  - Links from each section to the corresponding full pages (BA-007, future following/followers pages).
  - Update `docs/ARCHITECTURE.md`.
- Out of scope:
  - Editing profile info.
  - Full following/followers paginated list pages (separate tasks).
  - Real-time comment feed — static render on page load only.
  - Authenticated actions (follow, like).

## Plan
- [ ] Review kbsky API for `getProfile`, `getFollows`, `getFollowers`, `getActorLikes`, `getAuthorFeed`.
- [ ] Create `UserProfileController` with `GET /user/{handle}`.
- [ ] Build `profileHeader()` component.
- [ ] Build `galleryPreview()` component (reuse browse-card thumbnails, limit to 9).
- [ ] Build `favoritesPreview()` component (reuse browse-card thumbnails, limit to 6).
- [ ] Build `followList()` component for both following and followers previews.
- [ ] Investigate best ATProto endpoint for profile-level comments/mentions; implement `profileComments()` component.
- [ ] Wire all sections into the profile page layout.
- [ ] Run `./gradlew test`.
- [ ] Update `docs/ARCHITECTURE.md` routes table.

## Progress Log
- `2026-05-04 22:56`: Task created.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] Full following/followers paginated list pages.
- [ ] Profile edit page (authenticated).
- [ ] Pagination within gallery and favorites preview once BA-007 is done.
