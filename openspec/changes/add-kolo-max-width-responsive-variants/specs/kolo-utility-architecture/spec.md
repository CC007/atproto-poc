## MODIFIED Requirements

### Requirement: Kolo utility tokens follow canonical architecture contracts
The system SHALL define explicit compiler token contracts for utility generation. Parser hooks SHALL parse raw utility strings into typed tokens, and generator hooks SHALL consume those typed tokens to emit CSS through the compiler pipeline. Supported utility families MUST include spacing, layout, font-family, font-size, font-weight, sizing, and max-width responsive variants tokens in the same parser/generator hook architecture. The layout family MUST include display, box-sizing, overflow, position, inset/top/right/bottom/left offsets, z-index, and object-fit tokens.

#### Scenario: Tokens are prepared for stylesheet URL generation
- **WHEN** Kolo tokens are finalized for stylesheet delivery
- **THEN** they are canonicalized according to deterministic ordering and delimiter constraints

#### Scenario: max-width responsive variants token is parsed for generation
- **WHEN** a supported max-width responsive variants token is parsed
- **THEN** a typed compiler token is produced with raw token identity and resolved utility metadata

#### Scenario: Generator receives unsupported token type
- **WHEN** a generator hook receives a token type it does not support
- **THEN** it returns `false` and leaves emission to other hooks or compiler diagnostics
