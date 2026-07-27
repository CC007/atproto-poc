## ADDED Requirements

### Requirement: Kolo CSS endpoint generates stylesheet output from token input
The system SHALL generate `/css/generated/kolo.css` output from request token input through parser/generator hooks while surfacing malformed or unsupported tokens as CSS diagnostics.

#### Scenario: Unsupported token is handled permissively
- **WHEN** a request contains unsupported or malformed Kolo tokens
- **THEN** the endpoint still returns CSS and includes diagnostic comments for unparsed or unsupported tokens
