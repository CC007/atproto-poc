# AI Tasks

## Purpose
Track AI-executed work inside the repository without mixing it with human-only GitHub Issues.

Use this page as the index for active and completed AI task records under `docs/ai-tasks/`.

## Scope and Boundaries
- GitHub Issues: human planning, discussion, and project management.
- `docs/AI_TASKS.md` + `docs/ai-tasks/`: AI execution tracking, progress, and implementation notes.
- AI tasks can optionally reference a GitHub Issue, but do not require one.

## Workflow States
- `todo`: identified but not started.
- `in_progress`: currently being worked on.
- `partial`: some work done, but not fully complete.
- `blocked`: cannot continue without missing input or dependency.
- `completed`: done and verified for the intended scope.
- `cancelled`: intentionally stopped or superseded.

## How To Use
1. Create a new task file from `docs/ai-tasks/_TEMPLATE.md`.
2. Add or update one row in the Active Tasks table.
3. Move status forward as work progresses.
4. Fill in the `How Completed` section with exact files, commands, and checks used.
5. Move the row to Completed Tasks when done.

## Active Tasks
| ID | Title | Status | Owner | Links |
| --- | --- | --- | --- | --- |
| `BA-003` | Explore CSS-in-Kotlin approach (ktor CSS DSL / Kotlinwind) | `todo` | `ai` | `docs/ai-tasks/2026-05-04-BA-003-explore-css-in-kotlin.md` |
| `BA-006` | Functional subheader filter (post type switcher) | `todo` | `ai` | `docs/ai-tasks/2026-05-04-BA-006-functional-subheader-filter.md` |
| `BA-007` | User gallery and favorites pages | `todo` | `ai` | `docs/ai-tasks/2026-05-04-BA-007-user-gallery-and-favorites.md` |
| `BA-008` | User profile page (DeviantArt-style) | `todo` | `ai` | `docs/ai-tasks/2026-05-04-BA-008-user-profile-page.md` |
| `BA-009` | Support blocked post type placeholder in browse timeline | `todo` | `ai` | `docs/ai-tasks/2026-05-05-BA-009-support-blocked-post-type.md` |
| `BA-010` | Support quotes and status updates on post details page | `todo` | `ai` | `docs/ai-tasks/2026-05-05-BA-010-support-quotes-and-status-post-details.md` |
| `BA-011` | Hashtag/tag browsing page | `todo` | `ai` | `docs/ai-tasks/2026-05-05-BA-011-tag-browsing-page.md` |
| `BA-012` | Feeds, lists, and starter packs page with embed support | `todo` | `ai` | `docs/ai-tasks/2026-05-05-BA-012-feeds-lists-starter-packs-page.md` |
| `BA-013` | Unified search page for posts, users, feeds, and hashtags | `todo` | `ai` | `docs/ai-tasks/2026-05-05-BA-013-search-page-posts-users-feeds-hashtags.md` |
| `BA-014` | Adaptive media embeds across browse/search/gallery/favorites (BA-005 follow-up) | `todo` | `ai` | `docs/ai-tasks/2026-05-05-BA-014-adaptive-media-embeds-followup-ba-005.md` |

## Completed Tasks
| ID | Title | Completed On | Notes |
| --- | --- | --- | --- |
| `BA-001` | Add in-repo AI task tracker | `2026-05-04 22:31` | `docs/ai-tasks/2026-05-04-BA-001-add-ai-task-tracker.md` |
| `BA-002` | Remove proof-of-concept framing | `2026-05-04 23:06` | `docs/ai-tasks/2026-05-04-BA-002-remove-poc-framing.md` |
| `BA-004` | Rich-text facet rendering (links, hashtags, and mentions) | `2026-05-05 00:16` | `docs/ai-tasks/2026-05-04-BA-004-rich-text-facets.md` |
| `BA-005` | Uniform browse card height with multi-image layout | `2026-05-05 00:36` | `docs/ai-tasks/2026-05-04-BA-005-uniform-browse-card-layout.md` |
