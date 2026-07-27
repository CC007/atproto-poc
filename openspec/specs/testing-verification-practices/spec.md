# Testing and Verification Practices

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

## Provenance
- `docs/TESTING.md`
