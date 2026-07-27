## ADDED Requirements

### Requirement: Render-time Kolo integration emits canonical stylesheet links
The system SHALL collect Kolo tokens during HTML rendering, attach generated classes to elements, and emit a canonical `/css/generated/kolo.css` stylesheet href for the response.

#### Scenario: Page uses Kolo DSL in render context
- **WHEN** rendering executes `kolo { ... }` calls inside a Kolo-aware render context
- **THEN** token collection and final stylesheet-link emission include canonicalized tokens from the full rendered response
