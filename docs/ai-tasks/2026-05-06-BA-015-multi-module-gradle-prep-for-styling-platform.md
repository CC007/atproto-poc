# BA-015: Multi-module Gradle prep for styling platform

## Metadata
- ID: `BA-015`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-06 20:15`
- Updated: `2026-05-06 20:15`
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
- [ ] Propose initial module split and naming conventions.
- [ ] Move current app sources to an application module with minimal path disruption.
- [ ] Keep build/test commands working from repository root.
- [ ] Update `docs/ARCHITECTURE.md` and `docs/DECISIONS.md` with the rationale.
- [ ] Run targeted checks and `./gradlew test` after migration.

## Progress Log
- `2026-05-06 20:15`: Task created as a prerequisite for styling library work.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] `BA-017`: Set up the initial `kolo-styles` library module in the new multi-module structure.

