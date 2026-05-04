# Security

## Current Posture
- This repository is a pre-production web application. Security controls and threat modeling are evolving.
- Do not deploy to production until explicit hardening and threat modeling are complete.

## Practices
- Do not commit secrets, credentials, or private tokens.
- Prefer environment variables or external secret managers for sensitive values.
- Review dependencies periodically for known vulnerabilities.

## Authentication Notes
- Any ATProto/Bluesky auth handling should avoid leaking tokens in logs or rendered pages.
- Validate and sanitize external input used in route parameters and query strings.

## Follow-Ups
- Add explicit threat model notes once auth/session strategy is stabilized.
- Document hardening and deployment security requirements before production use.

