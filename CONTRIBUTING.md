# Contributing

## Working Agreement

Use small, reviewable changes and keep behavior changes explicit.

## Before You Edit

1. Read `AGENTS.md`.
2. Read `.github/copilot-instructions.md`.
3. Read these docs pages for current project state:
   - `docs/ARCHITECTURE.md`
   - `docs/DECISIONS.md`
   - `docs/AI_TASKS.md`
   - `docs/TESTING.md`
   - `docs/SECURITY.md`
   - `docs/GLOSSARY.md`

## Code Style and Structure

- Follow existing Kotlin and Spring Boot conventions in the repository.
- Keep files cohesive and avoid unrelated refactors in task-focused changes.
- Prefer clear naming and straightforward control flow.

## Validation

Run the most relevant checks for your change:

```bash
./gradlew test
```

If you cannot run checks, document what is unverified in your PR or handoff.

## Documentation Updates

Update the relevant docs pages under `docs/` after meaningful implementation work.

Also update or create docs when the change affects those concerns:

- `docs/ARCHITECTURE.md`
- `docs/DECISIONS.md` or `docs/adr/`
- `docs/AI_TASKS.md` and `docs/ai-tasks/`
- `docs/TESTING.md`
- `docs/SECURITY.md`
- `docs/GLOSSARY.md`

## AI-Assisted Contributions

When using GitHub Copilot for edits, ensure prompts include:

- The target task
- The files to modify
- A request to follow `AGENTS.md` and `.github/copilot-instructions.md`
- A request to update relevant docs pages under `docs/` if project state changes

## AI Task Tracking

- Use `docs/AI_TASKS.md` as the AI task index and status board.
- Create one task file per AI work item using `docs/ai-tasks/_TEMPLATE.md`.
- Keep status and progress current while work is in-flight.
- Record completion details in each task file under `How Completed`.

