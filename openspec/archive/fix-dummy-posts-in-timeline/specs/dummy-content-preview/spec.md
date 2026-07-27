## MODIFIED Requirements

### Requirement: Dummy account provides a deterministic preview timeline
The system SHALL provide a deterministic dummy timeline for the dummy account so visual verification remains possible even when live data does not currently contain all supported post types, and timeline rendering MUST include follow-type dummy records as supported content rather than unsupported placeholders.

#### Scenario: Dummy timeline loads supported post variants including follows
- **WHEN** an authenticated dummy account opens the browse timeline
- **THEN** the system returns a deterministic set of dummy posts that covers every post type currently supported by timeline rendering, including follow-type records rendered as supported follow activity

### Requirement: Dummy media covers remote embed variants
The system SHALL include stable remote media URLs for dummy posts that represent supported image, video, and GIF-style embeds, and image-based dummy posts MUST use working remote image URLs so timeline cards can render all image variants.

#### Scenario: Remote media fixtures cover and load supported media types
- **WHEN** the dummy timeline or detail page renders a post with an image, video, or GIF-style embed
- **THEN** the system provides a stable remote media URL for that embed variant and image embeds resolve to working remote image resources
