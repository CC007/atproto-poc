# BA-002: Remove proof-of-concept framing

## Metadata
- ID: `BA-002`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-04`
- Updated: `2026-05-04`
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
- [ ] Audit all files for `poc`, `proof-of-concept`, `PoC` references.
- [ ] Agree on new canonical package/artifact namespace (e.g. `com.github.cc007.blueart`).
- [ ] Rename package directories and update all imports.
- [ ] Update `build.gradle.kts` artifact ID and `settings.gradle.kts` project name.
- [ ] Update all documentation files.
- [ ] Add D-XXX entry in `docs/DECISIONS.md` capturing the rename rationale.
- [ ] Run `./gradlew test` to confirm refactor compiles and tests pass.

## Progress Log
- `2026-05-04`: Task created.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] Update CI/CD references if the artifact name is used there.

