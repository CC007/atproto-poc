# Security

## Current Posture
- This repository is a proof-of-concept and should not be treated as production-hardened.
- Security controls and threat modeling are evolving.

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

