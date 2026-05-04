# BA-001: Add in-repo AI task tracker

## Metadata
- ID: `BA-001`
- Status: `completed`
- Owner: `ai`
- Created: `2026-05-04`
- Updated: `2026-05-04`
- Related Human Issue: `none`

## Goal
Create a repository-local process to track AI tasks, including partial and completed work and how tasks were completed.

## Scope
- In scope:
  - Define AI task states and tracking workflow.
  - Add an index page and task record template.
  - Wire references into existing guidance docs.
- Out of scope:
  - Automation scripts for task creation.
  - GitHub Issue synchronization.

## Plan
- [x] Add AI task index and state model.
- [x] Add per-task folder guidance and template.
- [x] Update steering docs to include the new tracking mechanism.
- [x] Record rationale and glossary terms.

## Progress Log
- `2026-05-04`: Created AI task tracking docs and linked them from steering files.

## How Completed
List what was changed and how:
- Files edited:
  - `docs/AI_TASKS.md`
  - `docs/ai-tasks/README.md`
  - `docs/ai-tasks/_TEMPLATE.md`
  - `README.md`
  - `AGENTS.md`
  - `.github/copilot-instructions.md`
  - `CONTRIBUTING.md`
  - `docs/DECISIONS.md`
  - `docs/GLOSSARY.md`
- Commands run:
  - None (documentation-only change).
- Checks/tests run:
  - None (no runtime code changes).
- Constraints or tradeoffs:
  - Chose a minimal markdown-based approach to keep process lightweight and reviewable in Git.

## Verification
- Result: Manual review of file links and workflow consistency completed.
- Not verified: Automated linting/format checks for markdown.

## Follow-ups
- [ ] Decide whether to add `docs/ai-tasks/BA-NUMBERING.md` with explicit numbering ownership.
- [ ] Optionally add a script to scaffold new task files from template.


