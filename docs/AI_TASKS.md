# AI Tasks

## Purpose
Track AI-executed work inside the repository without mixing it with human-only GitHub Issues.

Use this page as the index for active AI task records under `docs/ai-tasks/` and completed-task archive links under `openspec/archive/`.

## Scope and Boundaries
- GitHub Issues: human planning, discussion, and project management.
- `docs/AI_TASKS.md` + `docs/ai-tasks/`: AI execution tracking for non-completed tasks.
- `openspec/archive/*`: historical completed AI-task records and archived change artifacts.
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
5. Move the row to Completed Tasks when done and link it to the migrated `openspec/archive/<slug>/` entry.

## AI Working Agreement
- Before changing any AI task status to `completed`, the AI must run relevant tests/checks and record results.
- After tests/checks pass, the AI must ask for user confirmation before marking a task `completed`.

## Active Tasks
| ID | Title | Status | Owner | Links |
| --- | --- | --- | --- | --- |
| `BA‑006` | Functional subheader filter (post type switcher) | `todo` | `ai` | [📋](ai-tasks/2026-05-04-BA-006-functional-subheader-filter.md) |
| `BA‑007` | User gallery and favorites pages | `todo` | `ai` | [📋](ai-tasks/2026-05-04-BA-007-user-gallery-and-favorites.md) |
| `BA‑008` | User profile page (DeviantArt-style) | `todo` | `ai` | [📋](ai-tasks/2026-05-04-BA-008-user-profile-page.md) |
| `BA‑009` | Support blocked post type placeholder in browse timeline | `todo` | `ai` | [📋](ai-tasks/2026-05-05-BA-009-support-blocked-post-type.md) |
| `BA‑010` | Support quotes and status updates on post details page | `todo` | `ai` | [📋](ai-tasks/2026-05-05-BA-010-support-quotes-and-status-post-details.md) |
| `BA‑011` | Hashtag/tag browsing page | `todo` | `ai` | [📋](ai-tasks/2026-05-05-BA-011-tag-browsing-page.md) |
| `BA‑012` | Feeds, lists, and starter packs page with embed support | `todo` | `ai` | [📋](ai-tasks/2026-05-05-BA-012-feeds-lists-starter-packs-page.md) |
| `BA‑013` | Unified search page for posts, users, feeds, and hashtags | `todo` | `ai` | [📋](ai-tasks/2026-05-05-BA-013-search-page-posts-users-feeds-hashtags.md) |
| `BA‑014` | Adaptive media embeds across browse/search/gallery/favorites (BA-005 follow-up) | `todo` | `ai` | [📋](ai-tasks/2026-05-05-BA-014-adaptive-media-embeds-followup-ba-005.md) |


## Cancelled Tasks
| ID | Title | Cancelled On | Notes |
| --- | --- | --- | --- |
| `BA‑018` | Type-safe style collection/parsing/application framework | `2026-05-09` | Superseded by [🗄️ BA-021](../openspec/archive/css-endpoint-to-generate-kolo-css-from-request-parameters/) and [🗄️ BA-022](../openspec/archive/kolo-extension-class-generation-and-stylesheet-link-integration/). |

## Completed Tasks
| ID | Title | Completed On | Notes |
| --- | --- | --- | --- |
| `BA‑001` | Add in-repo AI task tracker | `2026-05-04 22:31` | [🗄️](../openspec/archive/add-in-repo-ai-task-tracker/) |
| `BA‑002` | Remove proof-of-concept framing | `2026-05-04 23:06` | [🗄️](../openspec/archive/remove-proof-of-concept-framing/) |
| `BA‑003` | Complete Kotlin CSS DSL stylesheet migration | `2026-05-06 19:25` | [🗄️](../openspec/archive/complete-kotlin-css-dsl-stylesheet-migration/) |
| `BA‑004` | Rich-text facet rendering (links, hashtags, and mentions) | `2026-05-05 00:16` | [🗄️](../openspec/archive/rich-text-facet-rendering/) |
| `BA‑005` | Uniform browse card height with multi-image layout | `2026-05-05 00:36` | [🗄️](../openspec/archive/uniform-browse-card-height-with-multi-image-layout/) |
| `BA‑015` | Multi-module Gradle prep for styling platform | `2026-05-06 23:15` | [🗄️](../openspec/archive/multi-module-gradle-prep-for-styling-platform/) |
| `BA‑017` | Setup `kolo-styles` library module | `2026-05-09 00:09` | [🗄️](../openspec/archive/setup-kolo-styles-library-module/) |
| `BA‑016` | Co-located tailwind-like styling architecture and design | `2026-05-09 02:10` | [🗄️](../openspec/archive/co-located-tailwind-like-styling-architecture-and-design/) |
| `BA‑020` | Create endpoints folder and move all endpoint controllers to it | `2026-05-07 23:45` | [🗄️](../openspec/archive/create-endpoints-folder-and-move-all-endpoint-controllers/) |
| `BA‑021` | CSS endpoint to generate `kolo.css` from request parameters | `2026-05-11 02:49` | [🗄️](../openspec/archive/css-endpoint-to-generate-kolo-css-from-request-parameters/) |
| `BA‑022` | `kolo {}` extension, class generation, and stylesheet link integration | `2026-05-15 11:52` | [🗄️](../openspec/archive/kolo-extension-class-generation-and-stylesheet-link-integration/) |
| `BA‑019` | Add first margin/padding utilities and preserve visual parity | `2026-05-15 14:45` | [🗄️](../openspec/archive/add-first-margin-padding-utilities-and-preserve-visual-parity/) |
