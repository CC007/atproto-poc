# AI Task Records

## Directory Purpose
Store one markdown file per AI task so progress and completion details are versioned in Git.

## File Naming
Use:

`YYYY-MM-DD-BA-XXX-short-title.md`

Example:

`2026-05-04-BA-001-add-ai-task-tracker.md`

## Lifecycle
1. Create task file from `_TEMPLATE.md`.
2. Keep `Status` and `Progress Log` updated as work advances.
3. On completion, fill `How Completed` and `Verification`.
4. Update `docs/AI_TASKS.md` tables to reflect final state.

## Archival Rule
- Keep non-completed task files (`todo`, `in_progress`, `partial`, `blocked`, `cancelled`) in this directory.
- After a task is migrated to OpenSpec archive as `completed`, remove its file from this directory and keep the historical link in `docs/AI_TASKS.md` pointing to `openspec/archive/<slug>/`.
