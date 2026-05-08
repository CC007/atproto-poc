# BA-018: Type-safe style collection/parsing/application framework

## Metadata
- ID: `BA-018`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-06 20:15`
- Updated: `2026-05-09 00:00`
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

## Locked Decisions from BA-016
- API shape:
  - Element-attached DSL via `kolo { ... }`.
  - Zero-arg utilities as bare properties; parameterized utilities as functions.
- Token model:
  - Tailwind-style token names.
  - Pseudo/media variants are in scope.
  - Arbitrary value tokens (`[...]`) are deferred.
- CSS delivery:
  - Single utilities endpoint: `/css/generated/kolo.css`.
  - Query params: `version=<build git sha>`, `kolo=<semicolon-separated token list>`.
  - Canonicalization must deduplicate and variant-sort the token list before request generation.
  - `;` is reserved as delimiter and must not appear inside tokens.
- Caching:
  - Versioned URL caching is the primary strategy; `ETag` support is optional.

## Plan
- [ ] Finalize API contracts from `BA-016` architecture decisions.
- [ ] Build typed token model for utility declarations aligned to Tailwind naming/variant structure.
- [ ] Implement collector and parser/generator pipeline with deterministic canonicalization (dedupe + variant-aware sort).
- [ ] Add integration path that emits `/css/generated/kolo.css?version=...&kolo=...` links.
- [ ] Add unit and integration tests for type-safety and deterministic output.

## Progress Log
- `2026-05-06 20:15`: Task created to build type-safe utility infrastructure.
- `2026-05-09 00:00`: Synced implementation constraints from BA-016 decisions (DSL shape, token contract, endpoint, and cache model).

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] `BA-019`: Add first margin/padding utilities and remove equivalent rules from `CssController`.

