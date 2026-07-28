# Kolo Utility Architecture

## Provenance
- `BA-016` (`docs/ai-tasks/2026-05-06-BA-016-co-located-tailwind-like-styling-architecture.md`)
- `docs/DECISIONS.md` (D-007)

## Purpose
Define core Kolo utility-token architecture, canonicalization rules, and migration-era stylesheet coexistence.

## Requirements

### Requirement: Kolo utility tokens follow canonical architecture contracts
The system SHALL define Kolo utility token model and canonicalization rules, including deterministic ordering and semicolon-delimited token transport.

#### Scenario: Tokens are prepared for stylesheet URL generation
- **WHEN** Kolo tokens are finalized for stylesheet delivery
- **THEN** they are canonicalized according to deterministic ordering and delimiter constraints

### Requirement: Kolo stylesheet delivery coexists with page CSS during migration
The system SHALL support side-by-side delivery of `kolo.css` and page-specific CSS during incremental utility migration.

#### Scenario: Utility migration is partial
- **WHEN** only a subset of declarations has moved to Kolo utilities
- **THEN** both utility and page stylesheet paths remain active without requiring full migration cutover
