# Ai Task Docs Active Only

## Purpose
Define the expected post-migration state where docs-based AI task tracking only contains active/non-completed tasks, while completed entries remain as links to archived OpenSpec changes.

## Requirements

### Requirement: Docs retain only non-completed AI tasks
The system SHALL keep docs-based AI task tracking only for tasks that are not completed.

#### Scenario: Active and incomplete tasks remain in docs
- **WHEN** migration updates docs task tracking
- **THEN** tasks in `todo`, `in_progress`, `partial`, `blocked`, or `cancelled` states remain tracked in `docs/AI_TASKS.md` and `docs/ai-tasks/`

### Requirement: Completed docs entries point to archived OpenSpec changes
The system MUST keep a Completed section in `docs/AI_TASKS.md` where each completed task entry links to its migrated archive change.

#### Scenario: Completed task links resolve to archive entries
- **WHEN** a task appears in the Completed section of `docs/AI_TASKS.md`
- **THEN** that entry includes a direct link to the corresponding `openspec/archive/<slug>/` change directory

### Requirement: Migrated completed task files are removed from docs task records
The system MUST remove `docs/ai-tasks/*` markdown files for tasks that have been successfully migrated as completed archive changes.

#### Scenario: Completed task file is pruned after successful migration
- **WHEN** archive artifacts and corresponding spec coverage exist for a completed task
- **THEN** the completed task markdown file no longer exists under `docs/ai-tasks/`
