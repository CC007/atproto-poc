## ADDED Requirements

### Requirement: BlueArt naming is canonical
The system SHALL use BlueArt naming for package, artifact, and application identity, and MUST NOT retain obsolete proof-of-concept framing in current project identity.

#### Scenario: Repository identity is BlueArt
- **WHEN** contributors read project metadata and package references
- **THEN** canonical naming resolves to BlueArt conventions instead of legacy POC labels
