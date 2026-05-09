# BA-003: Complete Kotlin CSS DSL stylesheet migration

## Metadata
- ID: `BA-003`
- Status: `completed`
- Owner: `ai`
- Created: `2026-05-04 22:56`
- Updated: `2026-05-09 09:15`
- Completed: `2026-05-06 19:25`
- Related Human Issue: none

## Goal
Replace the standalone browse/art stylesheet source files with Kotlin CSS DSL generation so styling is emitted from application code while preserving the existing visual behavior.

## Scope
- In scope:
  - Add generated stylesheet endpoints for browse and art pages.
  - Encode existing browse/art stylesheet rules into Kotlin CSS DSL in `CssController`.
  - Preserve prior class contract and rendered visual behavior.
  - Update stylesheet tests to validate generated CSS structure and removal of `@import` bridging.
- Out of scope:
  - Tailwind-like co-located styling architecture.
  - Utility-style library/module design.
  - Type-safe style keyword framework and incremental utility migration.

## Plan
- [x] Read existing `src/main/resources/static/css/` to map current class names and rules.
- [x] Add generated CSS endpoints for browse and art routes.
- [x] Implement Kotlin CSS DSL mapping for all browse/art stylesheet rules.
- [x] Update tests to assert generated stylesheet integrity.
- [x] Update docs to reflect Kotlin CSS DSL based styling generation.

## Progress Log
- `2026-05-04 22:56`: Task created.
- `2026-05-06 18:16`: Added Kotlin CSS DSL dependency and Spring CSS endpoints (`/css/generated/browse.css`, `/css/generated/art.css`).
- `2026-05-06 19:25`: Implemented full browse/art rule mapping in Kotlin CSS DSL and removed `@import` bridge behavior.

## How Completed
- Files edited:
  - `src/main/kotlin/com/github/cc007/blueart/styling/CssController.kt`
  - `src/test/kotlin/com/github/cc007/blueart/styling/CssControllerTest.kt`
  - `docs/ARCHITECTURE.md`
  - `docs/TESTING.md`
- Commands run:
  - `./gradlew test --tests com.github.cc007.blueart.styling.CssControllerTest`
- Checks/tests run:
  - `CssControllerTest` passed.
- Constraints or tradeoffs:
  - Styles are centralized in `CssController`; co-located utility styling is intentionally deferred.

## Verification
- Result: Targeted stylesheet test passed and confirmed generated CSS endpoints expose Kotlin DSL output without `@import`.
- Not verified: Manual visual regression sweep across all browse/art variants.

## Follow-ups
- [ ] `BA-015`: Multi-module Gradle foundation for future styling libraries.
- [ ] `BA-016`: Tailwind-like co-located styling architecture and design.
- [ ] `BA-017`: `kolo-styles` library module setup.
- [ ] `BA-021`: CSS endpoint generation from canonical `kolo`/`version` request parameters.
- [ ] `BA-022`: `kolo {}` extension, generated classes, and stylesheet link integration.
- [ ] `BA-019`: Initial margin/padding utilities with visual parity validation.
