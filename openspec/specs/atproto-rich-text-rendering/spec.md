# ATProto Rich Text Rendering

## Provenance
- `BA-004` (`docs/ai-tasks/2026-05-04-BA-004-rich-text-facets.md`)
- `docs/TESTING.md`

## Purpose
Define how ATProto rich-text facets are rendered consistently across browse and detail/comment surfaces.

## Requirements

### Requirement: Supported rich-text facets render across browse and detail surfaces
The system SHALL render ATProto rich-text facets for links, tags, and mentions in shared rich-text output used by browse and art detail/comment rendering.

#### Scenario: Rich text with facets is rendered
- **WHEN** text includes supported ATProto facet metadata
- **THEN** output contains interactive rendered elements for those facets and preserves remaining text content

### Requirement: Facet slicing follows UTF-8 byte ranges
The system MUST interpret facet bounds using UTF-8 byte offsets.

#### Scenario: Text contains multi-byte characters
- **WHEN** facet indexes target text with multi-byte characters
- **THEN** facet resolution honors UTF-8 byte offsets instead of character-index assumptions
