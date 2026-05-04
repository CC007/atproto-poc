# GitHub Copilot Instructions

Use this repository guidance hierarchy:

1. `AGENTS.md`
2. `README.md`
3. `docs/ARCHITECTURE.md`
4. `docs/DECISIONS.md`
5. `docs/TESTING.md`
6. `docs/SECURITY.md`
7. `docs/GLOSSARY.md`
8. `CONTRIBUTING.md`
9. `HELP.md`

## Copilot Behavior Requirements

- Read the files above before making non-trivial edits.
- Keep changes minimal and directly scoped to the task.
- Do not revert unrelated local edits.
- Preserve current behavior unless explicitly asked to change it.
- For substantial changes, propose or add tests and update relevant pages in `docs/`.

## Project-Specific Guidance

- Tech stack: Kotlin + Spring Boot + Gradle Kotlin DSL.
- UI rendering style: server-side `kotlinx.html`.
- Existing routes, decisions, testing notes, security notes, and terminology are tracked in the docs pages under `docs/`.

## Documentation Maintenance

When your change impacts one of these domains, create or update the page:

- `docs/ARCHITECTURE.md`
- `docs/DECISIONS.md` (or files under `docs/adr/`)
- `docs/TESTING.md`
- `docs/SECURITY.md`
- `docs/GLOSSARY.md`

If missing, generate a minimal first version with clear headings and actionable content.

