# BA-003: Explore CSS-in-Kotlin approach (ktor CSS DSL / Kotlinwind)

## Metadata
- ID: `BA-003`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-04 22:56`
- Updated: `2026-05-04 22:56`
- Related Human Issue: none

## Goal
Evaluate using the **ktor CSS DSL** (or an equivalent utility-class library such as Kotlinwind) to define styles inline or co-located with component rendering code, replacing or supplementing the current standalone CSS stylesheet files under `src/main/resources/static/css/`.

## Scope
- In scope:
  - Research: ktor CSS DSL (`io.ktor:ktor-html-builder` CSS extension), Kotlinwind, and any comparable Kotlin-first styling options.
  - Prototype: apply one or two existing component styles (e.g. `.post-card`, `.post-author`) inline via the chosen DSL.
  - Assess: co-location trade-offs, bundle size, caching, and maintainability vs. current static CSS approach.
  - Decision record: document the approach chosen and rationale in `docs/DECISIONS.md`.
- Out of scope:
  - Full migration of all styles in one pass.
  - Introducing a JavaScript build step for CSS.

## Plan
- [ ] Read existing `src/main/resources/static/css/` to map current class names and rules.
- [ ] Research ktor CSS DSL availability and Spring Boot integration path.
- [ ] Research Kotlinwind API and project maturity.
- [ ] Build a minimal prototype for one component (suggested: `PostSummary.kt` card wrapper).
- [ ] Evaluate the DX, output quality, and compatibility with `kotlinx.html`.
- [ ] Write up findings and add a decision record.
- [ ] Update `docs/ARCHITECTURE.md` if the styling approach changes.

## Progress Log
- `2026-05-04`: Task created.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] If approach is adopted, plan full migration as a follow-up BA task.
