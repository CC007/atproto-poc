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

### Requirement: Contribution guidance references OpenSpec requirement sources
The system MUST define guidance in AGENTS.md, .github/copilot-instructions.md, and CONTRIBUTING.md so requirement discovery points to OpenSpec capability specs and archived OpenSpec changes, and MUST NOT point to `docs/` pages as requirement sources.

#### Scenario: Guidance files point to specs and archive
- **WHEN** AGENTS.md, .github/copilot-instructions.md, or CONTRIBUTING.md documents where contributors and AI assistants should find requirements
- **THEN** those files reference `openspec/specs/*` and `openspec/archive/*` instead of `docs/*` requirement pages
