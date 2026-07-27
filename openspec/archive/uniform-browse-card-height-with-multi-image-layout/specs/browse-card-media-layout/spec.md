## ADDED Requirements

### Requirement: Browse cards keep a uniform vertical footprint
The system SHALL keep browse cards at a consistent height while applying media-aware card body behavior.

#### Scenario: Card contains image embeds
- **WHEN** a browse card includes image or video embeds
- **THEN** card text is suppressed and media uses the configured single-image or split-gallery layout while preserving card height
