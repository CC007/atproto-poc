# BA-019: Add first margin/padding utilities and preserve visual parity

## Metadata
- ID: `BA-019`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-06 20:15`
- Updated: `2026-05-09 00:00`
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

## Implementation Constraints
- Utilities and variants follow Tailwind-style token naming.
- The runtime utility stylesheet is served via `/css/generated/kolo.css` using:
  - `version=<build git sha>`
  - `kolo=<semicolon-separated canonical token list>`
- Canonical token list must be deduplicated and variant-aware sorted.
- Arbitrary value tokens (`[...]`) are not included in this milestone.
- Primary cache strategy is versioned URL caching.

## Plan
- [ ] Pick a small pilot subset of components with clear margin/padding usage.
- [ ] Implement and apply typed margin/padding utilities via the new framework and `kolo { ... }` DSL.
- [ ] Ensure migrated markup emits deterministic token lists for `kolo.css` generation.
- [ ] Remove migrated margin/padding declarations from `CssController` once parity is confirmed.
- [ ] Update and run tests for CSS output and affected rendering paths.
- [ ] Run manual before/after visual comparison for `/browse` and `/art/{cid}`.

## Progress Log
- `2026-05-06 20:15`: Task created as first implementation milestone after framework setup.
- `2026-05-09 00:00`: Synced migration constraints from BA-016/BA-018 decisions (token format, endpoint contract, and cache/versioning strategy).

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] Expand utility set after proving parity and stability with margin/padding primitives.

