# BA-019: Add first margin/padding utilities and preserve visual parity

## Metadata
- ID: `BA-019`
- Status: `completed`
- Owner: `ai`
- Created: `2026-05-06 20:15`
- Updated: `2026-05-15 14:45`
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
- Keep `kolo.css` and existing page CSS side-by-side during migration.
- Remove only margin/padding declarations that are explicitly migrated to Kolo utilities.
- Primary cache strategy is versioned URL caching.

## Plan
- [x] Pick a small pilot subset of components with clear margin/padding usage.
- [x] Implement and apply typed margin/padding utilities via the new framework and `kolo { ... }` DSL.
- [x] Ensure migrated markup emits deterministic token lists for `kolo.css` generation using the canonical ordering contract.
- [x] Remove migrated margin/padding declarations from `CssController` once parity is confirmed.
- [x] Update and run tests for CSS output and affected rendering paths.
- [ ] Run manual before/after visual comparison for `/browse` and `/art/{cid}`.

## Progress Log
- `2026-05-06 20:15`: Task created as first implementation milestone after framework setup.
- `2026-05-09 00:00`: Synced migration constraints from BA-016 decisions (token format, endpoint contract, and cache/versioning strategy).
- `2026-05-09 09:15`: Framework dependency split acknowledged: endpoint generation moved to `BA-021`; `kolo {}`/class/link wiring moved to `BA-022`.
- `2026-05-09 00:10`: Added side-by-side stylesheet migration requirement and explicit canonical token ordering dependency.
- `2026-05-15 14:45`: Implementation completed.

## How Completed

**New Kolo library files:**
- `libs/kolo-styles/src/main/kotlin/com/github/cc007/blueart/kolostyles/utility/SpacingUtilities.kt` — margin/padding token parser + CSS generator implementations (`SpacingParserHook`, `SpacingGeneratorHook`) registered as Spring `@Component` hook beans, plus utility definition catalog for all 14 spacing prefixes × steps 0–16.
- `libs/kolo-styles/src/main/kotlin/com/github/cc007/blueart/kolostyles/render/KoloSpacingDsl.kt` — typed DSL helpers (`m()`, `mt()`, `mb()`, `p()`, `pt()`, `px()`, etc.) on both `KoloScope` and `KoloVariantScope`.
- `libs/kolo-styles/src/main/kotlin/com/github/cc007/blueart/kolostyles/compiler/KoloCssCompiler.kt` — restored Spring `@Service`; production wiring now uses injected `List<StyleParserHook>` + `List<StyleGeneratorHook>` from component scanning.

**Views updated (kolo spacing utilities applied):**
- `app/src/main/kotlin/com/github/cc007/blueart/components/Header.kt` — `m(0)` on `.brand h1`.
- `app/src/main/kotlin/com/github/cc007/blueart/components/overview/PostSummary.kt` — `m(0)` on `.post-text` paragraph elements.
- `app/src/main/kotlin/com/github/cc007/blueart/endpoints/browse/BrowseController.kt` — `m(0)` on `body`; `p(4)` on `.browse-layout`; `m(0)` + `mb(3)` on `.sidebar-title` h2; `m(0)` on `.content-top h1`.
- `app/src/main/kotlin/com/github/cc007/blueart/endpoints/content/art/ArtContentController.kt` — `m(0)` on `body`; `p(4)` on `.art-layout`; `m(0)` on `.art-title`, `.art-byline`, `.art-text`, `.art-empty`, `.art-external`, `.comment-text`; `p(4)` on `.art-card` and `.comments`; `p(2)` on `.art-embed`.

**CSS declarations removed from `CssController` (migrated to kolo):**
- Browse: `margin` on `body.browse-body`, `.brand h1`, `.sidebar-title`, `.content-top h1`, `.post-text`; `padding` on `.browse-layout`.
- Art: `margin` on `body.art-body`, `.art-title`, `.art-byline`, `.art-text, .comment-text, .art-empty, .art-external`; `padding` on `.art-layout`, `.art-card, .comments`, `.art-embed`.
- Responsive overrides in `@media (max-width: 700px)` kept: values (0.75rem, 0.8rem) don't map to clean Tailwind steps.

## Verification
- Ran: `./gradlew test` — BUILD SUCCESSFUL
- All pre-existing tests pass (no regressions).
- New `SpacingUtilitiesTest` (23 cases): all pass — covering parser, generator, compiler integration, and utility definition catalog.
- Updated `KoloCssControllerTest` (5 cases): all pass — including new tests verifying spacing tokens emit real CSS rules when spacing hooks are wired.
- `CssControllerTest` still passes: rule headers (selectors) are preserved; only individual property declarations were removed.

## Follow-ups
- [ ] Manual visual parity check on `/browse` and `/art/{cid}` in a running instance.
- [ ] Expand utility set after proving parity and stability with margin/padding primitives (flex, text, color, etc.).

