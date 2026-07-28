# Project Branding and Identity

## Provenance
- `BA-002` (`docs/ai-tasks/2026-05-04-BA-002-remove-poc-framing.md`)
- `docs/DECISIONS.md` (D-005)

## Purpose
Define canonical BlueArt naming and identity requirements across project metadata and source references.

## Requirements

### Requirement: BlueArt is the canonical project identity
The system SHALL use `blueart` naming and `com.github.cc007.blueart` package identity for current project references.

#### Scenario: Project references avoid legacy naming
- **WHEN** repository metadata and source package declarations are updated
- **THEN** they use BlueArt naming conventions and not deprecated proof-of-concept labels
