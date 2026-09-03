## Context

- Extend Kolo typography with Tailwind-compatible refinement utilities.
- Migrate direct-parity text declarations while retaining browser-specific text clamping as an exception.

## Goals / Non-Goals

**Goals:**
- Add the capability through Kolo’s typed DSL, parser-hook, generator-hook, and deterministic stylesheet pipeline.
- Migrate only declarations with direct utility parity and preserve visual behavior.

**Non-Goals:**
- Arbitrary-value or CSS-variable utility syntax.
- Changing canonical token ordering, stylesheet URL delivery, or unrelated visual design.

## Decisions

Extend the existing `font` package rather than create a second typography root. Split token models by property group only if needed. Support semantic text colors, leading, tracking, decoration line/offset, transform, overflow-wrap, and word-break values that have exact parity.

## Risks / Trade-offs

- [Utility coverage can expand without a boundary] → Use an explicit, documented Tailwind-derived allow-list.
- [Utility/page-CSS precedence can shift during migration] → Transfer each declaration and remove its duplicate in the same change, backed by controller and visual tests.
- [A rule lacks faithful utility parity] → Retain it as a `kolo-exception` in `CssController`.

## Migration Plan

1. Inventory affected declarations and their `kotlinx.html` call sites.
2. Add typed token, parser, generator, and DSL support with base, state, and supported responsive variants.
3. Add focused unit, endpoint, runtime, and ownership-boundary tests.
4. Migrate matching render sites and remove duplicate `CssController` declarations.
5. Run targeted Gradle and visual checks; rollback by restoring the removed declaration and removing its token.
