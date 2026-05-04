# BA-002: Remove proof-of-concept framing

## Metadata
- ID: `BA-002`
- Status: `done`
- Owner: `ai`
- Created: `2026-05-04 22:56`
- Updated: `2026-05-04 23:06`
- Completed: `2026-05-04 23:06`
- Related Human Issue: none

## Goal
Rebrand the project documentation and in-code references away from "proof-of-concept" (POC) language. The project is no longer a POC — it is the start of a real web application.

## Scope
- In scope:
  - `README.md` title, description, and scope section.
  - `docs/ARCHITECTURE.md` overview paragraph.
  - `docs/SECURITY.md` current posture section.
  - Module/package names containing `poc` (e.g. `com.github.cc007.poc.atproto` → decide on the canonical name).
  - Any references in `build.gradle.kts`, `settings.gradle.kts`, and `HELP.md`.
  - `docs/DECISIONS.md` entry recording the rename rationale.
  - `docs/GLOSSARY.md` if POC term was defined.
- Out of scope:
  - Actual feature work.
  - Changing the runtime behavior of the application.

## Plan
- [x] Audit all files for `poc`, `proof-of-concept`, `PoC` references.
- [x] Agree on new canonical package/artifact namespace (`com.github.cc007.blueart`).
- [x] Rename package directories and update all imports.
- [x] Update `build.gradle.kts` artifact ID and `settings.gradle.kts` project name.
- [x] Update all documentation files.
- [x] Add D-005 entry in `docs/DECISIONS.md` capturing the rename rationale.
- [x] Run `./gradlew test` to confirm refactor compiles and tests pass.

## Progress Log
- `2026-05-04 22:56`: Task created.
- `2026-05-04 23:06`: Task completed by AI.

## How Completed
- Canonical new package name chosen: `com.github.cc007.blueart`.
- Moved all Kotlin sources from `src/*/kotlin/com/github/cc007/poc/atproto/` to `src/*/kotlin/com/github/cc007/blueart/`.
- Renamed main application class `AtprotoPocApplication` → `BlueArtApplication`; test runner `TestAtprotoPocApplication` → `TestBlueArtApplication`; test class `AtprotoPocApplicationTests` → `BlueArtApplicationTests`.
- Updated `build.gradle.kts`: `group`, `description`.
- Updated `src/main/resources/application.yaml`: `spring.application.name`, logging package.
- Updated `README.md`, `docs/ARCHITECTURE.md`, `docs/SECURITY.md`, `docs/DECISIONS.md` (D-001, added D-005).

## Verification
- `./gradlew test` → `BlueArtApplicationTests > contextLoads() PASSED`, `BUILD SUCCESSFUL`.

## Follow-ups
- [ ] Update CI/CD references if the artifact name is used there.
