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

## Change-Focused Guidance
- Prefer targeted tests for narrow changes.
- Run `./gradlew test` when edits are broad or touch shared code paths.
- Document any unverified areas in handoff notes when checks cannot run.

## Current Gaps
- No dedicated automated tests currently cover:
  - browse cards containing `Open artwork` links
  - `/art/{cid}` rendering media, description, and comments

## Recent Coverage Additions
- `RichTextFacetRendererTest` validates UTF-8 byte-offset slicing plus defensive handling of malformed/overlapping link, tag, and mention facets.
- `PostSummaryTest` validates browse card rendering rules for BA-005 (text-only card text rendering, text suppression when embeds exist, and split gallery wrappers for multi-image embeds).
- `DummyAtProtoAuthControllerTest` validates localhost dummy auth credentials and deterministic session payloads for `com.atproto.server.createSession`.
- `DummyAtProtoTimelineControllerTest` validates localhost dummy timeline paging/content shape plus bearer-token rejection for `app.bsky.feed.getTimeline`.
- `CssControllerTest` validates BA-003 step 2 stylesheet endpoints no longer use `@import` and preserve complete rule-header coverage from `static/css/browse.css` and `static/css/art.css` in generated Kotlin CSS DSL output.
- `KoloCssCompilerTest` validates BA-021 permissive token handling: preserve token order, annotate unsupported/unparsed tokens with CSS comments, and generate CSS through the `StyleParserHook` + `StyleGeneratorHook` pipeline.
- `KoloCssControllerTest` validates BA-021 `/css/generated/kolo.css` from `:libs:kolo-styles` always returns `200 text/css` and emits comment diagnostics for unsupported/unparsed tokens.
- `KoloHtmlRuntimeTest` validates BA-022 render-side plumbing: full HTML content assertions (not spot-checks) across all test cases, canonical token deduplication and variant-aware ordering, placeholder href finalization to `/css/generated/kolo.css`, per-element class scoping via the class-name mapper, multi-element overlap/deduplication, and no-op behaviour outside a kolo render context. Tests call `KoloScope.variant()` / `KoloScope.recordBase()` and `KoloVariantScope.variant()` / `KoloVariantScope.recordBase()` directly (no test-only adapter helpers).
- `KoloStylesApiTest` validates baseline `:libs:kolo-styles` placeholder API wiring for utility definitions and parser/generator hook contracts.
- `KoloStylesModuleWiringTest` validates the `:app` module can consume `:libs:kolo-styles` types without changing runtime behavior.
- `BlueArtApplicationTests` validates the Spring application context still starts with the Kolo compiler/configuration/controller owned by `:libs:kolo-styles`.
- `SpacingUtilitiesTest` validates BA-019 spacing utilities: `SpacingParserHook` accepts/rejects tokens correctly, `SpacingGeneratorHook` produces `k-`-prefixed CSS selectors with pseudo-class and media-query variant support, `KoloCssCompiler` with spacing hook lists generates real CSS for spacing tokens, and `spacingUtilityDefinitions` has correct catalog size and `cssDeclaration` content (property declarations only, not full rules).

## Suggested Additions
- Lightweight integration tests for `GET /browse` and `GET /art/{cid}` rendering expectations.
