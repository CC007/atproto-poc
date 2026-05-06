# BA-003: Explore CSS-in-Kotlin approach (ktor CSS DSL / Kotlinwind)

## Metadata
- ID: `BA-003`
- Status: `partial`
- Owner: `ai`
- Created: `2026-05-04 22:56`
- Updated: `2026-05-06 19:25`
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
- [x] Read existing `src/main/resources/static/css/` to map current class names and rules.
- [x] Research ktor CSS DSL availability and Spring Boot integration path.
- [ ] Research Kotlinwind API and project maturity.
- [x] Build a minimal prototype for one component (suggested: `PostSummary.kt` card wrapper).
- [ ] Evaluate the DX, output quality, and compatibility with `kotlinx.html`.
- [ ] Write up findings and add a decision record.
- [x] Update `docs/ARCHITECTURE.md` if the styling approach changes.

## Progress Log
- `2026-05-04 22:56`: Task created.
- `2026-05-06 18:16`: Implemented BA-003 part 1: added Kotlin CSS DSL dependency and Spring CSS endpoints (`/css/generated/browse.css`, `/css/generated/art.css`) that keep styles external by importing existing static stylesheets.
- `2026-05-06 19:25`: Implemented BA-003 step 2: encoded all existing `browse.css` and `art.css` rules in Kotlin CSS DSL inside `CssController`; removed `@import` bridge behavior.

## How Completed
- Partial milestones delivered (parts 1 + 2):
  - Added CSS DSL dependency: `org.jetbrains.kotlin-wrappers:kotlin-css-jvm:2025.7.14`.
  - Added `CssController` endpoints at `/css/generated/browse.css` and `/css/generated/art.css`.
  - Implemented full rule mapping for `browse.css` and `art.css` into Kotlin CSS DSL output in `CssController`.
  - Removed generated stylesheet `@import` bridge usage.
  - Updated `CssControllerTest` to validate generated CSS contains all legacy rule headers and no `@import`.

## Verification
- `./gradlew test --tests com.github.cc007.blueart.styling.CssControllerTest` passed.
- Not verified yet: Kotlinwind evaluation and final BA-003 recommendation write-up.

## Follow-ups
- [ ] If approach is adopted, plan full migration as a follow-up BA task.
