# Design: kolo width and height

## Context

The kolo-styles module has a well-established utility pipeline: typed DSL helpers → token recording → canonicalization → `StyleParserHook` → `StyleGeneratorHook` → `/css/generated/kolo.css`. Spacing, display, and font utility families already follow this architecture.

Sizing (width, height, and their min/max variants) is not yet covered by kolo-styles. Width and height declarations live in `CssController`-generated page CSS and static `.css` files. Two existing `// kolo-exception: max-width responsive` comments in `CssController` explicitly call out that certain responsive sizing rules are deferred until Kolo gains sizing support.

The Tailwind sizing naming conventions define the token names: `w-*`, `h-*`, `min-w-*`, `max-w-*`, `min-h-*`, `max-h-*`. Inspection of the app's actual sizing usage shows a mix of Tailwind-mappable values (`100%`, `100vh`, `0`, `auto`) and arbitrary pixel/rem values (`170px`, `0.9rem`, `1080px`) that have no direct Tailwind equivalent.

## Goals / Non-Goals

**Goals:**
- Add a `sizing` utility family to `:libs:kolo-styles` covering `width`, `height`, `min-width`, `max-width`, `min-height`, and `max-height` using the same parser/generator hook architecture as spacing and font.
- Define typed DSL helpers on `KoloScope` and `KoloVariantScope` for all six dimension sub-families.
- Support Tailwind-compatible named tokens (`auto`, `full`, `screen`, `fit`, `min`, `max`, `svh`, `dvh`, `lvh`), the full Tailwind numeric spacing scale, and fractional tokens (`1/2`, `1/3`, etc.) as an explicit allow-list.
- Migrate Tailwind-mappable sizing declarations from `CssController` and static CSS to co-located `kolo { ... }` DSL usage incrementally.
- Resolve the two pending `kolo-exception: max-width responsive` comments in `CssController`.

**Non-Goals:**
- Arbitrary-value sizing utilities (`w-[1.2rem]`, `h-[170px]`, etc.); such declarations remain as `kolo-exception` comments.
- Custom CSS variable-based sizing tokens beyond standard Tailwind semantics.
- Migrating sizing rules that map to CSS custom properties or dynamic card-height variable patterns (`var(--card-height)`); these are retained in `CssController` as explicit exceptions.
- Changing the Kolo URL canonicalization, cache strategy, or route structure.
- Introducing aspect-ratio, overflow, or other layout utilities outside the six targeted CSS properties.

## Decisions

### 1) Add a `sizing` package parallel to `spacing`, `display`, and `font`

**Decision:** Implement sizing support in `:libs:kolo-styles` as a `compiler/sizing` package (typed tokens + parser + generator hooks) and a `dsl/sizing` package (DSL helpers), following the exact same structure used by every other utility family.

**Rationale:** Uniform structure across utility families keeps the codebase predictable and maximizes reuse of tested compiler infrastructure. Deviating would require documenting exceptions and would complicate future additions.

**Alternatives considered:**
- Extend the spacing family to cover sizing: rejected because sizing and spacing have different CSS properties, value sets, and migration scope. Merging them would create a kitchen-sink family with unclear ownership.

### 2) Single `SizingToken` data class — no `cssProperty` field

**Decision:** Define one `internal data class SizingToken : Token` carrying `stateVariants`, `mediaVariant`, `utility`, and `value: LinearDimension`. There are no sub-types per dimension and no explicit `cssProperty` field.

**Rationale:** The `utility` string already encodes which CSS property or properties the token targets — the generator derives the output property/properties from the utility prefix (`w-` → `width`, `h-` → `height`, `min-w-` → `min-width`, `size-` → both `width` and `height`, etc.). Storing `cssProperty` separately would be redundant for single-property tokens and would be wrong for multi-property utilities like `size-*`, which sets both `width` and `height`. Using the utility prefix as the dispatch key keeps the model consistent and extensible. This matches the `SpacingToken` precedent, which also uses one data class and lets the generator derive the CSS property from the utility string.

**Alternatives considered:**
- `cssProperty: String` field on the token: rejected — redundant for single-property tokens and incorrect for `size-*` which targets two properties.
- Sealed `SizingToken` with one concrete type per dimension: rejected — all subtypes would be structurally identical (same fields, same value type), so the sealed hierarchy adds complexity without enabling any benefit that utility-prefix dispatch does not also provide.

### 3) Use `LinearDimension` directly as the value type — no wrapper needed

**Decision:** The `SizingToken.value` field is typed as `LinearDimension`. The token-to-value allow-list maps each supported token string to a concrete `LinearDimension` instance using the library's built-in companion constants and unit extension properties.

Relevant `LinearDimension` coverage:
- **Keywords**: `LinearDimension.auto`, `LinearDimension.maxContent` (`"max-content"`), `LinearDimension.minContent` (`"min-content"`), `LinearDimension.fitContent` (`"fit-content"`), `LinearDimension.none`
- **Full percentage/viewport units**: `100.pct` (`"100%"`), `100.vh`, `100.vw`, `100.svh`, `100.dvh`, `100.lvh`, `100.svw`, `100.dvw`, `100.lvw`
- **All numeric lengths**: `px`, `rem`, `em`, `pt`, `cm`, etc., including fractional rem values for the Tailwind numeric scale (e.g. `0.25.rem` for `w-1`)
- **`0` (zero)**: `LinearDimension("0")` or `0.px` (both emit `"0"`)

`LinearDimension.value` is the CSS string and can be emitted directly by the generator — no further unwrapping is required.

**Rationale:** `LinearDimension` already covers every sizing value needed for the Tailwind allow-list: keywords, percentages, viewport units (including `svh`/`dvh`/`lvh`), and numeric scale values. Wrapping it in a `SizingValue` sealed type would be pure overhead with no expressive benefit.

**Alternatives considered:**
- `SizingValue` sealed type with `Keyword(cssValue: String)` and `Dimension(value: LinearDimension)`: rejected — unnecessary indirection since `LinearDimension` already natively represents all keyword values via companion constants, making the sealed wrapper redundant.

### 4) Explicit allow-list for supported tokens

**Decision:** The parser accepts only a defined set of token strings per dimension sub-family:
- **Shared named tokens** (all six dimensions + `size-*`): `auto`, `full`, `screen`
- **Height/min-height/max-height additional named tokens**: `svh`, `dvh`, `lvh`, `fit`, `min`, `max`
- **Width/max-width additional named tokens**: `fit`, `min`, `max`, `screen` (maps to `100vw`)
- **`size-*` tokens**: same named and numeric tokens as width/height; generator emits both `width` and `height`
- **Max-width named breakpoints**: `xs`, `sm`, `md`, `lg`, `xl`, `2xl`, `3xl`, `4xl`, `5xl`, `6xl`, `7xl` (mapping to Tailwind `max-w-*` breakpoints)
- **Numeric Tailwind spacing scale**: `0`, `px`, `0.5`, `1`, `1.5`, `2`, `2.5`, `3`, `3.5`, `4`, `5`, `6`, `7`, `8`, `9`, `10`, `11`, `12`, `14`, `16`, `20`, `24`, `28`, `32`, `36`, `40`, `44`, `48`, `52`, `56`, `60`, `64`, `72`, `80`, `96`
- **Fractional tokens** (width/height/size only): `1/2`, `1/3`, `2/3`, `1/4`, `3/4`, `1/5`, `2/5`, `3/5`, `4/5`, `1/6`, `5/6`

Unsupported tokens return `null` from the parser (permissive diagnostics, consistent with existing families).

**Rationale:** Explicit allow-lists prevent accidental acceptance of ambiguous tokens and match the deterministic behavior already established for font utilities.

**Alternatives considered:**
- Pattern-based permissive parsing for any `w-*` string: rejected — too ambiguous and inconsistent with the project's deliberate allow-list policy.

### 5) Reuse existing `MediaVariant` and `KOLO_MEDIA_VARIANT_MIN_WIDTHS`

**Decision:** The sizing family reuses the shared `KOLO_MEDIA_VARIANT_MIN_WIDTHS` table and the same `MediaVariant` type already used by spacing and font, rather than defining a new local `MediaVariant`.

**Rationale:** There is no need for dimension-specific media breakpoints. The existing `MediaVariant` is already module-internal and suitable. Sharing it avoids drift. Note: a single `MediaVariant` data class is currently duplicated across `spacing` and `font` packages — this change is an opportunity to consolidate to one shared definition.

**Alternatives considered:**
- Define a sizing-local `MediaVariant`: rejected — pure duplication with no added value.

### 6) Incremental migration, arbitrary values left as `kolo-exception`

**Decision:** Migrate sizing declarations to Kolo DSL only where the value maps cleanly to a supported allow-list token. Declarations using arbitrary pixel/rem values (`170px`, `0.9rem`, `1080px`, `72vh`, `var(--card-height)`) are marked with `// kolo-exception: arbitrary value` in `CssController` or left in static CSS as documented exceptions.

**Rationale:** Preserves visual parity without blocking shipping the utility family. Follows established migration practice from spacing and font changes.

**Alternatives considered:**
- Introduce arbitrary-value tokens to unblock full migration in this change: rejected — arbitrary-value parsing is a large, separate design concern that would significantly expand scope and risk.

## Risks / Trade-offs

- [Tailwind sizing token surface is large] → Defer fractional tokens and less-common named keywords to a follow-up if they prove unnecessary for the current app; allow-list makes adding them low-risk.
- [Shared `MediaVariant` duplication] → Address the consolidation opportunity as part of this change to avoid adding a third duplicate; risk is low since the type is internal.
- [Sizing CSS property names differ from token prefix names (`min-w` → `min-width`)] → Generator hook derives target properties from the utility prefix via an explicit prefix-to-property(s) map; add tests for all seven prefix families including `size-*` emitting both `width` and `height`.
- [Selector precedence changes when moving declarations from page CSS to utility classes] → Migrate per selector, confirm visual parity before removing old declaration; rollback is reinserting the CssController rule.
- [Many existing sizing values are arbitrary and cannot be migrated] → Track each non-migratable site with a `kolo-exception` comment identifying the reason (e.g., `arbitrary value`, `css-var`).

## Migration Plan

1. Consolidate the `MediaVariant` data class to a single shared definition in the `compiler` package; update `spacing` and `font` packages to import it.
2. Implement `SizingToken` as a single data class with `stateVariants`, `mediaVariant`, `utility`, and `value: LinearDimension` in `compiler/sizing`.
3. Implement `SizingParserHook` with the defined allow-list for all seven token families (`w-*`, `h-*`, `min-w-*`, `max-w-*`, `min-h-*`, `max-h-*`, `size-*`); each entry maps a token string to a `LinearDimension` value, and register it as a Spring `@Component`.
4. Implement `SizingGeneratorHook` consuming typed sizing tokens to emit the correct CSS property and value, and register it as a Spring `@Component`.
5. Add typed DSL extension properties on `KoloScope` and `KoloVariantScope` in `dsl/sizing` for all supported tokens across all six sub-families.
6. Add unit tests for parser (acceptance and rejection per sub-family), generator (CSS output per token type), and DSL (class name emission and stylesheet href inclusion).
7. Migrate Tailwind-mappable sizing declarations from `CssController` and static CSS to `kolo { ... }` call sites incrementally per component/selector.
8. Mark non-migratable sizing declarations (`arbitrary value`, `css-var`) with `// kolo-exception` comments.
9. Resolve the two existing `kolo-exception: max-width responsive` entries in `CssController` using the new max-width named breakpoint tokens.
10. Deploy under the existing side-by-side `kolo.css` + page CSS strategy; no route or infrastructure changes required.

**Rollback strategy:** Reintroduce removed `CssController` sizing declarations for any selector exhibiting visual regression, and remove the corresponding `kolo { ... }` calls until the issue is diagnosed.
