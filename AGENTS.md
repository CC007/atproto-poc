# Agent Operating Guide

This file defines how AI coding assistants should behave in this repository.

## Mandatory Context

Before planning or editing, read these files in order:

1. `README.md`
2. `.github/copilot-instructions.md`
3. `docs/ARCHITECTURE.md`
4. `docs/DECISIONS.md`
5. `docs/AI_TASKS.md`
6. `docs/TESTING.md`
7. `docs/SECURITY.md`
8. `docs/GLOSSARY.md`
9. `CONTRIBUTING.md`
10. `HELP.md`

If any file is missing or stale, propose an update as part of the change.

## Repo Rules

- Keep implementation consistent with Kotlin + Spring Boot + server-rendered `kotlinx.html` patterns.
- Prefer focused edits with minimal diff size.
- Do not revert unrelated local changes.
- Preserve existing behavior unless the task explicitly requires a change.
- For non-trivial code changes, add or update tests where practical.

## Validation Rules

- Run relevant Gradle checks for changed code paths.
- At minimum, run targeted tests; use `./gradlew test` when changes are broad.
- If validation cannot run, state what was not verified.

## Handoff Rules

After significant changes, update or create the relevant docs pages:

- `docs/ARCHITECTURE.md` for component boundaries, data flow, routes, or major structure changes
- `docs/DECISIONS.md` (or `docs/adr/`) for important technical tradeoffs and rationale
- `docs/AI_TASKS.md` (and `docs/ai-tasks/`) for AI-owned task state and completion notes
- `docs/TESTING.md` for verification scope, commands, and gaps
- `docs/SECURITY.md` for auth, secrets, threat model, or hardening updates
- `docs/GLOSSARY.md` for new domain terms or naming conventions


If the page does not exist and the change clearly impacts that area, create a minimal version.

