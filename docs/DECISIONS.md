# Decisions

## Purpose
Track technical decisions and rationale in one place. Use this file for concise entries, or add detailed ADRs under `docs/adr/` when needed.

## Decision Log

### D-001: Keep server-rendered UI
- Status: accepted
- Context: Project is a POC focused on fast iteration for browse/detail pages.
- Decision: Render pages server-side with `kotlinx.html` instead of introducing an SPA framework.
- Consequences: Lower frontend complexity, but fewer client-side interaction patterns out of the box.

### D-002: Inline SVG for stat icons
- Status: accepted
- Context: Stat rows require consistent icon rendering across environments.
- Decision: Use inline SVG icons in HTML components.
- Consequences: Deterministic rendering and no emoji/font dependency.

### D-003: URI-first art detail lookup
- Status: accepted
- Context: Post detail requests are most reliable with URI identity.
- Decision: Resolve `/art/{cid}` primarily via provided `uri`, with CID-only fallback as best-effort.
- Consequences: More stable detail fetch when URI is present; CID-only routes may miss off-timeline posts.

### D-004: Track AI tasks in repository docs
- Status: accepted
- Context: GitHub Issues are reserved for human planning and ownership, but AI work requires versioned status and completion traceability.
- Decision: Introduce `docs/AI_TASKS.md` as an index and maintain per-task records under `docs/ai-tasks/`.
- Consequences: AI progress is reviewable in Git history; contributors must keep task records updated as part of handoff.

