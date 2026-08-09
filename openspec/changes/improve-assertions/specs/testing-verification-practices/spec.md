## ADDED Requirements

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
