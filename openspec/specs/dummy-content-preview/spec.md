# Dummy Content Preview

## Purpose

## Requirements

### Requirement: Dummy account provides a deterministic preview timeline
The system SHALL provide a deterministic dummy timeline for the dummy account so visual verification remains possible even when live data does not currently contain all supported post types, and timeline rendering MUST include follow-type dummy records as supported content rather than unsupported placeholders.

#### Scenario: Dummy timeline loads supported post variants including follows
- **WHEN** an authenticated dummy account opens the browse timeline
- **THEN** the system returns a deterministic set of dummy posts that covers every post type currently supported by timeline rendering, including follow-type records rendered as supported follow activity### Requirement: Dummy browsing reuses the standard browse and detail routes
The system SHALL reuse the existing browse and detail routes for dummy content, and MUST NOT introduce a separate dummy-only browsing flow.

#### Scenario: Dummy browsing uses the standard navigation flow
- **WHEN** a dummy account navigates from the timeline to a post detail page
- **THEN** the system uses the same browse and detail route structure as the live-content experience

### Requirement: Every dummy timeline post has a resolvable detail page
The system SHALL make each dummy timeline post accessible on a detail page through the existing detail rendering flow.

#### Scenario: Dummy post detail page resolves from the timeline
- **WHEN** a user opens the detail link for a dummy timeline post
- **THEN** the system renders that dummy post on the standard detail page route

### Requirement: Dummy fixtures remain available at runtime
The system MUST serve dummy authentication and content fixtures from runtime application components that are available whenever the application runs.

#### Scenario: Dummy data is served without test-only dependencies
- **WHEN** the application is running and a request targets the reserved localhost dummy network
- **THEN** the system responds with runtime-available dummy data instead of relying on test-only fixtures

### Requirement: Dummy media covers remote embed variants
The system SHALL include stable remote media URLs for dummy posts that represent supported image, video, and GIF-style embeds, and image-based dummy posts MUST use working remote image URLs so timeline cards can render all image variants.

#### Scenario: Remote media fixtures cover and load supported media types
- **WHEN** the dummy timeline or detail page renders a post with an image, video, or GIF-style embed
- **THEN** the system provides a stable remote media URL for that embed variant and image embeds resolve to working remote image resources