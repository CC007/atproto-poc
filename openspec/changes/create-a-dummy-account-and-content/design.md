# Design: Create a dummy account and content

## Context

This change enables reliable visual verification of all supported post types even when the real Bluesky timeline does not currently contain them, as described in `openspec/changes/create-a-dummy-account-and-content/proposal.md`.

Current state:
- Login flow authenticates against Bluesky and uses live data for timeline/detail rendering.
- The browse/detail rendering stack is already implemented server-side and should remain unchanged for normal users.
- Supported post-type rendering behavior is defined in the `PostSummary` component, but coverage depends on live data availability.

Constraints and stakeholders:
- The dummy account is intentionally accessible in production, because it is read-only and contains no confidential data.
- Dummy fixture responses must be available at runtime (not test-only), so they cannot live in `testFixtures`.
- For now, timeline behavior should remain single-page with no pagination/rendering model change.
- Media in dummy posts must support remote URLs, and image/video/GIF resources should be stable over time.
- Primary stakeholders are developers and reviewers validating browse/detail behavior across all supported post types.

## Goals / Non-Goals

**Goals:**
- Allow the existing login flow to authenticate a read-only dummy account without Bluesky authentication when the submitted network URL points at localhost.
- Reuse the same login form and submission flow as real Bluesky authentication.
- Detect the dummy account path from the entered network URL instead of adding separate dummy-only frontend controls.
- Serve a deterministic dummy timeline containing all post types currently supported by `PostSummary`.
- Ensure each dummy timeline item has a corresponding detail route that works with the current rendering flow.
- Keep existing page rendering and pagination behavior unchanged; only the backend source behind the authenticated account changes.
- Keep dummy content runtime-available and environment-safe, including production.
- Support stable remote media URLs in dummy fixtures for images, videos, and GIFs.

**Non-Goals:**
- Replacing or modifying the real Bluesky login flow for normal users.
- Introducing feature flags for dummy mode.
- Building fixture authoring UI or dynamic fixture generation.
- Redesigning browse/detail templates, route structure, or pagination mechanics.
- Simulating full network error behavior or complete API parity beyond what current render paths require.

## Decisions

### 1) Keep one authentication flow and identify the dummy account by network URL

**Decision:** Use the existing login form and authentication entrypoint for both real and dummy users. When the submitted network URL matches the reserved localhost value/pattern, the backend authenticates a dummy account instead of performing remote Bluesky authentication.

**Why:**
- Preserves a single login UX and avoids frontend branching.
- Keeps the dummy account opt-in through an explicit, user-entered network URL.
- Preserves existing real-user behavior while adding a deterministic read-only account for visual verification.

**Alternatives considered:**
- Add a dedicated "Login as dummy" button/route: rejected to avoid extra UI/control-flow branching.
- Add a checkbox/toggle for dummy behavior: rejected because the network URL already provides a natural selector.

### 2) Expose ATProto-compatible localhost controllers for auth and timeline

**Decision:** Implement the dummy account backend as valid Spring MVC controllers hosted in the same application on localhost, returning ATProto-compatible auth and timeline responses.

**Why:**
- Matches the requirement that fixture data be provided as real controller responses rather than test-only objects.
- Lets the application treat localhost as a real network endpoint contract for login and timeline retrieval.
- Minimizes special-casing in frontend auth/timeline handling because the response shape stays aligned with ATProto expectations.

**Alternatives considered:**
- Runtime fixtures stored only as Kotlin objects/resources behind custom branches: rejected because the user asked for valid Spring controllers returning ATProto-shaped data.
- External mock service: rejected due to unnecessary operational dependency.

### 3) Keep the localhost dummy network always available for now

**Decision:** The localhost dummy network should be available whenever the application runs; no runtime toggle or feature flag is needed for this change.

**Why:**
- The dummy account is read-only and non-confidential, so always-on availability is acceptable.
- Avoids adding configuration surface area before there is a demonstrated need.
- Matches the change scope: logging into the dummy account is the effective opt-in.

**Alternatives considered:**
- Add a config toggle now: rejected as out of scope and unnecessary for current risk level.

### 4) Keep deterministic fixtures in runtime application code backing the localhost controllers

**Decision:** Define deterministic dummy auth/timeline payloads in production runtime code/resources used by the localhost Spring controllers, versioned in the main app module.

**Why:**
- Dummy mode must function in production runtime.
- Keeps fixture data deterministic, reviewable, and deployable with the app artifact.

**Alternatives considered:**
- `testFixtures`: rejected because it is test-scoped and unavailable to production runtime.
- External hosted fixture service: rejected due to avoidable operational dependency and instability risk.

### 5) Reuse existing browse/detail rendering paths unchanged

**Decision:** Keep current page rendering/pagination behavior and only swap data origin for the dummy account's timeline/detail fetches.

**Why:**
- Matches requirement to avoid rendering/pagination changes.
- Validates the real rendering code paths using controlled data.
- Minimizes risk and diff size.

**Alternatives considered:**
- Separate dummy-specific templates/controllers: rejected because it duplicates UI logic and weakens confidence in real paths.
- Add synthetic pagination now: rejected because single-page behavior is currently sufficient.

### 6) Post-type coverage derived from `PostSummary`

**Decision:** Define fixture completeness by the set of supported post variants handled by `PostSummary`.

**Why:**
- `PostSummary` is the practical source of truth for timeline card type support.
- Ensures design intent maps directly to visible behavior and avoids under-coverage.

**Alternatives considered:**
- Ad hoc/manual fixture curation without reference list: rejected due to drift risk.
- Defining scope from external API catalogs: rejected because support is ultimately bounded by app rendering behavior.

### 7) Stable remote media strategy for image/video/GIF fixtures

**Decision:** Use remote URLs in fixtures, but only from stable, versioned, long-lived sources with ownership/control preferences and fallback replacements documented.

**Why:**
- Meets requirement to support remote URLs.
- Reduces breakage risk over time for visual regression/manual verification workflows.

**Alternatives considered:**
- Fully local bundled media only: rejected because requirement explicitly asks to handle remote URLs.
- Arbitrary third-party URLs: rejected due to high link rot and availability risk.

## Risks / Trade-offs

- [Network-URL-based dummy-account detection may conflict with real custom network configurations] -> Mitigation: reserve an explicit, documented localhost URL/pattern and validate with strict matching.
- [The localhost dummy controllers may drift from ATProto response expectations] -> Mitigation: keep controller payloads aligned to the ATProto contract currently consumed by the app and add focused tests on response shape (KBsky might help keeping them aligned).
- [Dummy fixture schema drifts from real mapping assumptions] -> Mitigation: keep fixtures aligned to existing domain mapping contracts and add lightweight tests validating required fields per post variant.
- [The localhost dummy account path introduces regressions in real login flow] -> Mitigation: centralize localhost URL detection and add focused tests for real-network vs localhost-network auth selection.
- [Production users may enter the dummy account unintentionally] -> Mitigation: keep the localhost network selector explicit and label the authenticated account/timeline clearly as dummy content.
- [Fixture maintenance overhead as new `PostSummary` variants are added] -> Mitigation: require fixture updates whenever post-type support changes and track this in change tasks/spec updates.

## Migration Plan

1. Keep the existing login form and implement strict localhost-network detection on submission.
2. Add localhost Spring controllers for auth and timeline responses that follow the ATProto shapes currently consumed by the app.
3. Define deterministic runtime fixture payloads backing those controllers.
4. Route authenticated localhost users through the dummy account path while keeping non-localhost networks on the existing real-auth path.
5. Reuse the existing browse/detail rendering pipeline unchanged for the dummy account timeline and detail navigation.
6. Seed dummy timeline/detail fixtures to cover all currently supported `PostSummary` post types.
7. Populate dummy media fields with stable remote URLs for image/video/GIF variants.
8. Add targeted verification for localhost URL detection, ATProto-compatible controller responses, real-vs-localhost auth selection, and dummy-account browse/detail rendering coverage.

Rollback:
- Disable/remove localhost dummy auth/timeline controllers and keep the normal network-auth path only.
- Keep fixture assets inert (unused) or remove in follow-up cleanup.
- No database/data migration rollback is required for this change as designed.

## Specifications

### Dummy Account Credentials
- **Username:** `dummy.localhost`
- **Password:** `1234`

### Network URL
- **Localhost network identifier:** `localhost` (no scheme or port required; recognized as a literal string in the login form's network selector)

### Controllers
- Dummy auth endpoint: responds to login requests targeting the localhost network
- Dummy timeline endpoint: serves deterministic fixture feed data for authenticated dummy accounts

## Open Questions

- None for this design revision. The current plan assumes a reserved localhost network URL, always-available localhost ATProto-compatible controllers, no URL health-check work in this change, and deterministic controller-backed fixture payloads in production runtime code.
