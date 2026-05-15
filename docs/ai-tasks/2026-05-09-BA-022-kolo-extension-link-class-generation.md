# BA-022: `kolo {}` extension, class generation, and stylesheet link integration

## Metadata
- ID: `BA-022`
- Status: `done`
- Owner: `ai`
- Created: `2026-05-09 09:15`
- Updated: `2026-05-15 11:52`
- Related Human Issue: none

## Goal
Implement the rendering-side `kolo { ... }` API and integration points that collect typed utility tokens, attach generated classes to elements, and emit the `kolo.css` `<link>` tag for each response.

## Scope
- In scope:
  - Implement/complete the `kolo { ... }` extension entry points and runtime scaffolding for later typed utility accessors.
  - Collect utility tokens at render time in request scope.
  - Attach generated class names to HTML elements transparently.
  - Emit stylesheet link tags referencing `/css/generated/kolo.css?version=...&kolo=...` using canonical token output.
  - Add tests for DSL usage, token collection, class emission, and link generation.
- Out of scope:
  - Endpoint-side CSS generation internals for parsing/generating `kolo.css` response content.
  - Broad utility catalog migration beyond the initial framework behavior.

## Locked Decisions from BA-016 / BA-018

### API shape
- Element-attached DSL via `kolo { ... }`.
- Zero-arg utilities as bare properties; parameterized utilities as functions.
- Variant chains represented through nested scopes/builders — not raw strings typed by the developer.

### Token model
- Tailwind-style token names.
- Pseudo/media variants are in scope.
- Arbitrary value tokens (`[...]`) are deferred.

### CSS delivery contract
- Single utilities endpoint: `/css/generated/kolo.css`.
- Query params: `version=<build git sha>`, `kolo=<semicolon-separated canonical token list>`.
- `;` is reserved as delimiter and must not appear inside tokens.
- Keep existing page CSS side-by-side during migration; remove only declarations already covered by migrated Kolo utilities.

### Canonicalization (performed before link emission)
- split/trim/drop empty tokens
- reject `;` and `[...]` tokens
- dedupe exact tokens
- sort by `(group, variantCount, variantChain, baseUtility, token)`
- join with `;`

### Caching
- Versioned URL caching is the primary strategy.
- `ETag` support is optional.

### Request-scoped collection model
- Tokens are recorded into a request-scoped collector at render time — not resolved immediately.
- The framework attaches generated class name(s) to the current HTML element behind the scenes.
- The canonical `kolo.css` href is built after all render-time token collection completes (two-pass render preferred; placeholder URL replacement is an acceptable fallback).

## API Sketch to Implement Later

Intended developer-facing shape:

```kotlin
div {
    kolo {
        flex
        mt(2)
        px(4)
        hover.bg.sky(500)
        md.mt(2)
    }
}
```

Initial implementation sketch:

```kotlin
fun FlowContent.kolo(block: KoloScope.() -> Unit)

class KoloScope internal constructor(
    private val sink: KoloTokenSink
) {
    val flex: Unit
        get() { sink.add("flex") }

    fun mt(value: Int) {
        sink.add("mt-$value")
    }

    fun px(value: Int) {
        sink.add("px-$value")
    }
}
```

Implementation notes:
- `kolo { ... }` is attached directly to the current HTML element.
- Zero-arg utilities are exposed as bare properties whose getter records a token.
- Parameterized utilities are exposed as functions.
- Variant chains use nested scopes/builders (e.g. `hover.bg.sky(500)`, `md.mt(2)`).
- The DSL records tokens into a request-scoped collector, not a stylesheet URL immediately.
- The framework attaches generated class name(s) to the element transparently.
- Data owned by the request-scoped context:
  - collected token set
  - canonicalization helper
  - final `kolo.css` href builder using `version` + canonical `kolo`

## Plan
- [x] Finalize render/runtime surface for `kolo { ... }` — extension function, `KoloScope`, token sink.
- [~] Implement token properties and function entries for initial utility set (bare properties for zero-arg, functions for parameterized). → deferred to BA-019.
- [~] Implement nested scope builders for pseudo/media variant chains. → `variant()` scaffolding exists on `KoloScope` and `KoloVariantScope`; concrete utility entries deferred to BA-019.
- [x] Implement request-scoped token collector and class attachment path.
- [x] Implement canonical canonicalization pipeline (dedupe → variant-aware sort → join).
- [x] Integrate `kolo.css` link generation into the rendering layout pipeline using the canonical `kolo` param.
- [x] Add unit/integration tests for deterministic token/link/class behavior.

## Progress Log
- `2026-05-09 09:15`: Task created by splitting superseded `BA-018` into focused implementation slices.
- `2026-05-11 03:18`: Added `renderKoloHtml`, request-scoped token collection, canonical `kolo` URL generation, and browse/art head link integration. Production class mapping is intentionally left inert for now (default mapper returns no class names) and is only exercised through a test-only mapper path.
- `2026-05-14 00:00`: Kept variant-aware scaffolding in `KoloScope`/`KoloVariantScope` for future BA-019 work, but removed any concrete production utility members so BA-022 does not pre-implement `flex`, `mt`, `px`, or similar entries.
- `2026-05-15 11:52`: Upgraded tests from spot-checks to full HTML content assertions. Refactored `KoloScope` to hold only the sink (no variant tracking), moved variant chain ownership entirely into `KoloVariantScope`. Renamed `withVariant`/`recordBaseToken` to concise `variant`/`recordBase`, eliminating the test-only adapter helpers. Added multi-element test covering deduplicated href union and per-element class scoping.

## How Completed
The in-scope framework is fully wired:
- `renderKoloHtml` provides a request-scoped render context via a `ThreadLocal` holder.
- `kolo { }` collects tokens into the context and attaches mapped class names to the element.
- `koloStylesheetLink()` emits a placeholder href that is replaced post-render with the canonicalized `/css/generated/kolo.css?version=…&kolo=…` URL.
- `canonicalizeKoloTokens` deduplicates, rejects unsupported tokens, sorts by `(group, variantCount, variantChain, baseUtility, token)`, and joins with `;`.
- `KoloScope` / `KoloVariantScope` provide the DSL scaffold; concrete utilities (`flex`, `mt`, `px`, …) are intentionally absent — that work belongs to BA-019.

## Verification
All tests in `KoloHtmlRuntimeTest` pass (`./gradlew :libs:kolo-styles:test`):
- `canonicalize dedupes sorts and drops unsupported tokens`
- `renderKoloHtml emits canonicalized stylesheet href from collected tokens`
- `kolo can attach classes via mapper when explicitly provided`
- `kolo variant scaffold can compose nested variant tokens`
- `kolo collects tokens across multiple elements deduplicating overlaps in the stylesheet href`
- `kolo noops when called outside kolo rendering context`

## Follow-ups
- [ ] `BA-019`: Apply first margin/padding utilities in pages once framework wiring is complete.
