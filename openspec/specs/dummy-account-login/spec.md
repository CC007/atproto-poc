# Dummy Account Login

## Purpose
Define the dummy-account authentication behavior in the existing login flow so local development and demo sessions can authenticate without live Bluesky credentials while keeping real-network login behavior unchanged.

## Requirements

### Requirement: Users can start a dummy browsing session from the existing login flow
The system SHALL allow a user to authenticate a dummy account by submitting the existing login form with the reserved localhost network URL, and MUST NOT require a separate dummy-only login control.

#### Scenario: Login form selects dummy mode
- **WHEN** a user submits the standard login form with the reserved localhost network URL
- **THEN** the system authenticates the session as the dummy account through the same login submission flow

### Requirement: Dummy login bypasses Bluesky authentication
The system MUST authenticate the dummy account without contacting Bluesky or requiring live Bluesky credentials when the reserved localhost network URL is selected.

#### Scenario: Dummy login avoids remote authentication
- **WHEN** a login request targets the reserved localhost network URL
- **THEN** the system completes authentication for the dummy account without performing remote Bluesky authentication

### Requirement: Real-network login behavior remains unchanged
The system SHALL continue to use the existing Bluesky authentication flow for non-localhost network URLs.

#### Scenario: Non-localhost login uses the live authentication path
- **WHEN** a user submits the login form with a non-localhost network URL
- **THEN** the system uses the existing Bluesky authentication path instead of the dummy account path
