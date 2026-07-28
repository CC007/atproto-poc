# Server Rendered Web Architecture

## Provenance
- `docs/ARCHITECTURE.md`
- `docs/DECISIONS.md` (D-001, D-003)

## Purpose
Define the core server-rendered web architecture and route-model expectations for browse/detail experiences.

## Requirements

### Requirement: BlueArt renders views server-side
The system SHALL render browse and detail experiences server-side using Kotlin/Spring MVC with `kotlinx.html`.

#### Scenario: Browse request is handled
- **WHEN** a user requests browse or detail routes
- **THEN** the server renders HTML responses through Kotlin-based view rendering

### Requirement: Route model preserves browse and art detail surfaces
The system MUST maintain route availability for timeline browsing and art detail rendering.

#### Scenario: User navigates from browse to detail
- **WHEN** a timeline item is opened
- **THEN** navigation resolves through the established browse/detail route model
