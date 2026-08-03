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

### D-008: Move browse/art spacing ownership to Kolo utilities with max-width exceptions
- Status: accepted
- Context: Browse/art spacing migration removed most margin/padding declarations from `CssController`, but two responsive spacing rules still rely on `@media (max-width: 700px)` while Kolo currently supports min-width media variants only.
- Decision:
  - Add `mx-auto` as a first-class spacing utility token and DSL helper (`mxAuto`) for centering cases previously expressed as `margin: 0 auto`.
  - Author migrated browse/art spacing via typed Kolo helpers at render call sites.
  - Keep only the two max-width responsive spacing declarations in `CssController` as explicitly tagged `kolo-exception` rules until Kolo gains max-width variant support.
- Consequences: Spacing ownership is clearer and co-located for migrated elements, and future cleanup can target only the documented responsive exceptions when max-width variants are implemented.

### D-009: Add a dedicated Playwright visual regression lane
- Status: accepted
- Context: CSS-to-Kolo migration needs deterministic visual regression coverage on key user-facing routes without introducing a JavaScript test stack.
- Decision:
  - Add dedicated `:visual-tests` module for black-box visual tests.
  - Run snapshots in headless Chromium and Firefox with normalized rendering settings.
  - Keep visual execution as a separate Gradle lane (`visualTest`) with explicit developer-gated baseline updates (`updateVisualBaselines`).
- Consequences: Visual regressions now emit expected/actual/diff artifacts for route-level review, and baseline updates require explicit developer acknowledgement.

### D-010: Add allow-listed Kolo display utilities and migrate browse/art display ownership
- Status: accepted
- Context: Display declarations for browse/art were still split between page CSS and Kolo tokenized utility rendering, unlike spacing ownership that already migrated into Kolo.
- Decision:
  - Add a dedicated display utility family in `:libs:kolo-styles` via `DisplayToken`, `DisplayParserHook`, and `DisplayGeneratorHook`.
  - Freeze the explicit Tailwind-compatible display allow-list (`block`, `inline`, `inline-block`, `flow-root`, `flex`, `inline-flex`, `grid`, `inline-grid`, `contents`, `list-item`, `hidden`, `table`, `inline-table`, `table-caption`, `table-cell`, `table-column`, `table-column-group`, `table-header-group`, `table-row-group`, `table-row`, `table-footer-group`).
  - Add typed Kotlin display DSL helpers under `kolostyles.dsl.display` on both `KoloScope` and `KoloVariantScope`, including one-to-one camelCase mappings for hyphenated tokens (for example `inlineBlock -> inline-block`, `flowRoot -> flow-root`, `tableRowGroup -> table-row-group`).
  - Reuse existing state/media variant semantics (`hover`, `focus`, `focus-visible`, `active`, `visited`, `sm`/`md`/`lg`/`xl`/`2xl`) across spacing and display parser/generator families.
  - Migrate browse/art render paths to display DSL usage and remove duplicate `display` declarations from generated page CSS for migrated selectors.
- Consequences: Display layout intent is now co-located with server-rendered markup through typed Kolo APIs, `/css/generated/kolo.css` supports both spacing and display utilities through the same deterministic hook pipeline, and browse/art page CSS remains focused on non-migrated declarations.
