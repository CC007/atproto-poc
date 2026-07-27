## ADDED Requirements

### Requirement: Kolo utility architecture defines token and delivery contracts
The system SHALL define utility authoring and delivery through the Kolo DSL/token architecture, including canonical token ordering and `kolo.css` URL contracts.

#### Scenario: Utility tokens are canonicalized for stylesheet delivery
- **WHEN** render-time utility tokens are collected for a page response
- **THEN** token canonicalization and stylesheet URL generation follow the documented Kolo architecture contract
