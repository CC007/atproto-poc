# BA-018: Type-safe style collection/parsing/application framework

## Metadata
- ID: `BA-018`
- Status: `cancelled`
- Owner: `ai`
- Created: `2026-05-06 20:15`
- Updated: `2026-05-09 09:15`
- Related Human Issue: none

## Goal
This task originally covered both utility CSS endpoint generation and `kolo { ... }` DSL/application wiring. It has been superseded by two narrower tasks to make execution and validation clearer: `BA-021` and `BA-022`.

## Scope
- In scope:
  - Preserve historical planning context from BA-016 without executing implementation under this task ID.
  - Point implementation to replacement tasks with explicit boundaries.
- Out of scope:
  - Any direct implementation work.

## Locked Decisions from BA-016
- API shape:
  - Element-attached DSL via `kolo { ... }`.
  - Zero-arg utilities as bare properties; parameterized utilities as functions.
- Token model:
  - Tailwind-style token names.
  - Pseudo/media variants are in scope.
  - Arbitrary value tokens (`[...]`) are deferred.
- CSS delivery:
  - Single utilities endpoint: `/css/generated/kolo.css`.
  - Keep existing page CSS side-by-side during migration and remove only declarations covered by migrated Kolo utilities.
  - Query params: `version=<build git sha>`, `kolo=<semicolon-separated token list>`.
  - Canonicalization must deduplicate and variant-sort the token list before request generation.
  - `;` is reserved as delimiter and must not appear inside tokens.
- Caching:
  - Versioned URL caching is the primary strategy; `ETag` support is optional.

## API Sketch to Implement
- Intended developer-facing shape:

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

- Initial implementation sketch:

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

- Notes for implementation:
  - `kolo { ... }` is attached directly to the current HTML element.
  - Zero-arg utilities are exposed as bare properties whose getter records a token.
  - Parameterized utilities are exposed as functions.
  - Variant chains should be represented through nested scopes/builders, not raw strings typed by the developer.
  - The DSL should record tokens into a request-scoped collector rather than resolving the stylesheet URL immediately.
  - The framework should also attach the generated class name(s) for the current element behind the scenes.

## Plan
- [x] Capture consolidated framework constraints from `BA-016`.
- [x] Split execution scope into two focused tasks:
  - `BA-021`: CSS endpoint that generates utility stylesheet output from canonical request parameters.
  - `BA-022`: `kolo { ... }` extension surface, class generation, token collection, and stylesheet link tag integration.

## Progress Log
- `2026-05-06 20:15`: Task created to build type-safe utility infrastructure.
- `2026-05-09 00:00`: Synced implementation constraints from BA-016 decisions (DSL shape, token contract, endpoint, and cache model).
- `2026-05-09 00:10`: Added explicit canonical ordering algorithm and side-by-side migration rule for page CSS + `kolo.css`.
- `2026-05-09 00:25`: Updated canonical sort-key precedence to match BA-016 readability rule.
- `2026-05-09 00:35`: Added concrete `kolo { ... }` extension/property/function sketch plus request-scoped collection requirement for implementation.
- `2026-05-09 09:15`: Task cancelled and superseded by `BA-021` (CSS endpoint generation) and `BA-022` (`kolo {}` + class/link integration).

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] `BA-021`: Implement stylesheet endpoint generation from canonical parameters.
- [ ] `BA-022`: Implement `kolo {}` extension wiring, element classes, and stylesheet link emission.
- [ ] `BA-019`: Add first margin/padding utilities and remove equivalent rules from `CssController` after `BA-021` and `BA-022` foundations are in place.

