## ADDED Requirements

### Requirement: background and border utility ownership
The system SHALL provide this Kolo utility capability through typed DSL helpers, explicit parser allow-lists, and generator hooks that emit deterministic CSS for supported tokens.

#### Scenario: Supported utility is rendered
- **WHEN** a browse or art render site uses a typed helper for a supported utility
- **THEN** the element receives the canonical Kolo class and the generated stylesheet emits the equivalent CSS

#### Scenario: Unsupported utility is requested
- **WHEN** an unsupported token is supplied to the stylesheet endpoint
- **THEN** no utility CSS is emitted and the existing deterministic diagnostic behavior is retained

### Requirement: Migration retains explicit exceptions
The system MUST keep declarations without direct utility parity in `CssController` with a `kolo-exception` marker until a dedicated capability supports them.

#### Scenario: Declaration cannot be migrated
- **WHEN** a declaration requires an unsupported value, selector relationship, or browser-specific behavior
- **THEN** the generated page stylesheet retains that declaration as an explicit exception
