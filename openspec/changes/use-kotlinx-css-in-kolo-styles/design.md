# Design: Use kotlinx.css in kolo-styles

## Context

`CssController` (in `app`) already uses `kotlinx.css.CssBuilder` with full DSL type-safety for its static stylesheets. The `kolo-styles` library — which generates dynamic utility CSS at runtime — builds all CSS output via manual string templates (e.g., `"margin: $spacingValue;"`, `"margin-left: $spacingValue; margin-right: $spacingValue;"`).

The two modules use a different approach to CSS construction. The string-template approach in kolo-styles lacks type-checking, autocomplete, and is easy to corrupt (e.g., missing semicolons, unit errors). `kotlin-css-jvm` is already a declared dependency in the `app` module but is absent from `kolo-styles/build.gradle.kts`.

Affected components:
- `libs/kolo-styles/build.gradle.kts` — missing `kotlin-css-jvm` dependency
- `libs/kolo-styles/.../utility/SpacingUtilities.kt` — string-template CSS generation
- `libs/kolo-styles/.../generator/StyleGeneratorHook.kt` — `String?`-returning interface
- `libs/kolo-styles/.../compiler/KoloCssCompiler.kt` — joins raw CSS strings from hooks

## Goals / Non-Goals

**Goals:**
- Replace all manual string-template CSS construction in kolo-styles with `kotlinx.css.CssBuilder`
- Add `kotlin-css-jvm` as an explicit dependency of `kolo-styles`
- Preserve the existing CSS output format (selectors, media queries, rule structure) so no CSS behavioural changes reach the browser

**Non-Goals:**
- Migrating `CssController` in `app` (it already uses CssBuilder)
- Changing the kolo token format or URL schema
- Introducing new utility types beyond spacing

## Decisions

### 1. Where to adopt CssBuilder in the pipeline

The pipeline has three stages: parse → `StyleUtilityDefinition` → generate → `String`.

**Option A — Internal-only**: Keep `StyleGeneratorHook.generate(): String?` and `StyleUtilityDefinition.cssDeclaration: String`. Change `buildSpacingDeclaration` to use CssBuilder internally and serialize to a String. The public interfaces are untouched.

**Option B — Shared CssBuilder**: Change `StyleGeneratorHook` to emit CSS into a shared `CssBuilder` passed by the compiler, rather than returning a `String?`. `KoloCssCompiler.compile()` owns a single `CssBuilder`, passes it to each generator hook, and returns `.toString()` at the end.

**Decision: Option B.** A single `CssBuilder` accumulating all rules is the idiomatic pattern — it mirrors how `CssController` builds static stylesheets. Hooks write rules directly into the builder using the typed DSL (`selector { margin = 1.rem }`), eliminating all string serialisation within the generation phase. The compiler produces the final CSS string once, at the end.

The `StyleGeneratorHook` signature changes from `(StyleUtilityDefinition) -> String?` to `(StyleUtilityDefinition, CssBuilder) -> Boolean`, where the return value indicates whether the hook handled the token. Unsupported-token comments are still appended by the compiler as `raw()` calls on the same builder.

Existing `KoloCssCompilerTest` tests that construct hooks inline will need to be updated to the new signature, but the test intent is preserved.

### 2. Declaration building inside generator hooks

With a shared `CssBuilder`, each generator hook receives the builder and writes directly into it using the DSL. `buildSpacingDeclaration` is refactored to a private `CssBuilder.() -> Unit` extension that applies typed properties (e.g., `margin = 0.px`, `padding = 1.rem`). Media-query and selector wrapping is done by calling `media(...)` and the selector block on the same shared builder.

`StyleUtilityDefinition.cssDeclaration: String` becomes vestigial on the generator side — generators read the token to decide what to emit, not the stored string. The field is retained on `StyleUtilityDefinition` for now because `SpacingParserHook` tests assert its value; a follow-on cleanup can remove it once the pipeline is fully CssBuilder-native.

### 3. Dependency version

`kotlin-css-jvm` version `2025.7.14` is already pinned in `app/build.gradle.kts`. The same version must be used in `kolo-styles` to avoid classpath conflicts at runtime (both modules are on the same classpath when the Spring app boots).

**Decision:** Declare the same version string in `kolo-styles/build.gradle.kts` as in `app/build.gradle.kts`. Extract to a version catalogue entry (`libs.versions.toml` or `buildSrc`) if the project already centralises versions there; otherwise duplicate and leave a comment referencing `app/build.gradle.kts`.

## Risks / Trade-offs

**[Risk] CssBuilder serialises values differently from string templates** → Mitigation: Run the full `SpacingUtilitiesTest` suite after migration; each test asserts exact CSS output. If CssBuilder emits `0px` where the test expects `0`, update tests to match the canonical CssBuilder output (preferred) or wrap zero-values in a special case.

**[Risk] CssBuilder adds unwanted whitespace or newlines** → Mitigation: Inspect `CssBuilder.toString()` output in a unit test for a representative rule; adjust formatting expectations before changing all tests in bulk.

**[Risk] kolo-styles classpath now pulls in `kotlin-css-jvm`; downstream consumers that previously did not have that transitive dependency will pick it up** → Mitigation: `kolo-styles` is only consumed by the `app` module today, which already declares `kotlin-css-jvm` directly. No transitive risk in the current graph.

**[Trade-off] `cssDeclaration: String` in `StyleUtilityDefinition` becomes vestigial** — generators write directly into the CssBuilder from the token, not from the stored string. The field is kept for parser-test compatibility. A follow-on change can remove it and make the pipeline fully CssBuilder-native.

**[Trade-off] `StyleGeneratorHook` signature change breaks existing hook implementations** — any hook constructed inline in tests must be updated to the `(StyleUtilityDefinition, CssBuilder) -> Boolean` signature. The change is mechanical but touches all `KoloCssCompilerTest` test cases that supply a custom hook.

## Migration Plan

1. Add `+"org.jetbrains.kotlin-wrappers:kotlin-css-jvm:2025.7.14"` to `kolo-styles/build.gradle.kts` under `implementation`.
2. Change `StyleGeneratorHook` from `fun interface (StyleUtilityDefinition) -> String?` to `fun interface (StyleUtilityDefinition, CssBuilder) -> Boolean`.
3. Update `KoloCssCompiler.compile()` to create a single `CssBuilder`, pass it to each generator hook, and return `builder.toString()`. Unsupported/unparsed token comments are appended via `builder.raw(...)`.
4. Refactor `buildSpacingDeclaration(utility, value)` to a private `CssBuilder.() -> Unit` extension that sets typed properties.
5. Refactor `SpacingGeneratorHook.generate()` to write rules directly into the passed `CssBuilder` (selector block + media query wrapping) using the typed lambda from step 4.
6. Update all `KoloCssCompilerTest` tests that supply inline hook lambdas to the new `(StyleUtilityDefinition, CssBuilder) -> Boolean` signature.
7. Run `./gradlew :libs:kolo-styles:test` and fix any output-format mismatches (e.g. `0px` vs `0`).
8. Run `./gradlew :app:test` and visual tests to confirm no regression.

**Rollback:** Revert the `kolo-styles/build.gradle.kts` dependency addition and the `SpacingUtilities.kt` changes. No database, API contract, or CSS output changes are expected, so rollback is a pure code revert.
