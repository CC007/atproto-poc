# BlueArt

BlueArt is a Kotlin + Spring Boot proof-of-concept for browsing Bluesky/ATProto content with a server-rendered UI.

## Project Scope

- Focus: art-oriented timeline browsing and post detail pages.
- Stack: Kotlin, Spring Boot, Gradle Kotlin DSL, `kotlinx.html`.
- Current implementation context and handoff notes live in dedicated docs pages under `docs/`.

## Quick Start

```bash
./gradlew test
./gradlew bootRun
```

Then open:

- `http://localhost:8080/browse`

## AI Steering Files (Read In This Order)

When working with AI assistants, treat these as mandatory context:

1. `AGENTS.md` (global AI behavior and repo rules)
2. `.github/copilot-instructions.md` (GitHub Copilot-specific instructions)
3. `docs/ARCHITECTURE.md` (components, boundaries, routes, and data flows)
4. `docs/DECISIONS.md` (decision history and rationale)
5. `docs/TESTING.md` (test strategy and verification commands)
6. `docs/SECURITY.md` (security model and hardening notes)
7. `docs/GLOSSARY.md` (domain terms and naming consistency)
8. `CONTRIBUTING.md` (contribution rules and validation expectations)
9. `HELP.md` (generated references and environment notes)

## Documentation Roadmap (Nice-to-Have)

These pages are part of the expected documentation surface for future edits. If a change touches one of these areas, create or update the page:

- `docs/ARCHITECTURE.md` (components, boundaries, data flows)
- `docs/DECISIONS.md` or `docs/adr/` (decision history and rationale)
- `docs/TESTING.md` (test strategy and command matrix)
- `docs/SECURITY.md` (security model, secrets, and hardening guidance)
- `docs/GLOSSARY.md` (domain language and naming consistency)

## Notes

- `HELP.md` records the observed Java/Kotlin compatibility note (JVM 24 vs 25).
- This repository may evolve quickly; keep the docs pages under `docs/` up to date after meaningful changes.

