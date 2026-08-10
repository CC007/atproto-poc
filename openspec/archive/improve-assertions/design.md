# Design: improve-assertions

## Context

The proposal asks for stronger test assertions because current JUnit/Kotlin assertion usage often yields weak failure messages, especially in HTML-string checks.

Current baseline (full suite audit of all `*Test.kt` files):
- 15 test files audited across `:app`, `:libs:kolo-styles`, `:visual-tests`.
- Existing assertion footprint includes heavy use of `assertTrue`/`assertEquals`, plus `assertFalse`, `assertNull`, `assertNotNull`, `assertContains`, `assertFailsWith`, and `fail`.
- HTML and CSS tests frequently assert with `contains(...)` substring checks instead of structural assertions.

Observed counts from current suite:
- `assertTrue`: 64
- `assertEquals`: 105
- `assertFalse`: 13
- `assertNull`: 20
- `assertNotNull`: 34
- `assertContains`: 8
- `assertFailsWith`: 4
- `fail`: 4

Constraints and requirement anchors:
- Keep Gradle/JUnit verification workflow aligned to `openspec/specs/testing-verification-practices/spec.md`.
- Keep production behavior unchanged; this change is test quality + assertion infrastructure.
- Remain Kotlin/JVM-native and compatible with existing JUnit Platform execution.

Stakeholders:
- `:app` controller/component test maintainers.
- `:libs:kolo-styles` compiler/runtime utility test maintainers.
- Reviewers diagnosing regressions in CSS/HTML rendering and endpoint behavior.

## Goals / Non-Goals

**Goals:**
- Select a stronger assertion framework suited to Kotlin and current suite patterns.
- Migrate assertion style across the full existing suite (all audited test files), not only newly written tests.
- Replace fragile `contains` chains with more expressive matchers and, where needed, DOM-aware assertions.
- Improve failure diagnostics while preserving existing test intent.

**Non-Goals:**
- Rewriting production code paths.
- Migrating from JUnit test execution model to a new runner for this change.
- Expanding browser visual testing scope (already handled by Playwright capability/specs).

## Decisions

### 1) Choose KoTest assertions as the primary assertion framework
**Decision:** Use KoTest assertion/matcher APIs (`kotest-assertions-core`) as the default replacement surface for current Kotlin/JUnit assertions.

**Why this is better suited here:**
- Kotlin-first API (`shouldBe`, `shouldContain`, `shouldNotBeNull`, `shouldThrow`, etc.) fits current Kotlin-heavy test code.
- Can be adopted incrementally without changing JUnit test engine (assertion module only).
- Provides richer, more readable failures for strings, collections, and object comparisons than boolean-style assertions.
- Works uniformly across `:app`, `:libs:kolo-styles`, and `:visual-tests`.

**Alternatives considered:**
- **AssertJ:** strong diagnostics, but less Kotlin-idiomatic and currently unevenly available across modules.
- **Hamcrest:** powerful matchers, but less fluent/idiomatic in Kotlin.
- **Keep `kotlin.test` only:** lowest migration cost, but does not solve low-signal assertion issues.

### 2) Keep JUnit Platform; do not migrate to KoTest spec engine now
**Decision:** Adopt KoTest matchers/assertions first, while keeping current JUnit 5 test structure/annotations.

**Rationale:** Delivers assertion improvements with minimal operational risk and no test-runner migration complexity.

**Alternatives considered:**
- **Full KoTest spec-style migration now (FunSpec/StringSpec):** higher churn and broader style migration than required for this change.

### 3) Use Jsoup for HTML structure assertions, paired with KoTest matchers
**Decision:** Introduce Jsoup-backed parsing for HTML output assertions and validate DOM structure/content via selectors plus KoTest matchers.

**Rationale:** BlueArt is server-rendered HTML (`kotlinx.html`); DOM-level assertions are more robust than raw substring checks and produce clearer failure context.

**Alternatives considered:**
- **XMLUnit/XPath first:** powerful for XML/XPath cases but heavier than needed for most current HTML tests.
- **Raw string assertions only:** continues the core pain described in proposal.

### 4) Audit-driven full-suite migration scope
**Decision:** Migrate all audited test files in this change, with per-file assertion replacement strategy.

**Rationale:** User requirement is explicit full-suite coverage; leaving legacy assertions behind would fragment style and reduce impact.

**Alternatives considered:**
- **Only touched tests:** lower effort but fails requested scope.
- **Only HTML-heavy tests:** partial value, misses many low-signal assertions in utility/compiler tests.

### 5) Centralize KoTest via convention plugin; keep Jsoup module-local
**Decision:**
- Keep `kotlin("test-junit5")` centralized in the shared `kotlin-jvm` convention plugin as the baseline JUnit 5 test dependency.
- Add `io.kotest:kotest-assertions-core` once in the shared `kotlin-jvm` convention plugin so all Kotlin test modules get a consistent assertion baseline.
- Add `org.jsoup:jsoup` only in modules that actually need HTML parsing assertions (for example `:app`, and optionally `:libs:kolo-styles` only if DOM assertions are added there).

**Rationale:** This balances consistency and minimal dependency scope: JUnit 5 baseline + KoTest assertions are standardized project-wide through one convention path, while Jsoup remains narrowly scoped to real usage.

## Risks / Trade-offs

- **[Large one-change test diff can be hard to review]** → Mitigation: migrate in explicit per-file commits/sections with unchanged test semantics.
- **[Mixed style during migration window]** → Mitigation: complete conversion of each file once touched; avoid partial per-file conversion.
- **[DOM assertions may miss exact serialization details]** → Mitigation: keep exact-string assertions only where serialization format itself is the contract.
- **[New dependency surface in test scope]** → Mitigation: restrict to assertion + HTML parser libs only; no runtime dependency impact.

## Migration Plan

1. Keep `kotlin("test-junit5")` centralized in the shared `kotlin-jvm` convention plugin as the test-engine baseline.
2. Add KoTest assertion dependency in the shared `kotlin-jvm` convention plugin for all Kotlin test modules.
3. Add Jsoup test dependency only to modules that need HTML DOM assertions.
4. Add small shared test helpers for HTML parsing + selector assertions (thin wrappers, no deep custom DSL).
5. Convert assertions across **all audited test files**:
   - `app/.../LoginControllerTest.kt`: replace HTML `contains` checks with DOM/selector assertions.
   - `app/.../PostSummaryTest.kt`: replace text/class substring checks with structural and collection-aware assertions.
   - `app/.../RichTextFacetRendererTest.kt`: migrate equality/null checks + newline/link HTML checks to clearer matcher forms; use DOM assertions for rendered HTML fragments.
   - `app/.../CssControllerTest.kt`: replace negative/positive declaration checks with matcher-based assertions and better error clues.
   - `app/.../DummyLoginFlowTest.kt`: replace status/header/body checks with fluent assertions; improve redirect and content assertions.
   - `app/.../DummyAtProtoAuthControllerTest.kt` and `DummyAtProtoTimelineControllerTest.kt`: migrate exception/status/list-content assertions, including predicate-based collection assertions.
   - `libs/.../KoloHtmlRuntimeTest.kt`: convert `contains` + equality checks to richer matcher assertions, keep exact HTML equality only where needed.
   - `libs/.../SpacingUtilitiesTest.kt`, `DisplayUtilitiesTest.kt`, `FontUtilitiesTest.kt`, `SizingUtilitiesTest.kt`: convert parser/generator/compiler assertions to KoTest equivalents while keeping exact CSS contract checks.
   - `libs/.../KoloCssCompilerTest.kt`, `KoloCssControllerTest.kt`: convert string equality and unsupported/unparsed diagnostics assertions to matcher-based style with clearer failure clues.
   - `visual-tests/.../VisualSnapshotAssertions.kt`: replace `kotlin.test.fail` usage with KoTest failure/assertion patterns while preserving artifact messages.
6. Run repository verification workflow according to `testing-verification-practices` after migration.
7. Update spec/task artifacts to reflect completed assertion migration coverage.

**Rollback strategy:**
- Revert assertion migration per file/module if needed, keeping dependency additions isolated.
- Since this is test-only, rollback has no data/schema migration impact.