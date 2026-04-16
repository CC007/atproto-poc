# Conversation Context Handoff

## Purpose
This file captures the current project and conversation state so a new AI chat can continue implementation without re-discovery.

## Project Snapshot
- Project: `atproto-poc` (Kotlin + Spring Boot + `kotlinx.html` server-rendered UI).
- Domain: Bluesky/ATProto timeline browsing and post detail for art-focused content.
- Build: Gradle Kotlin DSL.
- Bluesky library: `work.socialhub.kbsky` (`core-jvm` snapshot in this project).

## User Intent So Far
- Improve feed card text wrapping for long unbroken words.
- Replace plain stat labels with clean icon-based stat row.
- Add hover tooltips for stat meaning.
- Add bookmark count alongside like/quote/repost/reply.
- Implement art detail page (DeviantArt-like layout):
  - large main embed,
  - record text under it,
  - comments beneath.
- Enable navigation from feed cards to art detail page.

## Implemented Changes (Current State)

### 1) Feed card text wrapping
- File: `src/main/resources/static/css/browse.css`
- `.post-text` now includes:
  - `overflow-wrap: anywhere;`
  - `word-break: break-word;`

### 2) Stat row redesign
- File: `src/main/kotlin/com/github/cc007/poc/atproto/components/overview/PostSummary.kt`
- Added inline SVG icons and compact `statItem(...)` rendering.
- Added `title` + `aria-label` tooltips/accessibility on each stat item.
- Added stat set: likes, quotes, reposts, replies, bookmarks.
- Bookmark count is threaded for top-level posts and embedded record views.

### 3) Stat/link styling
- File: `src/main/resources/static/css/browse.css`
- Added icon alignment/tuning classes (`post-stat-icon-*`).
- Added `.post-open-link` styles for detail navigation link visibility.

### 4) Browse -> Art navigation
- File: `src/main/kotlin/com/github/cc007/poc/atproto/components/overview/PostSummary.kt`
- `postSummary(...)` now threads `postUri` and `postCid`.
- Renders `Open artwork` link as:
  - `/art/{cid}?uri={urlEncodedUri}`
- Link shown only when there are embeds and both `uri` + `cid` exist.

### 5) Art detail page implementation
- File: `src/main/kotlin/com/github/cc007/poc/atproto/content/art/ArtContentController.kt`
- Route: `GET /art/{cid}` with optional query `uri`.
- Data fetching:
  - Primary: `getPostsBlocking(FeedGetPostsRequest(auth, uris=[uri]))`.
  - Thread: `getPostThreadBlocking(FeedGetPostThreadRequest(auth, uri, depth=5))`.
  - Fallback when only cid exists: search current timeline (`getTimelineBlocking`).
- Rendering:
  - title/byline,
  - main embed section (`renderMainEmbed`),
  - description from `FeedPost.text`,
  - comments flattened from thread replies (`collectComments`).
- Handles unsupported/missing media with fallback text.

### 6) Art page styling
- File: `src/main/resources/static/css/art.css`
- Added DeviantArt-inspired card layout and spacing:
  - hero embed container,
  - description block,
  - comment cards with depth indentation classes.

## Key Routes
- `GET /browse` -> timeline cards.
- `GET /art/{cid}?uri=...` -> art detail page.

## Important Files
- `src/main/kotlin/com/github/cc007/poc/atproto/browse/BrowseController.kt`
- `src/main/kotlin/com/github/cc007/poc/atproto/components/overview/PostSummary.kt`
- `src/main/kotlin/com/github/cc007/poc/atproto/content/art/ArtContentController.kt`
- `src/main/resources/static/css/browse.css`
- `src/main/resources/static/css/art.css`

## Notable Design Decisions
- Keep rendering server-side with `kotlinx.html` (no SPA framework).
- Use inline SVG for deterministic icon look (no emoji dependency).
- Use URI-driven post lookup for deterministic detail fetch; cid-only fallback is best-effort.
- Keep comments simple (flattened with depth class), optimized for readability.

## Known Gaps / Follow-ups
- Current cid-only fallback checks only timeline page data; may miss posts not in timeline response.
- Comments are flattened; true tree UI/thread collapse not implemented.
- Art detail currently supports image/video-thumbnail/external-thumb and record-with-media path; unsupported embeds show fallback text.
- No dedicated automated tests were added for these flows (POC state).

## Suggested Next Tasks for a New AI Chat
1. Improve `/art/{cid}` lookup robustness (resolve by URI from browse state/cache or add DID+rkey route format).
2. Add richer art page metadata panel (stats, publish time, author actions).
3. Render nested comment tree visually (thread connectors, collapse/expand).
4. Add lightweight integration tests for:
   - `/browse` response includes `Open artwork` links,
   - `/art/{cid}` renders media + description + comments.
5. Refine embed handling for additional Bluesky embed variants.

## Prompt Starter (Copy Into New Chat)
"Read `CONTEXT.md` and continue from current state. First validate the art detail flow between `BrowseController`, `PostSummary`, and `ArtContentController`; then implement the top-priority follow-up from `Known Gaps / Follow-ups` with minimal regressions."

## Last Verified State
- Kotlin compile was previously successful after the art controller integration.
- Workspace currently includes recent changes in browse/art controllers and CSS files listed above.

