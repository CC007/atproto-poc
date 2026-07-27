## ADDED Requirements

### Requirement: Requirement knowledge is consolidated into capability specs
The system SHALL consolidate requirement-level behavior from completed AI task outcomes and core docs pages into OpenSpec capability specs.

#### Scenario: Requirement-level behavior is represented in specs
- **WHEN** migration extracts requirement statements from completed tasks and core docs pages
- **THEN** those statements are captured as normative requirements in `openspec/specs/*` capability specs

### Requirement: Specs are the primary requirement source after migration
The system MUST make OpenSpec capability specs the primary source for requirement discovery after migration.

#### Scenario: Requirement lookup resolves through OpenSpec specs
- **WHEN** a contributor needs requirement-level guidance for migrated completed work
- **THEN** the required behavior is discoverable in OpenSpec capability specs without depending on `docs/ARCHITECTURE.md`, `docs/DECISIONS.md`, `docs/GLOSSARY.md`, `docs/SECURITY.md`, or `docs/TESTING.md`
