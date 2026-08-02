# Kolo CSS Generation — Delta

## MODIFIED Requirements

### Requirement: Kolo CSS endpoint handles tokenized requests deterministically
The system SHALL generate CSS from `/css/generated/kolo.css` requests using the parser/generator hook pipeline. Generator hooks SHALL emit rules into a shared `kotlinx.css.CssBuilder` instance owned by the compiler instead of returning raw CSS strings, and the compiler SHALL return the final CSS by serializing that shared builder.

#### Scenario: Valid Kolo tokens are provided
- **WHEN** a request includes supported Kolo tokens
- **THEN** generated CSS rules are emitted through configured parser/generator hooks into the shared `CssBuilder`

#### Scenario: Hook handling is explicit
- **WHEN** a generator hook receives a parsed `StyleUtilityDefinition`
- **THEN** it signals whether it handled the token by returning a boolean result

### Requirement: Unsupported and malformed tokens are surfaced diagnostically
The system MUST preserve permissive behavior by returning CSS with explicit diagnostics for unparsed or unsupported tokens. When parsing fails or no generator hook handles a parsed token, the compiler MUST append diagnostic comments to the shared `CssBuilder`.

#### Scenario: Request includes malformed token
- **WHEN** token parsing fails for an entry
- **THEN** the response still returns CSS and includes a diagnostic comment for that token

#### Scenario: No hook handles a parsed token
- **WHEN** a token is parsed successfully but no generator hook handles it
- **THEN** the response still returns CSS and includes a diagnostic comment for that token
