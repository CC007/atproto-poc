## ADDED Requirements

### Requirement: Migration-sensitive routes have deterministic visual baselines
The system SHALL provide deterministic visual regression snapshots for the login, browse timeline, and art detail routes using the existing dummy account and dummy post fixtures.

#### Scenario: Baseline comparison for covered routes
- **WHEN** the visual regression suite runs against the application in dummy mode
- **THEN** the system captures screenshots for the covered routes and compares them to committed visual baselines

### Requirement: Visual checks run in both required headless browsers
The system SHALL execute every visual regression scenario in headless Chromium and headless Firefox with normalized rendering settings.

#### Scenario: Cross-browser headless execution
- **WHEN** a visual regression test run starts
- **THEN** each configured scenario is executed in both headless Chromium and headless Firefox

### Requirement: Visual regressions produce reviewable artifacts
The system MUST provide expected, actual, and diff screenshots for any visual assertion failure.

#### Scenario: Regression artifacts on mismatch
- **WHEN** a screenshot assertion detects a visual mismatch
- **THEN** the system emits expected, actual, and diff image artifacts for developer review

### Requirement: Visual suite remains black-box and module-isolated
The system SHALL keep Playwright visual tests in a dedicated module and MUST interact with the application only through browser navigation, user input simulation, and rendered-output assertions.

#### Scenario: Running module-scoped visual tests
- **WHEN** developers execute the visual regression module
- **THEN** test logic exercises only external browser behavior and does not depend on internal application hooks

### Requirement: Snapshot baseline updates require explicit developer approval
The system MUST require explicit developer approval in-editor before accepting snapshot baseline updates, and MUST NOT treat AI approval as valid.

#### Scenario: Baseline update request in local workflow
- **WHEN** baseline snapshots differ and an update is requested
- **THEN** the baseline is updated only after explicit developer approval in-editor
