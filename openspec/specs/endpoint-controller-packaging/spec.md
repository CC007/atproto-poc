# Endpoint Controller Packaging

## Purpose
Define package-boundary expectations so HTTP endpoint controllers stay grouped under dedicated endpoint namespaces.

## Requirements

### Requirement: Endpoint handlers are organized under endpoint-specific packages
The system SHALL keep Spring MVC endpoint controllers under `com.github.cc007.blueart.endpoints.*`.

#### Scenario: Contributor locates route handlers
- **WHEN** route-handling code is inspected
- **THEN** endpoint controllers are found under the dedicated endpoints package hierarchy

## Provenance
- `BA-020` (`docs/ai-tasks/2026-05-07-BA-020-move-controllers-to-endpoints-folder.md`)
- `docs/ARCHITECTURE.md`
