# BA-016: Co-located tailwind-like styling architecture and design

## Metadata
- ID: `BA-016`
- Status: `in_progress`
- Owner: `ai`
- Created: `2026-05-06 20:15`
- Updated: `2026-05-09 00:00`
- Related Human Issue: none

## Goal
Design the architecture for a Kotlin-first, tailwind-like, co-located styling approach that balances utility-first speed with maintainability in server-rendered `kotlinx.html` views.

## Scope
- In scope:
  - Produce an architecture proposal for co-located styling in BlueArt.
  - Evaluate tradeoffs vs centralized stylesheets, using https://digitalbiztalk.com/article/tailwind-css-vs-inline-styles-the-full-circle-debate as inspiration input.
  - Define boundaries between app rendering, style declarations, style compilation, and runtime delivery.
  - Identify required decisions and risks before implementation tasks (`BA-017` to `BA-019`).
- Out of scope:
  - Shipping production style utilities.
  - Large-scale style migration.

## Architecture Decisions
- Developer API shape:
  - Styles are declared directly on elements with `kolo { ... }`.
  - Zero-arg utilities use bare properties (`flex`), parameterized utilities use functions (`mt(2)`, `px(4)`).
  - API supports both concise single-line and multi-line grouped usage.
- Token and naming model:
  - Use Tailwind-style utility token names.
  - Include pseudo-class and media-query variants.
  - Do not support arbitrary value tokens (`[...]`) yet.
- Runtime delivery model:
  - Serve utilities from one endpoint: `/css/generated/kolo.css`.
  - Use query params `version` (build-derived git SHA) and `kolo` (single semicolon-separated token list).
  - `kolo` list is canonicalized with variant-aware sorting and deduplication.
  - Disallow separators inside tokens for now (`;` reserved as list delimiter).
- Caching strategy:
  - Cache by versioned URL (`version` + canonical `kolo`) with immutable-friendly behavior.
  - `ETag` is optional and not required for the initial design.

## Plan
- [x] Review current styling flow (`CssController`, generated stylesheet endpoints, class usage in renderers).
- [x] Document desired developer experience for co-located utility usage in Kotlin.
- [x] Define architecture options and choose one with rationale.
- [ ] Record decisions in `docs/DECISIONS.md` (or ADR) and update `docs/ARCHITECTURE.md`.
- [ ] Produce implementation checklist for `BA-017`, `BA-018`, and `BA-019`.

## Progress Log
- `2026-05-06 20:15`: Task created to de-risk tailwind-like styling implementation.
- `2026-05-09 00:00`: Locked API and delivery decisions with user: `kolo {}` element DSL, Tailwind-style tokens, single `/css/generated/kolo.css` endpoint, and `version` + semicolon-separated `kolo` URL contract.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] `BA-018`: Implement the type-safe style framework aligned with the chosen architecture.

