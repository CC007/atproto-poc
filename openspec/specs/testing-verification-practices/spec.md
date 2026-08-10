# Testing and Verification Practices

## Provenance
- `docs/TESTING.md`

## Purpose
Define verification expectations for scoped and broad changes using existing Gradle test workflows.

## Requirements

### Requirement: Changes use existing Gradle verification workflows
The system SHALL use existing Gradle test tasks for validation, with `./gradlew test` required for broad changes.

#### Scenario: Change scope is broad or shared
- **WHEN** edits impact multiple modules or shared behavior
- **THEN** repository-level Gradle tests are executed as the verification baseline

### Requirement: Targeted checks are acceptable for scoped changes
The system SHALL permit module-scoped targeted tests when changes are narrow.

#### Scenario: Change is localized to specific module paths
- **WHEN** edits are limited to a module or narrow surface
- **THEN** targeted Gradle tests for affected modules are run

### Requirement: Visual tests run when visual evidence is needed during implementation
The system SHALL run visual regression tests whenever AI-led diagnosis or implementation decisions require visual evidence to resolve breakage or determine safe changes.

#### Scenario: Diagnosis requires UI confirmation
- **WHEN** AI-driven change work encounters a potential UI regression or uncertainty
- **THEN** visual regression tests are executed before implementation decisions are finalized

### Requirement: Visual regression suite is a completion gate
The system MUST execute the visual regression suite before any change is considered complete.

#### Scenario: Final verification before completion
- **WHEN** a change is ready for completion
- **THEN** the visual regression suite is run and must complete before the change can be marked done

### Requirement: Assertion APIs provide high-signal failure diagnostics
The system SHALL standardize Kotlin test assertions on a matcher-style assertion library that produces explicit, high-signal failure messages for equality, nullability, collection, and exception checks while remaining compatible with the existing JUnit Platform execution model.

#### Scenario: Assertion mismatch is reported
- **WHEN** a migrated test assertion fails
- **THEN** the reported failure message identifies the expected and actual outcomes with matcher-level context rather than a generic boolean assertion failure

### Requirement: HTML rendering checks use structure-aware assertions
The system MUST assert server-rendered HTML behavior through structure-aware parsing and selector-based checks when validating element presence, attributes, hierarchy, or text placement instead of relying only on raw substring checks.

#### Scenario: HTML contract is validated
- **WHEN** a test verifies rendered HTML structure
- **THEN** the test parses HTML content and asserts the required DOM structure and content using selector-aware assertions

### Requirement: Assertion migration preserves test intent across existing suites
The system SHALL migrate existing tests to the standardized assertion approach across affected modules while preserving each test's original behavioral intent and coverage scope.

#### Scenario: Existing test behavior is migrated
- **WHEN** assertion style is upgraded in an existing test file
- **THEN** the migrated test continues asserting the same behavioral contract with stronger diagnostics

#### Scenario: Audited suite migration is complete
- **WHEN** the assertion migration change is implemented
- **THEN** all audited test files in `:app`, `:libs:kolo-styles`, and `:visual-tests` use the standardized matcher-style assertion approach
