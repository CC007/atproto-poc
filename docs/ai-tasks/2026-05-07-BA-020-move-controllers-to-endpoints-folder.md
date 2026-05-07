# BA-020: Create endpoints folder and move all endpoint controllers to it

## Metadata
- ID: `BA-020`
- Status: `completed`
- Owner: `ai`
- Created: `2026-05-07 00:00`
- Updated: `2026-05-07 23:45`
- Completed: `2026-05-07 23:45`
- Related Human Issue: none

## Goal
Consolidate all Spring MVC endpoint controllers under a dedicated `endpoints` package so that request-handling code is easy to locate and clearly separated from auth config, components, styling, and utilities.

## Scope
- In scope:
  - Create `com.github.cc007.blueart.endpoints` package.
  - Move the following controllers into the new package (updating package declarations and imports), preserving their subdirectory names under `endpoints/`:
    - `auth/LoginController.kt` → `endpoints/auth/LoginController.kt`
    - `browse/BrowseController.kt` → `endpoints/browse/BrowseController.kt`
    - `content/art/ArtContentController.kt` → `endpoints/content/art/ArtContentController.kt`
    - `error/ErrorController.kt` → `endpoints/error/ErrorController.kt`
    - `styling/CssController.kt` → `endpoints/styling/CssController.kt`
  - Remove now-empty top-level source directories (`auth/` for controller files, `browse/`, `content/art/`, `error/`, `styling/` for controller files) if no other files remain.
  - Update all import references throughout the codebase.
  - Update or add tests to reflect new package structure.
- Out of scope:
  - Renaming controller classes.
  - Changing controller logic or route mappings.
  - Moving non-controller files (e.g. `SecurityConfig`, auth providers, components, utilities).

## Plan
- [x] Audit each controller file for cross-package import dependencies.
- [x] Create `app/src/main/kotlin/com/github/cc007/blueart/endpoints/` directory.
- [x] Move each controller, updating its `package` declaration.
- [x] Fix any broken import statements in other files that referenced the old packages.
- [x] Remove empty source directories left behind.
- [x] Run `./gradlew :app:test` and confirm all tests pass.
- [x] Update `docs/ARCHITECTURE.md` to reflect the new package layout.

## Progress Log
- `2026-05-07 00:00`: Task created.
- `2026-05-07 23:45`: Implementation complete and tests passing.

## How Completed
- Files created under `endpoints/` subtree:
  - `endpoints/auth/LoginController.kt`
  - `endpoints/browse/BrowseController.kt`
  - `endpoints/content/art/ArtContentController.kt`
  - `endpoints/error/ErrorController.kt`
  - `endpoints/styling/CssController.kt`
  - `(test) endpoints/styling/CssControllerTest.kt`
- Package declarations updated to match new paths (`com.github.cc007.blueart.endpoints.*`).
- Imports referencing moved files had no external consumers — no other files needed import fixes.
- Old source files removed; empty directories `browse/`, `content/art/`, `content/`, `error/`, `styling/` (main + test) deleted.
- Commands run: `./gradlew :app:test`
- `docs/ARCHITECTURE.md` updated with package layout table.

## Verification
- Result: `BUILD SUCCESSFUL` — all 14 tasks, CssControllerTest (2 tests), PostSummaryTest, RichTextFacetRendererTest, and BlueArtApplicationTests all passed.
- Not verified: runtime smoke test (no running instance available).

## Follow-ups
- [ ] Verify that `endpoints/auth/LoginController.kt` has no circular dependency with `SecurityConfig` (which stays in `auth/`).

