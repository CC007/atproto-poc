# Design: Introduce playwright

## Context

BlueArt currently relies on functional/unit/integration tests, while visual verification of `/browse`, `/art/{cid}`, and the CSS-to-Kolo migration remains manual.  
The proposal for this change introduces automated visual regression testing in **headless Chromium and headless Firefox**, with screenshots and before/after diffs when regressions occur.

Current architecture constraints:
- Server-rendered HTML (`kotlinx.html`) with styles split across generated page CSS (`/css/generated/*.css`) and `kolo.css`.
- Existing deterministic dummy timeline/detail fixtures plus dummy account flows are available through localhost dummy endpoints and are intended to stabilize visual tests.
- Existing verification workflows are Gradle-first (`testing-verification-practices`), and this change must keep the visual test surface Kotlin/Gradle-native.

Stakeholders:
- Contributors migrating styles from page CSS to Kolo utilities.
- Developers who need reliable in-editor visual change evidence before accepting baseline updates.

## Goals / Non-Goals

**Goals:**
- Add deterministic visual regression tests for key user-visible routes affected by style migration.
- Cover both Chromium and Firefox in headless mode in one consistent suite.
- Produce actionable screenshot artifacts (expected/actual/diff) for failed assertions.
- Use a Kotlin Playwright integration (not JavaScript/TypeScript test code).
- Keep tests black-box: interact through browser behavior only, not internal app hooks.
- Place Playwright coverage in a dedicated module with clear ownership and lifecycle.
- Integrate visual checks into repository verification in an in-editor, developer-driven workflow.
- Align with proposal motivation while preserving current route/model behavior from `server-rendered-web-architecture`.

**Non-Goals:**
- Replacing existing Kotlin/JUnit test coverage.
- Full cross-device or mobile matrix in this change.
- Pixel parity across all operating systems and font stacks.
- Rewriting styling architecture or changing Kolo/page-CSS ownership boundaries beyond test support needs.
- Introducing database/cache/wiremock-backed visual test environments in this change.

## Decisions

### 1) Use Playwright Test as the visual runner
**Decision:** Adopt Playwright via a Kotlin integration in JVM tests (Gradle-managed), using Playwright browser engines for screenshot assertions and diff generation.

**Rationale:** This keeps the new capability in the repository’s primary language/toolchain while still using Playwright’s browser automation strengths.

**Alternatives considered:**
- **JavaScript/TypeScript Playwright test project:** common approach, but introduces a second primary test stack that this change explicitly avoids.
- **Selenium/WebDriver + custom screenshot tooling:** more plumbing for diffs and artifact management.

### 2) Introduce a deterministic visual-test runtime mode
**Decision:** Run visual tests against the live Spring Boot app in localhost dummy mode, using the existing dummy account and dummy post fixtures as the canonical stable data source.

**Rationale:** The dummy fixtures were created specifically to prevent moving-target regressions in visual baselines.

**Alternatives considered:**
- **Use live external feeds/data for visual baselines:** not deterministic; unsuitable for stable snapshot diffs.
- **Static HTML snapshot generation without running app:** less representative of real route/render pipeline.

### 3) Define an explicit browser+render baseline contract
**Decision:** Execute all visual tests in **headless Chromium** and **headless Firefox** with fixed viewport, locale, timezone, and reduced animation/motion settings.

**Rationale:** Reduces non-functional variance so screenshot diffs indicate meaningful UI changes.

**Alternatives considered:**
- **Chromium-only:** faster but misses Firefox-specific regressions explicitly required by proposal.
- **Large browser matrix (WebKit/mobile):** broader coverage but too costly for initial rollout.

### 4) Scope initial visual scenarios to migration-sensitive surfaces
**Decision:** Start with route-level scenarios for login (localhost mode), browse timeline cards, and art detail rendering, including media-heavy examples from dummy fixtures.

**Rationale:** These are the highest-risk surfaces for CSS↔Kolo regressions and align with known testing gaps.

**Alternatives considered:**
- **Full-route coverage from day one:** high maintenance and slower feedback before baseline stabilizes.
- **Component-only screenshots:** useful, but misses route composition issues (layout interactions, spacing, link placement).

### 5) Keep visual tests as a separate verification lane
**Decision:** Add visual test commands as a distinct workflow lane (documented and locally executable in-editor), implemented in Kotlin and executed through Gradle/JUnit lifecycle boundaries.

**Rationale:** Preserves existing verification expectations while avoiding JavaScript-specific project wiring.

**Alternatives considered:**
- **Run only inside `./gradlew test`:** may slow broad feedback loops; visual tests should remain selectively runnable.
- **Manual-only execution:** does not satisfy regression automation goals.

### 6) Isolate visual tests in a dedicated module and enforce black-box boundaries
**Decision:** Place Playwright tests in a dedicated module and treat the application under test as an external black box, interacting only through browser navigation, input, and assertions on rendered output/screenshots.

**Rationale:** Separation improves ownership and prevents coupling visual tests to internal implementation details, preserving test value through refactors.

**Alternatives considered:**
- **Keep Playwright tests inside `:app` test sources:** simpler initial wiring, but weaker boundary clarity and higher risk of white-box coupling.
- **White-box test hooks for easier setup:** faster test authoring, but less realistic and less resilient.

### 7) Require explicit developer approval for snapshot baseline changes
**Decision:** Baseline snapshot updates are not routine test updates; they require explicit developer acknowledgement/approval in-editor during local test execution, and this approval must come from the developer (not the AI).

**Rationale:** This prevents accidental acceptance of regressions hidden inside bulk snapshot refreshes.

**Alternatives considered:**
- **Auto-accept baseline updates when tests pass:** fastest path but high regression risk.
- **No formal policy:** inconsistent approval behavior and lower signal quality.

### 8) Define execution policy for AI-driven development flow
**Decision:** Visual tests are run whenever AI diagnosis requires visual evidence, and they are mandatory before marking a change complete.

**Rationale:** This captures both exploratory debugging use and completion-gate regression protection.

**Alternatives considered:**
- **Run only at final verification stage:** misses early guidance during change development.
- **Run only on styling-labeled changes:** risks missing regressions from non-obvious UI side effects.

## Risks / Trade-offs

- **[Baseline churn from intentional UI updates]** → Mitigation: require explicit in-editor developer approval before snapshot baseline acceptance.
- **[Cross-browser rendering differences create noisy diffs]** → Mitigation: per-browser baselines and strict runtime normalization (viewport/locale/timezone/animations).
- **[Flaky results from asynchronous rendering or asset timing]** → Mitigation: deterministic dummy data, local fixture assets in visual mode, stable wait conditions before capture.
- **[Longer local test runtime]** → Mitigation: keep suite scoped to critical routes first; expand only after runtime is measured.
- **[Kotlin Playwright integration API differences vs JS examples]** → Mitigation: codify repository-specific test helpers/utilities and avoid copy-paste from JS-centric snippets.
- **[Black-box setup may need more environment plumbing over time]** → Mitigation: defer DB/cache/wiremock expansions as explicit future changes; keep this change focused on fixture-backed UI stability.

## Migration Plan

1. Create a dedicated Playwright module in the Gradle build.
2. Add Playwright JVM dependencies and Kotlin test utilities for browser launch, navigation, and screenshot assertion.
3. Wire module execution to run against the app as a black-box target in localhost dummy mode.
4. Implement initial visual scenarios for login, browse, and art detail in headless Chromium and headless Firefox using dummy account/posts.
5. Generate and commit initial per-browser snapshot baselines.
6. Integrate local in-editor execution with clear screenshot/diff output and explicit developer-gated snapshot update policy.
   - Approval should be available directly during test run output/workflow (easy in-editor developer confirmation, never AI-confirmed).
7. Update testing documentation with:
   - when to run visual tests during AI diagnosis,
   - mandatory completion-gate visual run,
   - baseline update approval policy,
   - failure triage flow.

**Rollback strategy:**
- Disable the visual lane in local verification flow and keep existing Gradle verification unchanged.
- Revert Playwright Kotlin test additions if needed; no data/schema migration rollback is required.
