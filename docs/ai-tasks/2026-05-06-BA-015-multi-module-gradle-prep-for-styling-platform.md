# BA-015: Multi-module Gradle prep for styling platform

## Metadata
- ID: `BA-015`
- Status: `completed`
- Owner: `ai`
- Created: `2026-05-06 20:15`
- Updated: `2026-05-06 23:20`
- Related Human Issue: none

## Goal
Restructure BlueArt into a multi-module Gradle project so future reusable libraries (including styling libraries) can be added without coupling everything to the application module.

## Scope
- In scope:
  - Define module boundaries for at least `app` and one reusable library-ready module group.
  - Update Gradle settings/build scripts for multi-module wiring.
  - Keep existing application behavior and routes unchanged.
  - Add/update docs that describe module boundaries.
- Out of scope:
  - Implementing the tailwind-like styling library internals.
  - Migrating existing CSS rules to utility classes.

## Plan
- [x] Propose initial module split and naming conventions.
- [x] Move current app sources to an application module with minimal path disruption.
- [x] Keep build/test commands working from repository root.
- [x] Update `docs/ARCHITECTURE.md` and `docs/DECISIONS.md` with the rationale.
- [x] Run targeted checks and `./gradlew test` after migration.

## Progress Log
- `2026-05-06 20:15`: Task created as a prerequisite for styling library work.
- `2026-05-06 23:02`: Began BA-015 implementation; reviewed repo guidance and existing Gradle layout.
- `2026-05-06 23:08`: Created `:app` and `:libs` modules; moved application sources from `src/*` to `app/src/*`; copied application build logic to `app/build.gradle.kts`.
- `2026-05-06 23:12`: Converted root build into aggregator config and added root `bootRun` alias to preserve developer workflow.
- `2026-05-06 23:15`: Updated architecture/decision/task index docs and completed verification commands.
- `2026-05-06 23:20`: Confirmed post-migration IDE task resolution issue (`:classes`) was resolved by syncing the Gradle project in IntelliJ.

## How Completed
- Updated `settings.gradle.kts` to include `:app` and `:libs`.
- Moved the executable app module code/resources/tests to `app/src/main/*` and `app/src/test/*`.
- Added `app/build.gradle.kts` with the previous Spring Boot application module plugins and dependencies.
- Replaced root `build.gradle.kts` with a multi-module aggregator configuration (`allprojects` coordinates/repos) and a root-level `bootRun` alias delegating to `:app:bootRun`.
- Added `libs/build.gradle.kts` as the reusable-library group anchor for upcoming styling modules.
- Updated `docs/ARCHITECTURE.md` and `docs/DECISIONS.md` to document module boundaries and rationale.
- Updated `docs/AI_TASKS.md` to move BA-015 from Active to Completed.
- Documented that IntelliJ Gradle sync resolved the transient root-task lookup issue seen after migration.

## Verification
- `./gradlew projects` (passed; showed `:app` and `:libs` under root)
- `./gradlew :app:test` (passed)
- `./gradlew test` (passed from repository root)
- `./gradlew bootRun --dry-run` (passed; root `bootRun` delegates to `:app:bootRun`)

## Follow-ups
- [ ] `BA-017`: Set up the initial `kolo-styles` library module in the new multi-module structure.

