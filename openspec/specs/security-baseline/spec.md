# Security Baseline

## Purpose
Define baseline repository security requirements for credential handling and input validation.

## Requirements

### Requirement: Sensitive values are never committed as plaintext
The system MUST avoid committing secrets, credentials, and private tokens into the repository, 
except for the explicitly allowed localhost dummy account credentials used for read-only dummy mode.

#### Scenario: Configuration needs secret material
- **WHEN** runtime credentials are required
- **THEN** they are sourced from environment variables or external secret management instead of committed source files

#### Scenario: Localhost dummy credentials are committed intentionally
- **WHEN** credentials belong to the reserved localhost dummy account used for read-only dummy-mode authentication
- **THEN** those credentials MAY be committed in-repo as an explicit exception to the no-credentials rule

### Requirement: External input is validated for auth and route parameters
The system SHALL validate and sanitize external input that influences authentication and route/query processing.

#### Scenario: Request includes route or query input
- **WHEN** user-supplied route parameters or query values are processed
- **THEN** those inputs are validated and sanitized before use

## Provenance
- `docs/SECURITY.md`
