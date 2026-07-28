# Browse Card Media Layout

## Provenance
- `BA-005` (`docs/ai-tasks/2026-05-04-BA-005-uniform-browse-card-layout.md`)
- `docs/ARCHITECTURE.md`

## Purpose
Define timeline-card layout behavior for consistent card height and media-aware rendering.

## Requirements

### Requirement: Browse cards maintain consistent vertical layout
The system SHALL keep timeline cards at a uniform height in browse views.

#### Scenario: Timeline renders mixed posts
- **WHEN** browse renders cards with mixed content types
- **THEN** cards maintain the configured consistent vertical footprint

### Requirement: Embed-aware card text and gallery behavior is applied
The system SHALL suppress card body text when embeds are present and apply media layout rules for single-image versus multi-image cards.

#### Scenario: Card includes multi-image embed
- **WHEN** a card has 2-4 image embeds
- **THEN** the first image renders as the primary slot and remaining images render as stacked supporting slots
