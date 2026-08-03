# Kolo Utility Architecture — Delta

## MODIFIED Requirements

### Requirement: Kolo utility tokens follow canonical architecture contracts
The system SHALL define explicit compiler token contracts for utility generation. Parser hooks SHALL parse raw utility strings into typed tokens, and generator hooks SHALL consume those typed tokens to emit CSS through the compiler pipeline.

#### Scenario: Spacing utility token is parsed for generation
- **WHEN** a supported spacing token is parsed
- **THEN** a typed spacing compiler token is produced with raw token identity, utility metadata, and resolved spacing value

#### Scenario: Generator receives unsupported token type
- **WHEN** a generator hook receives a token type it does not support
- **THEN** it returns `false` and leaves emission to other hooks or compiler diagnostics
