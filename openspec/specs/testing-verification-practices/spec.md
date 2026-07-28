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
