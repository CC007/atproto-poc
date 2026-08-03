# Agent Operating Guide

This file defines how AI coding assistants should behave in this repository.

## Mandatory Context

Before planning or editing, read these files in order:

1. `README.md`
2. `.github/copilot-instructions.md`
3. `openspec/specs/*` (primary requirement source)
4. `openspec/archive/*` (historical completed-change context)
5. `docs/ARCHITECTURE.md`
6. `docs/DECISIONS.md`
7. `docs/AI_TASKS.md`
8. `docs/TESTING.md`
9. `docs/SECURITY.md`
10. `docs/GLOSSARY.md`
11. `CONTRIBUTING.md`
12. `HELP.md`

If any file is missing or stale, propose an update as part of the change.

Requirement discovery MUST come from `openspec/specs/*` and `openspec/archive/*`; `docs/*` pages are supplemental implementation/operations context.

## Repo Rules

- Keep implementation consistent with Kotlin + Spring Boot + server-rendered `kotlinx.html` patterns.
- Prefer focused edits with minimal diff size.
- Do not revert unrelated local changes.
- Preserve existing behavior unless the task explicitly requires a change.
- Prefer built-in tool calls (`glob`, `rg`, `view`, `apply_patch`, etc.) over shell commands when they can do the same work, because they are easier for developers to review and approve.
- For non-trivial code changes, add or update tests where practical.
- The AI does not push code; only the user pushes to remote repositories.
- The AI only creates commits or amends commits when the user explicitly asks.

## Validation Rules

- Run relevant Gradle checks for changed code paths.
- At minimum, run targeted tests; use `./gradlew test` when changes are broad.
- For OpenSpec work, after completing all change tasks run `openspec validate --all` and fix every reported validation issue before handoff.
- If validation cannot run, state what was not verified.
- The terminal starts in the project root; no need to `cd` into the project before running Gradle commands.

## Handoff Rules

After significant changes, update or create the relevant docs pages:

- `docs/ARCHITECTURE.md` for component boundaries, data flow, routes, or major structure changes
- `docs/DECISIONS.md` (or `docs/adr/`) for important technical tradeoffs and rationale
- `docs/AI_TASKS.md` (and `docs/ai-tasks/`) for AI-owned task state and completion notes
- `docs/TESTING.md` for verification scope, commands, and gaps
- `docs/SECURITY.md` for auth, secrets, threat model, or hardening updates
- `docs/GLOSSARY.md` for new domain terms or naming conventions


If the page does not exist and the change clearly impacts that area, create a minimal version.
