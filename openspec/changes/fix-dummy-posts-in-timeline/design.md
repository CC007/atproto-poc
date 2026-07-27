# Design: Fix dummy posts in timeline

## Context

This change addresses two gaps captured in `proposal.md`:
1. only GIF-style dummy media reliably loads in the timeline, while other image fixtures are failing in practice;
2. follow-type dummy posts exist in the fixture feed but are rendered as unsupported placeholders.

Current behavior is implemented across app-level dummy and rendering components:
- `DummyAtProtoFixtures.kt` defines deterministic timeline fixture posts and media URLs.
- `DummyAtProtoTimelineController.kt` serves fixture timeline pages via ATProto-shaped localhost responses.
- `BrowseController.kt` implements normal timeline fetch/render path. Works also for dummy sessions.
- `PostSummary.kt` renders card bodies by record/embed type and currently falls back to “not yet supported” for `GraphFollow` records.

Constraints and stakeholders:
- Must preserve existing localhost dummy-network and general browse architecture, consistent with the original capability design.
- Must remain deterministic and runtime-available (not test-only), consistent with `dummy-content-preview` and `dummy-account-login` capability requirements.
- Must avoid regressions in real network behavior and preserve existing `/browse` and `/art/{cid}` routing.
- Primary stakeholders are developers/reviewers using dummy mode for visual/manual verification of supported timeline content.

## Goals / Non-Goals

**Goals:**
- Ensure dummy timeline image media renders reliably for all image-based dummy posts.
- Add first-class rendering support for follow-type dummy records (`GraphFollow`) in timeline cards.
- Keep the existing dummy auth/timeline controller contract and standard browse/detail routes unchanged.
- Keep fixture determinism and stable visual verification coverage across supported post/record/embed types.

**Non-Goals:**
- Redesigning timeline UI structure, filters, pagination model, or route layout.
- Expanding support for unrelated unsupported record types beyond follow posts.
- Reworking dummy auth/session behavior or introducing feature flags/toggles.
- Replacing the ATProto-shaped localhost dummy endpoints.
- Changing follow-type detail-page behavior (`/art/{cid}` path) as part of this timeline-focused change.

## Decisions

### 1) Move dummy image fixtures to working remote media URLs without frontend host filtering

**Decision:** Replace fragile image fixture URLs with working remote media URLs, while keeping frontend/dummy-controller media policy permissive (no host allowlist/denylist filtering).

**Why:**
- The root issue is fixture reliability, not renderer inability to output `<img>` tags.
- Deterministic dummy previews require links that remain fetchable across environments and time.
- Keeps timeline rendering aligned with real Bluesky behavior where backend data policy is authoritative.

**Alternatives considered:**
- Keep current URLs and add retries/fallback rendering logic: rejected; masks source-data fragility and still causes broken media.
- Bundle local binary assets and bypass remote URLs: rejected for this change because existing dummy-content requirements explicitly cover remote media variants.
- Enforce frontend host allowlist/denylist filtering: rejected because media filtering is a backend concern, not a browse renderer concern.

### 2) Support `GraphFollow` as a dedicated timeline card record variant in `PostSummary`

**Decision:** Add a specific rendering branch for `GraphFollow` records in the record renderer, producing a meaningful follow-activity card message instead of the generic unsupported-type placeholder.

**Why:**
- “All dummy types supported in the timeline” requires follow-type records to be rendered intentionally.
- The current placeholder makes dummy coverage incomplete and weakens confidence in timeline type handling.
- A typed branch is low-risk and consistent with existing record-type dispatch style in `PostSummary`.

**Alternatives considered:**
- Remove follow records from fixtures: rejected; avoids the requirement rather than supporting the type.
- Convert follow fixtures into synthetic `FeedPost` text-only records: rejected; loses fidelity to the record type being tested.

### 3) Keep controller and paging contracts unchanged; apply fixes at fixture + renderer layers only

**Decision:** Leave `DummyAtProtoTimelineController` request/response shape and fixture paging cursors intact; scope changes to fixture media data and timeline record rendering.

**Why:**
- Problem scope is media reliability and record-type presentation, not transport contract or pagination semantics.
- Preserving controller contract avoids unnecessary risk to login flow, timeline retrieval, and existing tests.

**Alternatives considered:**
- Refactor dummy timeline API shape or pagination while touching this area: rejected as unrelated churn.

### 4) Strengthen test coverage around image URL validity assumptions and follow-record rendering

**Decision:** Extend existing dummy timeline/component tests to assert:
- image fixture URLs for image-based posts are non-empty HTTPS direct media URLs expected by the renderer;
- follow-type records no longer render unsupported placeholder text and produce the intended follow card output.

**Why:**
- Existing tests already verify presence of embed/record categories but not rendering quality of follow cards or fixture reliability rules.
- This protects against regressions when future fixture updates happen.

**Alternatives considered:**
- Manual-only validation: rejected; too easy to regress silently.

### 5) Render follow cards with actor attribution only

**Decision:** Follow-type cards will render actor attribution only (e.g., “@dummy followed @target”), without timestamp/extra metadata in this change.

**Why:**
- Matches current UI maturity where other post types also do not show timestamp/metadata in timeline card bodies.
- Keeps this fix scoped to missing type support rather than broader timeline information architecture changes.

**Alternatives considered:**
- Include timestamp/metadata now: rejected as out of scope and inconsistent with current timeline card presentation.

## Risks / Trade-offs

- [Remote fixture URLs can still degrade over long time horizons] → Mitigation: keep fixture maintenance lightweight (replace broken URLs as needed) and keep focused tests that fail loudly when fixture assumptions change.
- [New follow-card wording may be too implementation-specific for future UX changes] → Mitigation: test semantic intent (follow activity rendered) rather than brittle exact copy where possible.
- [Supporting only follow among unsupported records may create partial-consistency expectations] → Mitigation: explicitly scope this change to follow type and track additional record-type support as separate capability updates.

## Migration Plan

1. Update dummy fixture image/video URLs in `DummyAtProtoFixtures.kt` to working remote sources while preserving deterministic ordering/cursors.
2. Implement `GraphFollow` rendering branch in `PostSummary.kt` record dispatch.
3. Update/extend tests (`DummyAtProtoTimelineControllerTest`, `PostSummaryTest`, and/or dummy login flow integration assertions) for the new fixture assumptions and follow rendering behavior.
4. Run targeted test suite for dummy endpoints and timeline card rendering.
5. Deploy with no config/data migration; behavior change is runtime-only in dummy-mode rendering.

Rollback strategy:
- Revert fixture URL and `GraphFollow` rendering changes in app code; controller/auth contract remains untouched so rollback is low-risk and code-only.

## Open Questions
