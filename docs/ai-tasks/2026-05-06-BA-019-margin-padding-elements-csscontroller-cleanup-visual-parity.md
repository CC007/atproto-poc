# BA-019: Add first margin/padding utilities and preserve visual parity

## Metadata
- ID: `BA-019`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-06 20:15`
- Updated: `2026-05-06 20:15`
- Related Human Issue: none

## Goal
Introduce the first co-located utility elements (padding and margin), remove equivalent rules from `CssController`, and verify that browse/art pages remain visually unchanged.

## Scope
- In scope:
  - Add initial typed utilities for padding and margin values.
  - Apply utilities in selected view components.
  - Remove duplicated margin/padding declarations from `CssController` once utilities cover those cases.
  - Add tests for generated classes/rules and ensure existing rendering tests remain valid.
  - Perform manual visual parity checks on browse and art pages.
- Out of scope:
  - Full replacement of all CSS properties.
  - New visual redesign.

## Plan
- [ ] Pick a small pilot subset of components with clear margin/padding usage.
- [ ] Implement and apply typed margin/padding utilities via the new framework.
- [ ] Remove migrated margin/padding declarations from `CssController`.
- [ ] Update and run tests for CSS output and affected rendering paths.
- [ ] Run manual before/after visual comparison for `/browse` and `/art/{cid}`.

## Progress Log
- `2026-05-06 20:15`: Task created as first implementation milestone after framework setup.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] Expand utility set after proving parity and stability with margin/padding primitives.

