# Design: font-styling-with-kolo-styles

## Context

The proposal requires migrating font-related styling ownership from `CssController` page CSS into co-located Kolo utilities so typography intent lives next to rendered HTML elements.

Current state:
- `:libs:kolo-styles` already has a working utility pipeline (typed DSL -> token collection -> canonicalization -> parser hooks -> generator hooks -> `/css/generated/kolo.css`).
- Spacing and display utility families already use this pattern and are documented by existing OpenSpec capability specs (`kolo-utility-architecture`, `kolo-css-generation`, `kolo-html-runtime-integration`, `generated-page-stylesheets`, `kolo-spacing-ownership`).
- Font-family, font-size, and font-weight utilities are not yet implemented in Kolo, so typography still depends on centralized rules in `:app` `CssController`.

Constraints and stakeholders:
- Must preserve deterministic token canonicalization and permissive diagnostics behavior already defined for Kolo.
- Must remain Tailwind-compatible for the targeted font utility families (`font-family`, `font-size`, `font-weight`) per proposal references.
- Must support incremental migration while `kolo.css` and page CSS coexist.
- Stakeholders: app UI maintainers, `:libs:kolo-styles` maintainers, and reviewers validating typography parity.

This design uses the proposal as motivation/scope and aligns with existing OpenSpec capability requirements as the implementation contract baseline.

## Goals / Non-Goals

**Goals:**
- Add first-class Kolo support for font-family, font-size, and font-weight utilities through the same hook architecture used by spacing/display.
- Provide typed DSL helpers so font styling can be authored inside `kolo { ... }` blocks on render elements.
- Migrate font declarations from `CssController` to co-located Kolo usage for migrated selectors.
- Preserve rendered typography behavior during migration by keeping side-by-side stylesheet delivery.

**Non-Goals:**
- Introducing arbitrary-value font utilities (`[...]`) or custom token parsing beyond explicit support.
- Supporting custom size syntaxes like `text-[...]` or `text-(length:...)` in this change.
- Redesigning typography scale/tokens beyond the Tailwind-compatible set chosen for this change.
- Migrating unrelated style families (color, line-height, letter-spacing, text-align, etc.) unless already required by a touched selector.
- Changing Kolo URL canonicalization, cache strategy, or route structure.

## Decisions

### 1) Add a dedicated font utility family using parser/generator hooks
**Decision:** Implement font support in `:libs:kolo-styles` as typed compiler tokens plus dedicated parser/generator hooks (parallel to spacing/display), instead of embedding font handling into existing hooks.

**Rationale:** Keeps utility families isolated, testable, and consistent with existing extensibility contracts.

**Alternatives considered:**
- Extend `Display*` or `Spacing*` hooks for font tokens: rejected due to mixed concerns and harder maintenance.
- Hardcode font generation in `KoloCssCompiler`: rejected because it bypasses the established plugin architecture.

### 2) Provide typed font DSL helpers for `KoloScope` and `KoloVariantScope`
**Decision:** Add typed helpers under `kolostyles.dsl.font` for family/size/weight utilities and emit canonical Tailwind-style tokens.

**Rationale:** Co-located typed APIs reduce string-typing mistakes and match existing Kolo ergonomics.

**Alternatives considered:**
- Use only raw token recording APIs: rejected because typo risk is high and discoverability is low.
- Single string-valued `font(...)` helper: rejected due to weak type safety.

### 3) Support explicit allow-lists for font tokens with Tailwind-compatible semantics
**Decision:** Parse and generate only supported font-family/font-size/font-weight tokens from explicit allow-lists, including existing variant prefixes; unsupported/malformed tokens remain diagnostics. For font-size, phase 1 support is the full Tailwind text scale from `text-xs` through `text-9xl`. For font-family, use Tailwind-only named families (for example `font-sans`, `font-serif`, `font-mono`) with no project-specific family aliases. For font-weight, phase 1 support is exactly `font-thin`, `font-extralight`, `font-light`, `font-normal`, `font-medium`, `font-semibold`, `font-bold`, `font-extrabold`, and `font-black` (no custom numeric or arbitrary weights).

**Rationale:** Deterministic behavior and explicit compatibility are more stable than pattern-based permissive parsing.

**Alternatives considered:**
- Generic pattern parsing for any `text-*` / `font-*`: rejected due to ambiguity and accidental acceptance.
- Fail hard on unsupported tokens: rejected because current compiler contract is permissive with diagnostics.

### 4) Migrate typography ownership incrementally per selector/component
**Decision:** Move font declarations from page CSS to Kolo DSL in small batches; remove only declarations already represented by equivalent Kolo utilities.

**Rationale:** Reduces regression risk and follows established migration practice from spacing/display ownership transfer.

**Alternatives considered:**
- Big-bang migration of all font rules: rejected due to high breakage/debug risk.
- Long-term dual ownership in both Kolo and page CSS: rejected due to drift and precedence ambiguity.

### 5) Keep variant behavior identical to existing utility families
**Decision:** Font utilities reuse the same pseudo/media variant chaining behavior already used by spacing/display tokens.

**Rationale:** Uniform behavior across utility families avoids surprise at call sites and reuses proven runtime/compiler behavior.

**Alternatives considered:**
- Base-only font utilities: rejected because it would fragment the DSL model and limit migration.
- New variant classes specific to typography: rejected as out-of-scope expansion.

### 6) Redefine the current default font as Tailwind sans via `--font-sans`
**Decision:** The current app default font style is redefined to the Tailwind sans stack by routing it through the `--font-sans` CSS variable (i.e., default body/base typography aligns with `font-sans` semantics).

**Rationale:** This preserves a single family baseline during migration while keeping family ownership within Tailwind-compatible named tokens.

**Alternatives considered:**
- Keep existing bespoke default stack unchanged: rejected because it conflicts with strict Tailwind-only named family policy.
- Introduce project alias tokens for the current default stack: rejected as out-of-scope for this change.

## Risks / Trade-offs

- [Tailwind token coverage gaps vs existing app typography rules] -> Keep unmatched rules in `CssController` as temporary exceptions and track explicit follow-up migration.
- [Selector precedence changes when moving declarations into utility classes] -> Migrate per selector and remove old declarations only after equivalent rendered output is confirmed.
- [Ambiguity around which Tailwind font tokens are in-scope first] -> Freeze a documented allow-list in code/tests for family/size/weight before broad migration.
- [Increased maintenance surface in parser/generator code] -> Isolate font family implementation and add focused parser/generator/runtime tests.
- [Temporary mixed ownership complexity] -> Keep migration batches small and bounded to touched routes/components.

## Migration Plan

1. Define the initial Tailwind-compatible token allow-list for `font-family`, `font-size`, and `font-weight` supported by this change, with full `text-xs`..`text-9xl` size coverage, Tailwind-only named family tokens, and font-weight allow-list coverage from `font-thin` through `font-black` only.
2. Implement typed font token model + parser hook + generator hook in `:libs:kolo-styles` and register via existing Spring hook wiring, including explicit font-weight token-to-CSS mapping (`font-thin`->`100`, `font-extralight`->`200`, `font-light`->`300`, `font-normal`->`400`, `font-medium`->`500`, `font-semibold`->`600`, `font-bold`->`700`, `font-extrabold`->`800`, `font-black`->`900`).
3. Implement typed render DSL helpers for `KoloScope` and `KoloVariantScope` that map one-to-one to canonical token strings.
4. Add/extend tests for parser acceptance/rejection, generator emission, mixed utility compilation, and runtime href/class behavior.
5. Redefine the existing app default font-family declaration to use the Tailwind sans baseline through `--font-sans`.
6. Migrate font declarations from `CssController` to `kolo { ... }` call sites incrementally (component/selector batches).
7. Remove migrated font declarations from generated page CSS while keeping non-migrated rules as explicit temporary exceptions.
8. Deploy with existing side-by-side stylesheet strategy (`kolo.css` + page CSS).
9. Rollback strategy: reintroduce removed `CssController` font declarations for affected selectors and remove corresponding migrated Kolo font tokens until parity issues are resolved.
