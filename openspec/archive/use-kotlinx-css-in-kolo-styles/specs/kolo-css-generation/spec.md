# Kolo CSS Generation — Delta

## MODIFIED Requirements

### Requirement: Kolo CSS endpoint handles tokenized requests deterministically
The system SHALL generate CSS from `/css/generated/kolo.css` requests using the parser/generator hook pipeline. Parser hooks SHALL parse raw token strings into typed compiler tokens, generator hooks SHALL emit rules into a shared `kotlinx.css.CssBuilder` instance owned by the compiler instead of returning raw CSS strings, and the compiler SHALL return final CSS by serializing that shared builder.

#### Scenario: Valid Kolo tokens are provided
- **WHEN** a request includes supported Kolo tokens
- **THEN** generated CSS rules are emitted through configured parser/generator hooks into the shared `CssBuilder`

#### Scenario: Hook handling is explicit
- **WHEN** a generator hook receives a parsed compiler token
- **THEN** it signals whether it handled the token by returning a boolean result

#### Scenario: Parser and generator contracts are type-safe
- **WHEN** a parser hook supports a token string
- **THEN** it returns a typed compiler token consumed by generator hooks

### Requirement: Unsupported and malformed tokens are surfaced diagnostically
The system MUST preserve permissive behavior by returning CSS with explicit diagnostics for unparsed or unsupported tokens. When parsing fails or no generator hook handles a parsed token, the compiler MUST append deterministic diagnostics to the shared `CssBuilder` as `:root` CSS custom properties with the forms `--kolo-unparsed-<index>` and `--kolo-unsupported-<index>`.

#### Scenario: Request includes malformed token
- **WHEN** token parsing fails for an entry
- **THEN** the response still returns CSS and includes a deterministic diagnostic entry for that token

#### Scenario: No hook handles a parsed token
- **WHEN** a token is parsed successfully but no generator hook handles it
- **THEN** the response still returns CSS and includes a deterministic diagnostic entry for that token

#### Scenario: Diagnostic output shape is deterministic
- **WHEN** malformed or unsupported tokens are encountered
- **THEN** diagnostics are encoded in deterministic `:root` custom-property names with quoted token values
