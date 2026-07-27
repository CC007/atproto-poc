## ADDED Requirements

### Requirement: Browse and art stylesheets are generated server-side
The system SHALL generate browse and art page stylesheets from Kotlin CSS DSL endpoints.

#### Scenario: Generated stylesheet endpoint returns CSS
- **WHEN** a client requests `/css/generated/browse.css` or `/css/generated/art.css`
- **THEN** the response provides generated CSS from server-side Kotlin stylesheet definitions
