## ADDED Requirements

### Requirement: Endpoint controllers are organized under dedicated package roots
The system SHALL keep Spring MVC endpoint controllers under `com.github.cc007.blueart.endpoints` subpackages.

#### Scenario: Controller ownership is discoverable
- **WHEN** contributors inspect endpoint-handling source files
- **THEN** controller classes are grouped under the `endpoints` package hierarchy rather than mixed with non-endpoint concerns
