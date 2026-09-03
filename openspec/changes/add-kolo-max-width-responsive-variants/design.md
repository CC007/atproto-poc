## Context

- Add Tailwind-compatible `max-*` breakpoint variants to Kolo.
- Migrate the 960px browse sidebar, 700px art spacing, and 640px content-top overrides where equivalent utilities exist.

## Goals / Non-Goals

**Goals:**
- Add the capability through Kolo’s typed DSL, parser-hook, generator-hook, and deterministic stylesheet pipeline.
- Migrate only declarations with direct utility parity and preserve visual behavior.

**Non-Goals:**
- Arbitrary-value or CSS-variable utility syntax.
- Changing canonical token ordering, stylesheet URL delivery, or unrelated visual design.

## Decisions

Use named `max-sm` through `max-2xl` variants with Tailwind’s exclusive upper-bound semantics. Keep custom 960px and 700px rules as explicit breakpoint configuration decisions rather than silently approximating them.

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
