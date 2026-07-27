## ADDED Requirements

### Requirement: Rich text facets render as interactive elements
The system SHALL render supported ATProto facets (link, tag, mention) using UTF-8 byte-range indexing in shared rich-text output.

#### Scenario: Post text contains mixed facets
- **WHEN** post text contains supported rich-text facets
- **THEN** the rendered output converts those facets into their interactive HTML equivalents while preserving surrounding text
