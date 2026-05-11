# BA-021: CSS endpoint to generate `kolo.css` from request parameters

## Metadata
- ID: `BA-021`
- Status: `completed`
- Owner: `ai`
- Created: `2026-05-09 09:15`
- Updated: `2026-05-11 02:49`
- Related Human Issue: none

## Goal
Implement the server-side CSS endpoint behavior for `/css/generated/kolo.css` so it deterministically generates utility rules from canonical request parameters (`version` and `kolo`).

## Scope
- In scope:
  - Parse `kolo` query values and tolerate unsupported/malformed tokens.
  - Convert tokens into generated CSS rules via pluggable generators, while annotating unsupported/unparsed tokens as CSS comments.
  - Keep endpoint output deterministic for identical canonical input.
  - Add tests for endpoint parsing, permissive handling, and output stability.
- Out of scope:
  - HTML DSL ergonomics (`kolo { ... }`) and element class application.
  - Render-layer stylesheet link tag generation.

## Locked Decisions from BA-016 / BA-018

### Token model
- Tailwind-style token names.
- Pseudo/media variants are in scope.
- Arbitrary value tokens (`[...]`) are deferred from style generation; current endpoint behavior annotates them as unparsed comments.

### CSS delivery contract
- Single utilities endpoint: `/css/generated/kolo.css`.
- Query params: `version=<build git sha>`, `kolo=<semicolon-separated canonical token list>`.
- `;` is reserved as delimiter and must not appear inside tokens.
- Keep existing page CSS side-by-side during migration; remove only declarations already covered by migrated Kolo utilities.

### Canonicalization (performed by the caller for URL caching)
- split/trim/drop empty tokens
- reject `;` and `[...]` tokens at caller-side canonicalization
- dedupe exact tokens
- sort by `(group, variantCount, variantChain, baseUtility, token)`
- join with `;`

### Caching
- Versioned URL caching is the primary strategy.
- `ETag` support is optional.

## Plan
- [x] Define endpoint contract for tolerant token parsing and comment diagnostics.
- [x] Implement token parsing: split on `;`, trim, drop empty, and annotate malformed/unparsed tokens instead of failing the request.
- [x] Implement pluggable token-to-CSS rule generation via `StyleParserHook` + `StyleGeneratorHook`.
- [x] Leave the default hook set empty so tokens are annotated as unsupported until BA-019 provides real utility coverage.
- [x] Add focused tests for validation, error handling, and generator integration.
- [x] Verify endpoint wires with minimal diff and tests pass.

## Progress Log
- `2026-05-09 09:15`: Task created by splitting superseded `BA-018` into focused implementation slices.
- `2026-05-09 11:47`: Added initial `/css/generated/kolo.css` endpoint wiring, implemented parser/canonicalizer/generator in `:libs:kolo-styles`, and added focused compiler/controller tests for success and invalid-token cases.
- `2026-05-09 12:16`: Simplified to remove hardcoded utilities and dedupe/sort logic. Compiler now focuses on token validation and pluggable CSS generation; deduping/sorting moves to client-side (BA-022) for URL caching purposes. Actual utility implementations deferred to BA-019.
- `2026-05-09 13:03`: Applied permissive handling for unsupported/malformed tokens (Postel's law): endpoint now returns `200 text/css` and emits `kolo-unsupported` / `kolo-unparsed` comments for easier debugging.
- `2026-05-09 13:20`: Refactored compiler API to return `String` directly (no sealed result wrapper), inverted compile branching for clearer final generated-rule path, and moved app wiring to Spring bean injection via `KoloCssCompilerConfig` while keeping `:libs:kolo-styles` framework-agnostic.
- `2026-05-10 19:53`: Removed the parallel token-generator path so CSS generation now uses one mechanism only: `StyleParserHook` + `StyleGeneratorHook`. Updated compiler tests accordingly and re-verified `:libs:kolo-styles` plus targeted `:app` controller tests.
- `2026-05-10 22:38`: Moved `/css/generated/kolo.css` endpoint ownership from `:app` into `:libs:kolo-styles`, including the Spring MVC controller and compiler bean configuration. Re-verified both focused controller/compiler tests and full app context startup.
- `2026-05-11 02:49`: Final cleanup completed: removed the obsolete empty `KoloStylesWebConfiguration.kt`, kept `KoloCssCompiler` as the Spring bean (`@Service`), and aligned `:libs:kolo-styles` dependency declarations to grouped scope blocks using the repository dependency DSL helpers.

## How Completed
- Added `/css/generated/kolo.css` handling in `libs/kolo-styles/src/main/kotlin/com/github/cc007/blueart/kolostyles/web/KoloCssController.kt` with library-owned Spring bean wiring via `KoloCssCompiler` as an `@Service`.
- Kept browse/art page stylesheet endpoints in `app/src/main/kotlin/com/github/cc007/blueart/endpoints/styling/CssController.kt` while moving Kolo endpoint ownership into `:libs:kolo-styles`.
- Implemented tolerant token parsing and hook-driven CSS generation in `libs/kolo-styles/src/main/kotlin/com/github/cc007/blueart/kolostyles/compiler/KoloCssCompiler.kt`.
- Kept malformed tokens observable via `/* kolo-unparsed: ... */` comments and unsupported-but-well-formed tokens observable via `/* kolo-unsupported: ... */` comments.
- Preserved server-side token order and duplicates; caller-side canonicalization/caching concerns remain assigned to BA-022.
- Added/updated focused tests in `libs/kolo-styles/src/test/kotlin/com/github/cc007/blueart/kolostyles/compiler/KoloCssCompilerTest.kt`, `libs/kolo-styles/src/test/kotlin/com/github/cc007/blueart/kolostyles/web/KoloCssControllerTest.kt`, and `app/src/test/kotlin/com/github/cc007/blueart/endpoints/styling/CssControllerTest.kt`.

## Verification
- Ran:
  - `./gradlew :libs:kolo-styles:test :app:test --tests com.github.cc007.blueart.endpoints.styling.CssControllerTest`
  - `./gradlew :app:test --tests com.github.cc007.blueart.BlueArtApplicationTests`
  - `./gradlew :libs:kolo-styles:test --tests com.github.cc007.blueart.kolostyles.compiler.KoloCssCompilerTest --tests com.github.cc007.blueart.kolostyles.web.KoloCssControllerTest`
- Result: all listed commands passed on `2026-05-11`.
- Coverage verified:
  - token splitting/trim/drop-empty behavior
  - malformed token diagnostics
  - unsupported token diagnostics
  - hook-based generation success path
  - token-order preservation
  - controller response status/body for `/css/generated/kolo.css`
  - application context startup with the Kolo endpoint/controller/config provided by `:libs:kolo-styles`

## Follow-ups
- [ ] `BA-022`: Wire request-scoped token collection and stylesheet link generation from rendered pages.
- [ ] `BA-019`: Apply first margin/padding utilities after endpoint + DSL plumbing is in place.
