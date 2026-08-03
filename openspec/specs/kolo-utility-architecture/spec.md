# Kolo Utility Architecture

## Provenance
- `BA-016` (`docs/ai-tasks/2026-05-06-BA-016-co-located-tailwind-like-styling-architecture.md`)
- `docs/DECISIONS.md` (D-007)

## Purpose
Define core Kolo utility-token architecture, canonicalization rules, and migration-era stylesheet coexistence.

## Requirements

### Requirement: Kolo utility tokens follow canonical architecture contracts
The system SHALL define explicit compiler token contracts for utility generation. Parser hooks SHALL parse raw utility strings into typed tokens, and generator hooks SHALL consume those typed tokens to emit CSS through the compiler pipeline. Supported utility families MUST include spacing and display tokens in the same parser/generator hook architecture.

#### Scenario: Tokens are prepared for stylesheet URL generation
- **WHEN** Kolo tokens are finalized for stylesheet delivery
- **THEN** they are canonicalized according to deterministic ordering and delimiter constraints

#### Scenario: Spacing utility token is parsed for generation
- **WHEN** a supported spacing token is parsed
- **THEN** a typed spacing compiler token is produced with raw token identity, utility metadata, and resolved spacing value

#### Scenario: Display utility token is parsed for generation
- **WHEN** a supported display token is parsed
- **THEN** a typed display compiler token is produced with raw token identity and resolved display utility metadata

#### Scenario: Generator receives unsupported token type
- **WHEN** a generator hook receives a token type it does not support
- **THEN** it returns `false` and leaves emission to other hooks or compiler diagnostics### Requirement: Kolo stylesheet delivery coexists with page CSS during migration
The system SHALL support side-by-side delivery of `kolo.css` and page-specific CSS during incremental utility migration.

#### Scenario: Utility migration is partial
- **WHEN** only a subset of declarations has moved to Kolo utilities
- **THEN** both utility and page stylesheet paths remain active without requiring full migration cutover
