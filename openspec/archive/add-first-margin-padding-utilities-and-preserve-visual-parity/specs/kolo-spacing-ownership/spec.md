## ADDED Requirements

### Requirement: Margin and padding ownership migrates to Kolo spacing utilities
The system SHALL move migrated margin/padding declarations from page CSS generation into typed Kolo spacing utilities while preserving rendered layout parity.

#### Scenario: Migrated spacing declarations are utility-owned
- **WHEN** a browse or art spacing declaration is migrated to Kolo DSL helpers
- **THEN** equivalent spacing is generated through `kolo.css` utility rules and redundant page CSS declarations are removed
