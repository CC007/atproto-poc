# BA-020: Create endpoints folder and move all endpoint controllers to it

## Metadata
- ID: `BA-020`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-07 00:00`
- Updated: `2026-05-07 00:00`
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
- [ ] Audit each controller file for cross-package import dependencies.
- [ ] Create `app/src/main/kotlin/com/github/cc007/blueart/endpoints/` directory.
- [ ] Move each controller, updating its `package` declaration.
- [ ] Fix any broken import statements in other files that referenced the old packages.
- [ ] Remove empty source directories left behind.
- [ ] Run `./gradlew :app:test` and confirm all tests pass.
- [ ] Update `docs/ARCHITECTURE.md` to reflect the new package layout.

## Progress Log
- `2026-05-07 00:00`: Task created.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] Verify that `endpoints/auth/LoginController.kt` has no circular dependency with `SecurityConfig` (which stays in `auth/`).

