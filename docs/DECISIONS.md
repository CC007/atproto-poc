# Decisions

## Purpose
Track technical decisions and rationale in one place. Use this file for concise entries, or add detailed ADRs under `docs/adr/` when needed.

## Decision Log

### D-001: Keep server-rendered UI
- Status: accepted
- Context: Project is focused on fast iteration for browse/detail pages.
- Decision: Render pages server-side with `kotlinx.html` instead of introducing an SPA framework.
- Consequences: Lower frontend complexity, but fewer client-side interaction patterns out of the box.

### D-002: Inline SVG for stat icons
- Status: accepted
- Context: Stat rows require consistent icon rendering across environments.
- Decision: Use inline SVG icons in HTML components.
- Consequences: Deterministic rendering and no emoji/font dependency.

### D-003: URI-first art detail lookup
- Status: accepted
- Context: Post detail requests are most reliable with URI identity.
- Decision: Resolve `/art/{cid}` primarily via provided `uri`, with CID-only fallback as best-effort.
- Consequences: More stable detail fetch when URI is present; CID-only routes may miss off-timeline posts.

### D-004: Track AI tasks in repository docs
- Status: accepted
- Context: GitHub Issues are reserved for human planning and ownership, but AI work requires versioned status and completion traceability.
- Decision: Introduce `docs/AI_TASKS.md` as an index and maintain per-task records under `docs/ai-tasks/`.
- Consequences: AI progress is reviewable in Git history; contributors must keep task records updated as part of handoff.

### D-005: Rename package and artifact from `poc`/`atproto-poc` to `blueart`
- Status: accepted
- Context: The project has grown beyond a proof-of-concept and carries enough real capability to be treated as a real web application. The old group `com.github.cc007.poc.atproto` and artifact `atproto-poc` signalled throwaway/experimental intent.
- Decision: Rename the root package to `com.github.cc007.blueart`, the Gradle artifact description to `blueart`, the Spring application name to `blueart`, and the main entry-point class to `BlueArtApplication`. Remove all "proof-of-concept" prose from documentation.
- Consequences: Any external tooling or CI that references the old artifact name or package must be updated (see BA-002 follow-ups). Build and tests confirmed passing after rename.

### D-006: Adopt a minimal-diff multi-module Gradle structure
- Status: accepted
- Context: Upcoming styling platform work requires reusable library modules without coupling everything to the executable app module.
- Decision: Split the build into `:app` (Spring Boot executable) and `:libs` (library group anchor) while preserving existing app code, package names, routes, and root-level build/test workflows.
- Consequences: Future reusable modules can be added under `:libs:*` without another structural migration; application code now lives under `app/src/*` and should be targeted with `:app:*` tasks when module-specific execution is needed.

### D-007: Adopt Kolo co-located utility styling contract
- Status: accepted
- Context: Styling needs a Kotlin-first co-located authoring flow while preserving maintainability, pseudo/media support, and predictable CSS delivery/caching.
- Decision:
  - Author utilities via element-attached `kolo { ... }` DSL.
  - Use Tailwind-style token naming with pseudo/media variants.
  - Defer arbitrary value tokens (`[...]`) for now.
  - Serve generated utility CSS from `/css/generated/kolo.css` using `version=<build git sha>` and `kolo=<semicolon-separated canonical token list>` query params.
  - Canonicalize `kolo` with dedupe + variant-aware deterministic sorting before link emission.
  - During migration, keep page CSS and `kolo.css` side-by-side; remove only declarations already migrated to Kolo utilities.
  - Use versioned URL caching as the primary cache strategy (`ETag` optional).
- Consequences: Implementation tasks must preserve canonical token ordering and URL stability, and migration work should be incremental to avoid regressions while both stylesheet paths coexist.

