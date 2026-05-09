# BA-016: Co-located tailwind-like styling architecture and design

## Metadata
- ID: `BA-016`
- Status: `completed`
- Owner: `ai`
- Created: `2026-05-06 20:15`
- Updated: `2026-05-09 09:15`
- Related Human Issue: none

## Goal
Design the architecture for a Kotlin-first, tailwind-like, co-located styling approach that balances utility-first speed with maintainability in server-rendered `kotlinx.html` views.

## Scope
- In scope:
  - Produce an architecture proposal for co-located styling in BlueArt.
  - Evaluate tradeoffs vs centralized stylesheets, using https://digitalbiztalk.com/article/tailwind-css-vs-inline-styles-the-full-circle-debate as inspiration input.
  - Define boundaries between app rendering, style declarations, style compilation, and runtime delivery.
  - Identify required decisions and risks before implementation tasks (`BA-017` to `BA-019`).
- Out of scope:
  - Shipping production style utilities.
  - Large-scale style migration.

## Architecture Decisions
- Developer API shape:
  - Styles are declared directly on elements with `kolo { ... }`.
  - Zero-arg utilities use bare properties (`flex`), parameterized utilities use functions (`mt(2)`, `px(4)`).
  - API supports both concise single-line and multi-line grouped usage.
- Token and naming model:
  - Use Tailwind-style utility token names.
  - Include pseudo-class and media-query variants.
  - Do not support arbitrary value tokens (`[...]`) yet.
- Runtime delivery model:
  - Serve utilities from one endpoint: `/css/generated/kolo.css`.
  - Keep `kolo.css` and existing page stylesheets side-by-side during migration; remove only migrated utility-covered declarations from page CSS.
  - Use query params `version` (build-derived git SHA) and `kolo` (single semicolon-separated token list).
  - `kolo` list is canonicalized with variant-aware sorting and deduplication.
  - Disallow separators inside tokens for now (`;` reserved as list delimiter).
- Caching strategy:
  - Cache by versioned URL (`version` + canonical `kolo`) with immutable-friendly behavior.
  - `ETag` is optional and not required for the initial design.

## Canonical `kolo` Ordering Spec
- Input contract:
  - `kolo` is one semicolon-separated list: `kolo=tokenA;tokenB;tokenC`.
  - Tokens follow Tailwind-style naming and variant notation (`hover:bg-sky-500`, `md:mt-2`).
  - Arbitrary value tokens (`[...]`) are not supported yet.
  - `;` is reserved as list delimiter and cannot appear inside a token.
- Canonicalization algorithm:
  1. Split by `;`, trim, and drop empty entries.
  2. Reject invalid tokens (`;` present in token, or `[`/`]` present).
  3. Deduplicate exact token strings.
  4. For each token, compute a sort key:
     - `group`: `0` when token has no `:` variant prefix, else `1`.
     - `baseUtility`: substring after the last `:`.
     - `variantCount`: number of `:` separators.
     - `variantChain`: substring before the last `:` (empty for group `0`).
     - `token`: full token.
  5. Sort ascending by `(group, variantCount, variantChain, baseUtility, token)`.
  6. Join using `;` to produce canonical `kolo` value.
- Example:
  - Input: `hover:px-4;mt-2;md:mt-2;px-4;hover:mt-2;flex`
  - Canonical: `flex;mt-2;px-4;hover:mt-2;hover:px-4;md:mt-2`
  - URL: `/css/generated/kolo.css?version=<git-sha>&kolo=flex;mt-2;px-4;hover:mt-2;hover:px-4;md:mt-2`

## Render-Time Link Emission Considerations
- Problem:
  - `kolo { ... }` calls can appear deep in body/component rendering, while the `<link rel="stylesheet">` tag is usually emitted earlier in the document head.
  - The final `/css/generated/kolo.css?...` URL must include every Kolo token used during that request.
- Recommended approach:
  - Use a request-scoped Kolo render context that collects tokens from every `kolo { ... }` call.
  - Prefer a two-pass render flow:
    1. Render the page/components while collecting Kolo tokens.
    2. Canonicalize the full token set.
    3. Emit the final document `<head>` with the completed `kolo.css?version=...&kolo=...` URL.
- Why this is preferred:
  - Avoids resolving the stylesheet URL before all Kolo declarations are known.
  - Keeps URL generation deterministic and aligned with the canonical ordering contract.
  - Avoids brittle post-processing of already-rendered HTML.
- Acceptable fallback (if needed temporarily):
  - Render with a placeholder stylesheet URL and replace it after token collection.
  - This is less preferred than two-pass rendering because it relies on string/template replacement.
- Data owned by the request-scoped context:
  - collected token set
  - canonicalization helper
  - final `kolo.css` href builder using `version` + canonical `kolo`
- Migration note:
  - This affects Kolo utility stylesheet emission only; existing page stylesheet links still remain alongside `kolo.css` during migration.

## Plan
- [x] Review current styling flow (`CssController`, generated stylesheet endpoints, class usage in renderers).
- [x] Document desired developer experience for co-located utility usage in Kotlin.
- [x] Define architecture options and choose one with rationale.
- [x] Record decisions in `docs/DECISIONS.md` (or ADR) and update `docs/ARCHITECTURE.md`.
- [x] Produce implementation checklist for `BA-017`, `BA-018`, and `BA-019`.
- [x] Split former `BA-018` implementation scope into `BA-021` (endpoint generation) and `BA-022` (DSL/class/link wiring).

## Progress Log
- `2026-05-06 20:15`: Task created to de-risk tailwind-like styling implementation.
- `2026-05-09 00:00`: Locked API and delivery decisions with user: `kolo {}` element DSL, Tailwind-style tokens, single `/css/generated/kolo.css` endpoint, and `version` + semicolon-separated `kolo` URL contract.
- `2026-05-09 00:10`: Locked migration mode to side-by-side `kolo.css` + page CSS and added canonical variant-aware token ordering spec.
- `2026-05-09 00:20`: Synced architecture-level docs with accepted BA-016 decisions in `docs/DECISIONS.md` (`D-007`) and `docs/ARCHITECTURE.md`.
- `2026-05-09 00:25`: Adjusted canonical sort-key precedence to prioritize variant grouping readability: `(group, variantCount, variantChain, baseUtility, token)`.
- `2026-05-09 00:35`: Documented request-scoped token collection and recommended two-pass render flow for correct `kolo.css` link emission.
- `2026-05-09 02:10`: Validation completed (`./gradlew test` passed) and task marked complete after user confirmed the current architecture is sufficient.
- `2026-05-09 09:15`: Follow-up execution was split by concern; superseded `BA-018` with `BA-021` and `BA-022`.

## How Completed
- Recorded the accepted Kolo styling architecture directly in this task file, including:
  - `kolo { ... }` DSL shape
  - Tailwind-style token naming and variant support boundaries
  - `/css/generated/kolo.css` delivery contract with `version` + `kolo`
  - canonical variant-aware token ordering
  - request-scoped token collection and two-pass render guidance
- Synced the accepted architecture into:
  - `docs/DECISIONS.md` via `D-007`
  - `docs/ARCHITECTURE.md`
  - `docs/ai-tasks/2026-05-06-BA-018-type-safe-style-collection-parsing-application-framework.md`
  - `docs/ai-tasks/2026-05-09-BA-021-kolo-css-endpoint-generation-from-parameters.md`
  - `docs/ai-tasks/2026-05-09-BA-022-kolo-extension-link-class-generation.md`
  - `docs/ai-tasks/2026-05-06-BA-019-margin-padding-elements-csscontroller-cleanup-visual-parity.md`
  - `docs/AI_TASKS.md`
- Completed after the user confirmed the current architecture is sufficient for now and can be reopened later if needed.

## Verification
- Ran:

```bash
./gradlew test
```

- Result: passed.
- Note: test run produced shutdown-time warnings about missing `EVENT_PUBLICATION` table during bean destruction, but the build finished successfully and these warnings were pre-existing/non-blocking for this documentation task.

## Follow-ups
- [ ] `BA-021`: Implement the CSS endpoint path that generates utility stylesheet output from canonical request parameters.
- [ ] `BA-022`: Implement `kolo {}` API wiring, class generation, and stylesheet link emission.

