# Tasks: Introduce playwright

## 1. Visual Test Module Setup

- [x] 1.1 Add a dedicated Gradle module for Playwright visual regression tests
- [x] 1.2 Configure module dependencies for Kotlin/JUnit Playwright execution in JVM tests
- [x] 1.3 Select and wire library-first tooling (Playwright/JUnit/assertion libs) before adding custom utilities
- [x] 1.4 Wire the new module into root Gradle settings and verification task grouping

## 2. Deterministic Runtime and Browser Contract

- [x] 2.1 Implement shared visual test config for fixed viewport, locale, timezone, and reduced motion
- [x] 2.2 Configure headless Chromium and headless Firefox execution in one reusable test lane
- [x] 2.3 Configure localhost dummy-mode app startup/targeting so visual tests run as black-box browser tests

## 3. Visual Assertion and Artifact Infrastructure

- [x] 3.1 Implement Kotlin test helpers only for missing gaps, reusing Playwright navigation/wait/screenshot APIs by default
- [x] 3.2 Implement screenshot assertions using library-provided comparison/diff artifacts (expected/actual/diff) where available
- [x] 3.3 Define per-browser snapshot storage layout and naming conventions compatible with chosen assertion libraries

## 4. Initial Scenario Coverage

- [x] 4.1 Add visual scenario for dummy-account login route rendering
- [x] 4.2 Add visual scenario for browse timeline card rendering with deterministic dummy fixtures
- [x] 4.3 Add visual scenario for art detail rendering including media-heavy dummy examples
- [x] 4.4 Ensure every scenario runs in both configured browsers and asserts against per-browser baselines

## 5. Baseline and Approval Workflow

- [x] 5.1 Generate initial Chromium and Firefox baseline snapshots for covered routes
- [x] 5.2 Commit initial baseline artifacts in the defined snapshot layout
- [x] 5.3 Implement explicit developer-approved baseline update workflow and block AI-only approval paths

## 6. Verification Lane and Documentation

- [x] 6.1 Add dedicated Gradle commands/tasks for running visual tests separately from default unit/integration lanes
- [x] 6.2 Update testing docs with when to run visual tests during AI diagnosis and as completion gate
- [x] 6.3 Document baseline update policy and failure triage flow for expected/actual/diff artifacts
