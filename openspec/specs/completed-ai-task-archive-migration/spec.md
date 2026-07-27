# Completed Ai Task Archive Migration

## Purpose
Define how completed AI tasks are represented as archived OpenSpec changes with preserved provenance to the original docs-based task records.

## Requirements

### Requirement: Completed AI tasks are archived as OpenSpec changes
The system SHALL represent every AI task marked `completed` in docs as an archived OpenSpec change directory with complete change artifacts.

#### Scenario: Completed task is migrated into archive
- **WHEN** a task in `docs/AI_TASKS.md` is marked `completed`
- **THEN** an archive entry exists under `openspec/archive/<slug>/` for that task with `proposal.md`, `design.md`, and `tasks.md`

### Requirement: Archived changes preserve task provenance
The system MUST preserve provenance from legacy AI task records to archived OpenSpec artifacts.

#### Scenario: Archive artifact references source task identity
- **WHEN** a completed task is migrated
- **THEN** the archived change content references the original task ID, title, and source task file path
