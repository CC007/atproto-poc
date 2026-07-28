## ADDED Requirements

### Requirement: Visual tests run when visual evidence is needed during implementation
The system SHALL run visual regression tests whenever AI-led diagnosis or implementation decisions require visual evidence to resolve breakage or determine safe changes.

#### Scenario: Diagnosis requires UI confirmation
- **WHEN** AI-driven change work encounters a potential UI regression or uncertainty
- **THEN** visual regression tests are executed before implementation decisions are finalized

### Requirement: Visual regression suite is a completion gate
The system MUST execute the visual regression suite before any change is considered complete.

#### Scenario: Final verification before completion
- **WHEN** a change is ready for completion
- **THEN** the visual regression suite is run and must complete before the change can be marked done
