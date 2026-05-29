# Design: Move over padding and margin definitions from CssController to kolo styles

## Context

The proposal for this change moves margin/padding declarations out of centralized page CSS in `CssController` and into co-located `kolo { ... }` utility usage.

Current state:
- `:app` `CssController` still owns generated page stylesheets (`/css/generated/browse.css`, `/css/generated/art.css`) and includes spacing declarations.
- `:libs:kolo-styles` already provides typed spacing utilities (`m`, `mt`, `mx`, `p`, `px`, etc.), parser/generator hooks, canonical token generation, and `/css/generated/kolo.css` delivery.
- Rendering code can already attach spacing semantics at element level using `kolo { mt(2); px(0) }`.

Constraints and stakeholders:
- Must preserve existing page rendering behavior and visual parity while migrating styling responsibility.
- Must remain consistent with D-007 (incremental migration, deterministic token canonicalization, side-by-side CSS during transition).
- Affects both module boundaries (`:app` and `:libs:kolo-styles`) and runtime CSS generation flow.
- Stakeholders: UI maintainers, `kolo-styles` maintainers, and reviewers validating no layout regression.

Requirement references:
- Motivation and target behavior come from this change's `proposal.md`.
- There are currently no capability spec files under `openspec/specs/` or this change's `specs/`; this design treats the proposal as authoritative scope for now.

## Goals / Non-Goals

**Goals:**
- Make `kolo-styles` the single source of truth for spacing utilities used by page markup.
- Remove margin/padding declarations from `CssController` generated stylesheets once equivalent utility usage is in place.
- Preserve current visual behavior on browse and art routes while performing migration incrementally.
- Keep compatibility with existing Kolo token parsing/generation contracts and caching/canonicalization behavior.

**Non-Goals:**
- Rewriting non-spacing style rules in `CssController` during this change.
- Introducing new utility families beyond spacing (e.g., typography, color, sizing redesign).
- Altering route structure, controller responsibilities outside styling, or Kolo URL/caching protocol.
- Adding arbitrary value token syntax (`[...]`) support.

## Decisions

### 1) Spacing definitions live in `:libs:kolo-styles`, not `CssController`
**Decision:** Spacing behavior is authored via Kolo spacing DSL usage in render code and generated through `SpacingParserHook`/`SpacingGeneratorHook`; `CssController` no longer emits spacing declarations.

**Rationale:** This aligns implementation with the co-located styling architecture and avoids dual maintenance of equivalent spacing rules.

**Alternatives considered:**
- Keep spacing in both places temporarily long-term: rejected because it creates drift risk and unclear ownership.
- Keep `CssController` as primary and Kolo as optional sugar: rejected because it undermines module intent and reusability goals.

### 2) Migrate incrementally with visual parity checks
**Decision:** Convert spacing declarations page-by-page/component-by-component, then delete matching declarations from `CssController` only after equivalent Kolo tokens are present.

**Rationale:** Matches D-007 incremental migration guidance and lowers regression risk compared with a full cutover.

**Alternatives considered:**
- Big-bang replacement of all spacing styles: faster initially but high regression/debug risk.
- Leave old rules indefinitely as fallback: safe short-term but retains duplication and blocks objective completion.

### 3) Preserve existing spacing scale and utility API shape
**Decision:** Use existing typed spacing API (`m`, `mt`, `mx`, `p`, `px`, etc.) and token catalog semantics; do not introduce new numeric scales or naming.

Spacing conversion rule during migration:
- `m*` and `p*` utilities use a `0.25rem` step scale.
- When existing CSS values are not exact quarter-rem matches, map to the nearest available step to preserve visual parity as closely as possible.
- Examples: `margin-top: 0.30rem` -> `mt(1)` (`0.25rem`), `padding-left: 1.47rem` -> `pl(6)` (`1.50rem`).

**Rationale:** Avoids accidental visual shifts and limits change scope to ownership migration, not design-system expansion.

**Alternatives considered:**
- Introduce new spacing scale now: rejected as a separate product/design decision.
- Use raw CSS utility tokens directly in strings: rejected because typed DSL provides safer authoring and discoverability.

### 4) Validate parity through focused tests plus targeted manual checks
**Decision:** Extend/adjust spacing and controller tests to assert spacing ownership boundaries (utility generation present, `CssController` spacing rules absent) and run route-level spot checks for browse/art output.

**Rationale:** This change is architectural ownership transfer; tests should detect duplicate/removed spacing definitions early.

**Alternatives considered:**
- Manual visual review only: rejected due to low repeatability.
- Snapshot every full HTML/CSS output: rejected as high-maintenance for limited incremental value.

## Risks / Trade-offs

- [Missing Kolo token coverage for a previously centralized spacing rule] -> Add/confirm equivalent spacing utility usage before deleting each rule; gate removal with focused tests.
- [Temporary coexistence of old and new spacing paths causes duplicate declarations] -> Remove declarations in small batches and verify CSS precedence for touched components.
- [Token canonicalization or ordering mismatch changes generated class names/URLs] -> Reuse existing compiler canonicalization flow and keep tests around canonical output stability.
- [Migration touches both `:app` and `:libs:kolo-styles`, increasing coordination cost] -> Keep edits narrow per step and review module boundary changes explicitly.
- [No explicit OpenSpec capability specs to anchor requirement language] -> Treat proposal as source of truth for now and add/align specs if this workflow begins enforcing capability-level requirements.

## Migration Plan

1. Inventory current margin/padding declarations in `CssController`-owned generated styles and map each to corresponding render locations.
2. Convert each declaration to spacing DSL tokens using the nearest `0.25rem` step (`m*`/`p*`) when values are not exact matches.
3. Add or confirm equivalent Kolo spacing DSL usage (`kolo { ... }`) in relevant HTML render paths.
4. Remove only the migrated spacing declarations from `CssController` stylesheets.
5. Run targeted tests for spacing utilities/compiler/controller behavior and route-level checks for browse/art output.
6. Verify generated CSS requests still include required `kolo` tokens and visual layout remains stable.
7. Rollback strategy (if regressions appear): restore removed `CssController` spacing declarations for affected sections and re-run tests while gaps are corrected.

## Open Questions

- Should this change also add OpenSpec capability specs for styling ownership, given `openspec/specs/` is currently empty?
- Is the intended end state to remove all spacing declarations from all `CssController` stylesheets immediately, or only for browse/art scope first?
- Do we want an automated regression check that scans generated page CSS for forbidden spacing properties to enforce the boundary long-term?
