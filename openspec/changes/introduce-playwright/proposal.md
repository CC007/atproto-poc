## Why

Visual testing is manual up until now. It would be nice to automate this

## What Changes

There will be playwright tests that check if any visual change is properly implemented and if any migration from css to kolo doesn't cause any visual regressions. 
Tests will run in **headless mode** for both **Chromium** and **Firefox** browsers.  
The implementation will use a **Kotlin Playwright integration** in the existing Kotlin/Gradle ecosystem, rather than JavaScript/TypeScript-based Playwright tests.  
The Playwright tests will live in a **dedicated module** and treat the application as a **black box**, interacting only through simulated browser interactions.  
The suite will use the existing **dummy account and dummy posts/fixtures** specifically to keep visual snapshots stable and deterministic.  
The suite will provide screenshots for before-after comparison if something changed that shouldn't have.  
Snapshot baseline updates require **explicit developer approval in-editor** during the local test flow (approval must be given by the developer, not by the AI).

## Verification Expectations

- Visual tests should run whenever the AI determines they are needed to diagnose breakage or decide what to change.
- Visual tests must always run before a change is considered completed, to guard against regressions.

## Capabilities

### New Capabilities
<!-- Capabilities being introduced. Use kebab-case identifiers (e.g., user-auth, data-export). Each creates specs/<name>/spec.md -->

### Modified Capabilities
<!-- Existing capabilities whose REQUIREMENTS are changing. Use existing spec names from openspec/specs/. -->

## Impact

<!-- Affected code, APIs, dependencies, systems -->
