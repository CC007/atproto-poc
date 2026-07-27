# AI Task Tracking

## Purpose
Define how AI tasks are tracked in docs while completed work is referenced through OpenSpec archive entries.

## Requirements

### Requirement: AI task index tracks non-completed work
The system SHALL keep `docs/AI_TASKS.md` as the active tracker for AI tasks that are not in `completed` state.

#### Scenario: Active task remains tracked in docs
- **WHEN** an AI task status is `todo`, `in_progress`, `partial`, `blocked`, or `cancelled`
- **THEN** it remains represented in `docs/AI_TASKS.md` and its task-record file remains under `docs/ai-tasks/`

### Requirement: Completed AI tasks link to OpenSpec archive
The system MUST reference migrated completed AI work through OpenSpec archived changes.

#### Scenario: Completed task entry points to archive
- **WHEN** a task is listed as completed in `docs/AI_TASKS.md`
- **THEN** the entry links to the corresponding `openspec/archive/<slug>/` change directory

## Provenance
- `BA-001` (`docs/ai-tasks/2026-05-04-BA-001-add-ai-task-tracker.md`)
- `docs/AI_TASKS.md`
