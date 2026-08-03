# GitHub Copilot Instructions

Use this repository guidance hierarchy:

1. `AGENTS.md`
2. `README.md`
3. `openspec/specs/*` (primary requirements)
4. `openspec/archive/*` (historical completed-change requirements/provenance)
5. `docs/ARCHITECTURE.md`
6. `docs/DECISIONS.md`
7. `docs/AI_TASKS.md`
8. `docs/TESTING.md`
9. `docs/SECURITY.md`
10. `docs/GLOSSARY.md`
11. `CONTRIBUTING.md`
12. `HELP.md`

## Copilot Behavior Requirements

- Read the files above before making non-trivial edits.
- Keep changes minimal and directly scoped to the task.
- Do not revert unrelated local edits.
- Preserve current behavior unless explicitly asked to change it.
- Prefer built-in tool calls (`glob`, `rg`, `view`, `apply_patch`, etc.) over shell commands when either approach works, because built-in calls are easier for developers to review and approve.
- For substantial changes, propose or add tests and update relevant pages in `docs/`.
- For OpenSpec design docs: if there are no open questions, remove the `## Open Questions` section instead of leaving placeholder or "none" content.

## Project-Specific Guidance

- Tech stack: Kotlin + Spring Boot + Gradle Kotlin DSL.
- UI rendering style: server-side `kotlinx.html`.
- Requirement-level behavior is tracked in `openspec/specs/*` and historical completed work in `openspec/archive/*`.
- `docs/*` pages remain implementation, testing, security, and terminology context.

## Documentation Maintenance

When your change impacts one of these domains, create or update the page:

- `docs/ARCHITECTURE.md`
- `docs/DECISIONS.md` (or files under `docs/adr/`)
- `docs/AI_TASKS.md` (and files under `docs/ai-tasks/`)
- `docs/TESTING.md`
- `docs/SECURITY.md`
- `docs/GLOSSARY.md`

If missing, generate a minimal first version with clear headings and actionable content.
