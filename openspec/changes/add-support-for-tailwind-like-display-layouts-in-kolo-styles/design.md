# Design: add-support-for-tailwind-like-display-layouts-in-kolo-styles

## Context

`kolo-styles` already supports typed spacing utilities end-to-end (DSL token recording, canonicalized `kolo` query generation, parser/generator hook compilation, and CSS delivery through `/css/generated/kolo.css`). Display behavior is still authored outside this utility pipeline, which forces mixed styling ownership and prevents co-located display intent in `kolo { ... }`.

The proposal requires Tailwind-like display layouts from the Tailwind display reference to be available through `kolo-styles`, so display utilities can be authored in the same path as existing spacing utilities. This change must preserve existing Kolo contracts:

- deterministic token canonicalization and URL shape
- tolerant compiler behavior (`unsupported` / `unparsed` diagnostics instead of hard failures)
- hook-driven parser/generator extensibility
- no behavior regression for existing spacing and render-time APIs

Primary stakeholders are feature developers authoring server-rendered UI, maintainers of `:libs:kolo-styles`, and reviewers depending on deterministic CSS output and testability.

## Goals / Non-Goals

**Goals:**
- Add first-class display utility support to the existing Kolo compiler hook architecture.
- Provide typed DSL helpers for display utilities on both `KoloScope` and `KoloVariantScope`.
- Support the Tailwind display utility set from `https://tailwindcss.com/docs/display` through `kolo.css`:
  `block`, `inline`, `inline-block`, `flow-root`, `flex`, `inline-flex`, `grid`, `inline-grid`, `contents`, `list-item`, `hidden`, `table`, `inline-table`, `table-caption`, `table-cell`, `table-column`, `table-column-group`, `table-header-group`, `table-row-group`, `table-row`, `table-footer-group`.
- Keep state/media variant behavior consistent with current spacing utilities (`hover`, `focus`, `focus-visible`, `active`, `visited`, and `sm`/`md`/`lg`/`xl`/`2xl` min-width variants).
- Migrate current app display declarations from page/static CSS into Kolo display utilities where equivalent coverage exists, preserving rendered behavior.
- Preserve backward compatibility for existing tokens and endpoint/runtime behavior.

**Non-Goals:**
- Introducing arbitrary value display utilities or custom display values.
- Implementing non-display accessibility helpers mentioned on that page (`sr-only`, `not-sr-only`), which belong to visibility/accessibility capability scope.
- Changing canonicalization ordering rules, endpoint URLs, or cache strategy.
- Adding new runtime rendering infrastructure beyond current `renderKoloHtml` + `koloStylesheetLink()` flow.

## Decisions

### 1) Implement display support as a new parser/generator hook pair
**Decision:** Add `DisplayParserHook`, `DisplayGeneratorHook`, and a typed `DisplayToken` under `kolostyles.compiler.display`, registered as Spring components like spacing hooks.

**Rationale:** This matches the established extension architecture and keeps each utility family isolated, testable, and composable.

**Alternatives considered:**
- Extend spacing hooks to also parse display tokens. Rejected because it conflates utility domains and increases coupling.
- Add display handling directly in `KoloCssCompiler`. Rejected because it breaks the parser/generator plug-in boundary already codified in specs.

### 2) Provide typed display DSL helpers rather than relying on raw `recordBase(...)`
**Decision:** Add DSL helpers in `kolostyles.dsl.display` for both `KoloScope` and `KoloVariantScope` (for example property-style helpers like `flex`, `block`, `inlineFlex`, `hidden`, `tableRow`, etc.), each recording the canonical token string.

**Rationale:** Typed helpers reduce token typos, keep call sites readable, and mirror the ergonomics used by spacing DSL.

**Alternatives considered:**
- Use only `recordBase("flex")` style raw tokens. Rejected because it weakens type safety and discoverability.
- Use a single `display(value: String)` API. Rejected because stringly-typed values permit invalid tokens at call sites.

### 3) Support the documented Tailwind display token set explicitly (no fallback parsing)
**Decision:** Parser accepts only the exact utility tokens documented on the Tailwind display page (including table variants and `inline-table`) plus existing variant prefixes; unknown display-like strings remain unsupported diagnostics.

**Rationale:** Explicit allow-list parsing keeps behavior deterministic and aligns with current Kolo strategy of tolerant output plus diagnostics for unsupported tokens.

**Alternatives considered:**
- Parse any token matching generic patterns. Rejected due to ambiguity and accidental acceptance of unintended utilities.
- Fail request on unknown tokens. Rejected because current endpoint contract is permissive and diagnostic-oriented.

### 4) Keep variant parsing semantics aligned with spacing
**Decision:** Reuse the same state/media variant rules and selector/media emission pattern currently used for spacing tokens.

**Rationale:** Developers get consistent behavior across utility families; implementation and tests can reuse established patterns.

**Alternatives considered:**
- Limit display utilities to base tokens only. Rejected because it creates inconsistent DSL/compiler behavior versus spacing.
- Add new variant types in this change. Rejected to keep scope focused and avoid cross-capability expansion.

### 5) Use explicit Kotlin DSL naming that maps one-to-one to Tailwind tokens
**Decision:** Expose strongly-typed Kotlin helpers with predictable mapping for hyphenated tokens (e.g., `inlineBlock -> inline-block`, `inlineFlex -> inline-flex`, `flowRoot -> flow-root`, `tableRowGroup -> table-row-group`), while keeping emitted token values exactly Tailwind-compatible.

**Rationale:** This keeps call sites idiomatic Kotlin while preserving stable token semantics for canonicalization and CSS generation.

**Alternatives considered:**
- Keep only raw token recording APIs. Rejected due to typo risk and weaker discoverability.
- Add multiple aliases per token. Rejected to avoid API ambiguity and long-term maintenance overhead.

## Risks / Trade-offs

- **[Token/API naming mismatch between Kotlin DSL and Tailwind token names]** → Define a clear one-to-one mapping table in tests (e.g., `inlineFlex -> "inline-flex"`) and lock it with unit assertions.
- **[Broader token support increases parser/generator maintenance surface]** → Keep display support isolated in its own package with targeted parser/generator integration tests.
- **[Output-order surprises when mixed with existing utilities]** → Rely on existing canonicalization ordering and add mixed-token runtime tests to lock expected href ordering.
- **[Migration can unintentionally change layout behavior]** → Migrate declaration-by-declaration with parity assertions in controller/component tests and preserve any non-covered rules until utility support exists.

## Migration Plan

1. Add compiler-side display token model + parser + generator hooks and register through Spring component scanning.
2. Add render-side display DSL helpers for base and variant scopes in `kolostyles.dsl.display`.
3. Add/extend tests in `:libs:kolo-styles`:
   - parser acceptance/rejection for full display token list
   - generator CSS emission for base, pseudo, and media variants
   - compiler/controller integration for mixed spacing + display tokens
   - runtime canonicalized href/class emission coverage using display DSL
4. Migrate current app display declarations to display DSL usage in render code and remove corresponding declarations from page/static CSS where behavior is fully covered.
5. Keep any non-covered display behavior as temporary exceptions, explicitly tagged for follow-up cleanup when utility support expands.
6. Deploy with existing side-by-side stylesheet delivery; rollback is safe by restoring removed CSS declarations and/or removing migrated display tokens from render call sites.
