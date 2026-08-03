# Testing

## Scope
This project currently relies on Gradle test tasks and targeted checks around changed paths.

## Baseline Commands
```bash
./gradlew test
```

For module-scoped verification after the multi-module split:

```bash
./gradlew :app:test
```

Visual regression lane:

```bash
./gradlew visualTest
```

Baseline refresh (developer approval required):

```bash
./gradlew updateVisualBaselines -PvisualBaselineApprover=<developer-name>
```

## Change-Focused Guidance
- Prefer targeted tests for narrow changes.
- Run `./gradlew test` when edits are broad or touch shared code paths.
- Run `./gradlew visualTest` whenever AI diagnosis needs visual evidence for UI changes and before completing any change.
- Document any unverified areas in handoff notes when checks cannot run.

## Visual Baseline Policy
- Baseline updates require explicit in-editor developer approval.
- `-PvisualBaselineApprover` is mandatory for baseline updates.
- Approval strings that identify AI/Copilot are rejected by the visual baseline task.

## Visual Failure Triage
When `visualTest` fails, inspect expected/actual/diff artifacts under:

`visual-tests/build/reports/visual-regression/<browser>/<scenario>/`

Files:
- `expected.png` (committed baseline)
- `actual.png` (current run)
- `diff.png` (highlighted mismatch areas)

## Recent Coverage Additions
- `RichTextFacetRendererTest` validates UTF-8 byte-offset slicing plus defensive handling of malformed/overlapping link, tag, and mention facets.
- `PostSummaryTest` validates browse card rendering rules for BA-005 plus follow-record coverage (text-only card text rendering, split gallery wrappers for multi-image embeds, and `GraphFollow` activity rendering without unsupported placeholders).
- `LoginControllerTest` validates the login form exposes the `localhost` network option.
- `LoginNetworkSelectionTest` validates localhost network selection resolves to the running host and port while non-localhost selections stay on the existing path.
- `DummyLoginFlowTest` validates localhost form login, dummy browse follow-activity rendering, and dummy detail-page rendering through the live app.
- `DummyAtProtoAuthControllerTest` validates localhost dummy auth credentials and deterministic session payloads for `com.atproto.server.createSession`.
- `DummyAtProtoTimelineControllerTest` validates localhost dummy timeline paging/content shape (including deterministic cursor pagination and HTTPS direct image fixture URLs) plus bearer-token rejection for `app.bsky.feed.getTimeline`.
- `CssControllerTest` validates BA-003 step 2 stylesheet endpoints no longer use `@import` and preserve complete rule-header coverage from `static/css/browse.css` and `static/css/art.css` in generated Kotlin CSS DSL output.
- `KoloCssCompilerTest` validates BA-021 permissive token handling: preserve token order, annotate unsupported/unparsed tokens with CSS comments, and generate CSS through the `StyleParserHook` + `StyleGeneratorHook` pipeline.
- `KoloCssControllerTest` validates BA-021 `/css/generated/kolo.css` from `:libs:kolo-styles` always returns `200 text/css` and emits comment diagnostics for unsupported/unparsed tokens.
- `KoloHtmlRuntimeTest` validates BA-022 render-side plumbing: full HTML content assertions (not spot-checks) across all test cases, canonical token deduplication and variant-aware ordering, placeholder href finalization to `/css/generated/kolo.css`, per-element class scoping via the class-name mapper, multi-element overlap/deduplication, and no-op behaviour outside a kolo render context. Tests call `KoloScope.variant()` / `KoloScope.recordBase()` and `KoloVariantScope.variant()` / `KoloVariantScope.recordBase()` directly (no test-only adapter helpers).
- `DisplayUtilitiesTest` validates display utility parsing/generation coverage: full allow-list acceptance, unknown/malformed token diagnostics, pseudo/media variant rule generation, and mixed spacing+display compiler output order.
- `KoloCssControllerTest` validates mixed spacing+display `/css/generated/kolo.css` generation when both hook families are wired, alongside unsupported/unparsed diagnostics behavior.
- `CssControllerTest` validates migrated browse/art selectors no longer emit duplicate `display` declarations from generated page CSS once display ownership has moved into Kolo DSL render paths.
- `KoloStylesApiTest` validates baseline `:libs:kolo-styles` placeholder API wiring for utility definitions and parser/generator hook contracts.
- `KoloStylesModuleWiringTest` validates the `:app` module can consume `:libs:kolo-styles` types without changing runtime behavior.
- `BlueArtApplicationTests` validates the Spring application context still starts with the Kolo compiler/configuration/controller owned by `:libs:kolo-styles`.
- `SpacingUtilitiesTest` validates BA-019 spacing utilities: `SpacingParserHook` accepts/rejects tokens correctly, `SpacingGeneratorHook` produces `k-`-prefixed CSS selectors with pseudo-class and media-query variant support, and `KoloCssCompiler` with spacing hook lists generates real CSS for spacing tokens.
- `PlaywrightVisualRegressionTest` in `:visual-tests` validates login, browse timeline card rendering, and art detail rendering in headless Chromium and Firefox against committed per-browser snapshots.

## Suggested Additions
- Lightweight integration tests for `GET /browse` and `GET /art/{cid}` rendering expectations.
