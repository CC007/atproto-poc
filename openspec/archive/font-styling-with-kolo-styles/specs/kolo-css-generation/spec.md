## MODIFIED Requirements

### Requirement: Kolo CSS endpoint handles tokenized requests deterministically
The system SHALL generate CSS from `/css/generated/kolo.css` requests using the parser/generator hook pipeline. Parser hooks SHALL parse raw token strings into typed compiler tokens, generator hooks SHALL emit rules into a shared `kotlinx.css.CssBuilder` instance owned by the compiler instead of returning raw CSS strings, and the compiler SHALL return final CSS by serializing that shared builder. The endpoint MUST generate rules for supported spacing, display, font-family, font-size, and font-weight utility tokens through the same deterministic pipeline.

#### Scenario: Valid Kolo tokens are provided
- **WHEN** a request includes supported Kolo tokens
- **THEN** generated CSS rules are emitted through configured parser/generator hooks into the shared `CssBuilder`

#### Scenario: Hook handling is explicit
- **WHEN** a generator hook receives a parsed compiler token
- **THEN** it signals whether it handled the token by returning a boolean result

#### Scenario: Parser and generator contracts are type-safe
- **WHEN** a parser hook supports a token string
- **THEN** it returns a typed compiler token consumed by generator hooks

#### Scenario: Display utility token is included in request
- **WHEN** a request includes a supported display utility token
- **THEN** the endpoint emits the corresponding display CSS rule through the same parser/generator pipeline used by other utilities

#### Scenario: Font utility token is included in request
- **WHEN** a request includes a supported font utility token
- **THEN** the endpoint emits the corresponding font CSS rule through the same parser/generator pipeline used by other utilities
