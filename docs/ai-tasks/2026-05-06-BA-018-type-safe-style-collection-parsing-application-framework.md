# BA-018: Type-safe style collection/parsing/application framework

## Metadata
- ID: `BA-018`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-06 20:15`
- Updated: `2026-05-06 20:15`
- Related Human Issue: none

## Goal
Implement the framework that gathers co-located style declarations, parses them into CSS output, and applies generated class tokens with a type-safe API (no fragile string-only style keywords).

## Scope
- In scope:
  - Define typed style keyword model (for example sealed hierarchies/value classes/enums) for utility expressions.
  - Implement style collection from render-time declarations.
  - Implement parser/translator from typed tokens to CSS rules and generated classes.
  - Integrate output with current stylesheet delivery mechanism.
  - Add tests for parser correctness, duplicate handling, and typo prevention guarantees.
- Out of scope:
  - Full utility catalog coverage.
  - Broad page-by-page migration.

## Plan
- [ ] Finalize API contracts from `BA-016` architecture decisions.
- [ ] Build typed token model for utility declarations.
- [ ] Implement collector and parser/generator pipeline.
- [ ] Add integration path from generated output to served CSS.
- [ ] Add unit and integration tests for type-safety and deterministic output.

## Progress Log
- `2026-05-06 20:15`: Task created to build type-safe utility infrastructure.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] `BA-019`: Add first margin/padding utilities and remove equivalent rules from `CssController`.

