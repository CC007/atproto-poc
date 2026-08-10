# Tasks: improve assertions

## 1. Test Dependency Baseline

- [x] 1.1 Locate the shared `kotlin-jvm` convention plugin test dependency block and confirm current JUnit 5 wiring remains unchanged.
- [x] 1.2 Add `io.kotest:kotest-assertions-core` to the shared `kotlin-jvm` convention plugin so all Kotlin test modules receive KoTest matchers.
- [x] 1.3 Add `org.jsoup:jsoup` as a test dependency only in modules that need DOM assertions (starting with `:app`, extend to `:libs:kolo-styles` only if required).

## 2. Shared Assertion Helpers

- [x] 2.1 Add thin shared test helpers for HTML parsing and selector lookup to standardize Jsoup usage in tests.
- [x] 2.2 Add helper-level assertion utilities that pair Jsoup results with KoTest matchers for readable failure messages.
- [x] 2.3 Update imports/usages so migrated tests consistently use helper + KoTest patterns instead of ad hoc string checks.

## 3. App Test Migration

- [x] 3.1 Migrate `LoginControllerTest.kt` HTML substring assertions to DOM/selector assertions with KoTest matchers.
- [x] 3.2 Migrate `PostSummaryTest.kt` substring/class checks to structural assertions and collection-aware KoTest matchers.
- [x] 3.3 Migrate `RichTextFacetRendererTest.kt` null/equality/newline/link checks to KoTest, using DOM assertions for rendered fragments where appropriate.
- [x] 3.4 Migrate `CssControllerTest.kt` positive/negative CSS declaration assertions to clearer KoTest matcher forms.
- [x] 3.5 Migrate `DummyLoginFlowTest.kt` status/header/body and redirect assertions to KoTest matchers.
- [x] 3.6 Migrate `DummyAtProtoAuthControllerTest.kt` exception/status/list assertions to KoTest matcher patterns.
- [x] 3.7 Migrate `DummyAtProtoTimelineControllerTest.kt` list-content and predicate checks to KoTest matcher patterns.

## 4. Library Test Migration

- [x] 4.1 Migrate `KoloHtmlRuntimeTest.kt` to KoTest matchers, preserving exact HTML equality only where serialization is the contract.
- [x] 4.2 Migrate `SpacingUtilitiesTest.kt` assertions to KoTest while preserving exact CSS contract checks.
- [x] 4.3 Migrate `DisplayUtilitiesTest.kt` assertions to KoTest while preserving exact CSS contract checks.
- [x] 4.4 Migrate `FontUtilitiesTest.kt` assertions to KoTest while preserving exact CSS contract checks.
- [x] 4.5 Migrate `SizingUtilitiesTest.kt` assertions to KoTest while preserving exact CSS contract checks.
- [x] 4.6 Migrate `KoloCssCompilerTest.kt` diagnostics and output assertions to clearer KoTest matcher style.
- [x] 4.7 Migrate `KoloCssControllerTest.kt` unsupported/unparsed diagnostics assertions to KoTest matcher style.

## 5. Visual Test Assertion Migration

- [x] 5.1 Migrate `VisualSnapshotAssertions.kt` from `kotlin.test.fail` to KoTest failure/assertion patterns without losing artifact/context messaging.
- [x] 5.2 Confirm migrated visual assertion helpers still emit actionable failure details for snapshot mismatch triage.

## 6. Verification and Spec/Docs Alignment

- [x] 6.1 Run the repository testing verification workflow required by `testing-verification-practices` after migration.
- [x] 6.2 Update change task/spec artifacts to mark full audited-suite migration coverage.
- [x] 6.3 Update `docs/TESTING.md` with the new assertion conventions (KoTest-first, Jsoup for HTML structure checks, and when exact-string assertions remain valid).
