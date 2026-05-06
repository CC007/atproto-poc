# BA-016: Co-located tailwind-like styling architecture and design

## Metadata
- ID: `BA-016`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-06 20:15`
- Updated: `2026-05-06 20:15`
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

## Plan
- [ ] Review current styling flow (`CssController`, generated stylesheet endpoints, class usage in renderers).
- [ ] Document desired developer experience for co-located utility usage in Kotlin.
- [ ] Define architecture options and choose one with rationale.
- [ ] Record decisions in `docs/DECISIONS.md` (or ADR) and update `docs/ARCHITECTURE.md`.
- [ ] Produce implementation checklist for `BA-017`, `BA-018`, and `BA-019`.

## Progress Log
- `2026-05-06 20:15`: Task created to de-risk tailwind-like styling implementation.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] `BA-018`: Implement the type-safe style framework aligned with the chosen architecture.

