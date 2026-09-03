# Design: migrate-layout-styling-to-kolo

## Context

This change continues the existing ownership migration from `CssController` page CSS to typed Kolo utilities (`:libs:kolo-styles`) for browse/art rendering.

Current state:
- Spacing, display, font, and sizing already run through the Kolo pipeline (`kolo { ... }` → canonical token list → `/css/generated/kolo.css` via parser/generator hooks).
- `CssController` still owns several layout declarations across browse/art (`box-sizing`, `overflow-*`, `position`, `top`, `z-index`, `object-fit`) plus some non-Tailwind-compatible rules.
- Kolo variant support is currently state variants + min-width breakpoints only; max-width responsive overrides still require page CSS exceptions.

Constraints and stakeholders:
- Must preserve visual parity while migrating ownership incrementally.
- Must stay compatible with existing deterministic canonicalization and permissive unsupported-token diagnostics.
- Affects both `:libs:kolo-styles` (new utility support) and `:app` render sites/CSS cleanup.
- Stakeholders: UI maintainers, Kolo module maintainers, and reviewers validating no layout regression.

References:
- Motivation/source scope: `openspec/changes/migrate-layout-styling-to-kolo/proposal.md`
- Requirement baselines likely impacted:
  - `openspec/specs/kolo-utility-architecture/spec.md`
  - `openspec/specs/kolo-css-generation/spec.md`
  - `openspec/specs/kolo-html-runtime-integration/spec.md`
  - `openspec/specs/generated-page-stylesheets/spec.md`

## Goals / Non-Goals

**Goals:**
- Add Tailwind layout utilities (from aspect-ratio through z-index) that are adjacent to layout declarations currently used in `CssController`, and implement full variant coverage for each used property family.
- Treat display styling as part of layout taxonomy by relocating existing display support into the layout subpackage without changing display behavior.
- Group layout styling into coherent subpackages by concern where it improves clarity, while keeping small utility groups in shared layout-level files.
- Keep layout token parsing/generation in the same typed hook architecture used by existing utility families.
- Provide typed Kotlin DSL helpers on `KoloScope` and `KoloVariantScope` for migrated layout tokens.
- Migrate mappable browse/art layout declarations from `CssController` to co-located Kolo usage and remove duplicated page-CSS declarations.
- Preserve existing behavior by keeping non-mappable or unsupported-responsive rules as explicit exceptions.

**Non-Goals:**
- Full Tailwind layout-page parity in one step when utilities are not used by the app.
- Arbitrary-value and CSS-variable token forms for any Kolo utility family (existing or newly added), including `[...]` and `(--some-variable)` forms.
- Changes to canonicalization ordering, URL shape, cache strategy, or route structure.
- Reworking unrelated visual styles (colors, borders, shadows, typography scale design, etc.).

## Decisions

### 1) Add a dedicated layout utility family in `:libs:kolo-styles`
**Decision:** Introduce a `layout` utility family (typed token model + parser hook + generator hook + DSL helpers), implemented with the same contracts as existing utility families.

**Rationale:** Keeps migration aligned with existing Kolo architecture and avoids one-off logic inside `CssController` or `KoloCssCompiler`.

**Alternatives considered:**
- Add logic directly in `KoloCssCompiler`: rejected (breaks hook extensibility pattern).
- Spread layout support across ad-hoc updates in existing families: rejected (unclear ownership and higher coupling).

### 1a) Keep display behavior as-is, but move it under layout subpackages
**Decision:** Reclassify display as layout by moving current display parser/generator/DSL code to `layout` subpackages, while preserving token set, generated CSS output, variants, and public behavior.

**Rationale:** Tailwind documents display under layout, and relocation gives a coherent package model without taking behavior-change risk in this change.

**Alternatives considered:**
- Rework display behavior during relocation: rejected because it mixes taxonomy refactor with functional change and increases regression risk.
- Exclude display and treat it as a separate migration track: rejected because it leaves layout ownership split and conflicts with Tailwind’s layout categorization.

### 1b) Organize layout styling into concern-based subpackages
**Decision:** Use a hybrid structure under layout:
- keep major/cohesive concerns in subpackages (for example `layout.display` and `layout.offset` for `inset/top/right/bottom/left`)
- keep smaller utility groups in shared layout-level DSL/parser/generator files instead of forcing one subpackage per tiny concern

**Rationale:** This preserves discoverability and related grouping (especially positional offsets), while avoiding unnecessary package sprawl for very small concerns.

**Alternatives considered:**
- Put all layout tokens in one flat package: rejected due to poor discoverability as scope grows.
- Force every concern into its own subpackage: rejected due to package churn and overhead for tiny utility sets.
- Split by CSS property only without grouping semantics: rejected because it fragments tightly-related positional behavior.

### 2) Use a usage-driven allow-list with full variants for used families
**Decision:** Support layout tokens based on actual current usage in `CssController`. For each used layout family, implement its full Tailwind-style variant set in Kolo (within current parser constraints), rather than only a minimal subset. Specifically:
- `box-sizing` (`box-border`, `box-content`)
- `overflow` (full overflow variants, including axis variants)
- `position` (`static|relative|absolute|fixed|sticky`)
- `inset/top/right/bottom/left` (full offset variants)
- `z-index` (`z-auto` plus `z-<n>` where `n` is any positive integer)
- `object-fit` (full object-fit variants)

Do not add unused layout families yet (for example `aspect-ratio`, `break-after`).

**Rationale:** Usage-driven scope keeps implementation focused, while full variants on used families avoid follow-up churn and partial coverage gaps.

**Alternatives considered:**
- Parse arbitrary `layout-*` patterns: rejected (ambiguous and risky).
- Implement every Tailwind layout utility immediately: rejected because unused families add scope without near-term value.
- Add only the exact values currently encountered: rejected because it leads to fragmented support and repeated follow-up changes for the same family.

### 2a) Allow unbounded positive integer z-index tokens
**Decision:** Accept `z-<n>` for any positive integer `n` (for example `z-1`, `z-10`, `z-999`) instead of a fixed z-index allow-list.

**Rationale:** `z-index` often requires app-specific stacking values, and restricting to a tiny fixed list adds avoidable friction without improving safety meaningfully.

**Alternatives considered:**
- Keep only a fixed Tailwind-style z-index set: rejected because it would force frequent follow-up token additions for legitimate stacking needs.
- Allow all integers including negatives: rejected because this request explicitly targets positive integers and positive-only keeps parser behavior narrower.
- Support bracket/CSS-var forms for z-index: rejected to preserve the established Kolo-wide rule that `[...]` and `(--some-variable)` token forms are out of scope across all utility families.

### 3) Centralize shared media-variant emission in `StyleGeneratorHook`
**Decision:** Refactor repeated generator scaffolding for media variant handling into shared support on `StyleGeneratorHook` (or its shared base helper), so individual generator implementations only provide selector/declaration specifics.

**Rationale:** Existing generator implementations repeat near-identical media wrapping logic, which increases duplication and drift risk. Centralizing this keeps behavior consistent and reduces per-family boilerplate.

**Alternatives considered:**
- Keep media handling duplicated in each generator: rejected due to avoidable repetition and maintenance overhead.
- Move media handling into `KoloCssCompiler`: rejected because media emission belongs to generation concerns, not compile-loop orchestration.

### 4) Keep responsive max-width exceptions in page CSS until variant model expands
**Decision:** Continue using `CssController` for layout behavior that depends on max-width media overrides not representable with current Kolo variants.

**Rationale:** Current Kolo variants are min-width only; forcing max-width behavior into today’s model would either break parity or expand scope into variant-system redesign.

**Alternatives considered:**
- Introduce max-width variants in this change: rejected (separate cross-cutting capability).
- Remove max-width behavior during migration: rejected (visual regression risk).

### 5) Migrate declaration ownership selector-by-selector with explicit exceptions
**Decision:** For each layout declaration in `CssController`, migrate only when a clear Tailwind-compatible utility exists; leave non-mappable values and browser-specific patterns (for example `display: -webkit-box`) as documented `kolo-exception` rules.

**Rationale:** Maintains layout parity and keeps migration auditable.

**Alternatives considered:**
- Big-bang rewrite of all layout CSS: rejected (high blast radius).
- Keep dual ownership long-term: rejected (drift and maintenance burden).

## Risks / Trade-offs

- **[Layout token surface grows quickly]** → Mitigation: keep scope usage-driven by family; avoid adding entirely unused families.
- **[Max-width responsive behavior cannot move to Kolo yet]** → Mitigation: keep explicit page-CSS exceptions and tag them for follow-up capability work.
- **[Ownership split may create duplicate/conflicting declarations during transition]** → Mitigation: migrate selector-by-selector and remove old declarations in the same step.
- **[Token/value mapping mistakes (e.g., `top`, `z-index`, `object-position`)]** → Mitigation: add parser/generator tests for every supported token, including positive-integer `z-<n>` coverage, and mixed-family compiler outputs.
- **[Display relocation breaks imports/wiring despite no intended behavior changes]** → Mitigation: keep migration mechanical (move + import updates), preserve existing tests, and assert generated display CSS parity before/after move.
- **[Shared media refactor could subtly change emitted CSS structure]** → Mitigation: lock parity with before/after generator tests across spacing/display/font/sizing and new layout utilities.
- **[Visual shifts from moving layout classes into render sites]** → Mitigation: preserve prior CSS as rollback path and keep migration incremental.

## Migration Plan

1. Inventory layout declarations currently emitted by `CssController` and map each to render call sites (`Header`, browse/art controllers, shared components).
2. Move existing display DSL/compiler code into `layout` subpackages with behavior parity (same tokens, variants, and emitted CSS).
3. Refactor shared media-variant emission scaffolding into `StyleGeneratorHook` shared support and migrate existing generator implementations (spacing/display/font/sizing) to use it without behavior changes.
4. Implement additional `layout` compiler support in `:libs:kolo-styles` (typed token model, parser hook, generator hook, Spring registration) for non-display layout utilities, with full variant support for currently used families.
5. Add typed DSL helpers with a hybrid structure: use `kolostyles.dsl.layout.*` subpackages for major concerns, and keep minor concerns in shared `kolostyles.dsl.layout` files (including grouped inset/offset helpers for `top`/`right`/`bottom`/`left`).
6. Add/extend tests in `:libs:kolo-styles` for relocation parity, shared media-emission parity, parsing, generation, full-variant behavior for used families, and mixed utility-family compilation.
7. Migrate app render sites to layout DSL helpers where equivalent tokens exist; remove matching declarations from `CssController`.
8. Keep remaining non-mappable and max-width-dependent declarations in `CssController` with explicit `kolo-exception` markers.
9. Extend `CssControllerTest` ownership assertions to include migrated layout properties and preserved exceptions.
10. Deploy under existing dual-stylesheet architecture (`kolo.css` + page CSS).  
   Rollback: restore removed `CssController` declarations for affected selectors and remove corresponding layout tokens from render sites.
