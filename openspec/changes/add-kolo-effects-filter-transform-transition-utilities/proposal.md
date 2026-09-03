## Why

Shadows, blur, opacity, transforms, backdrop filters, and transitions remain centralized even though they are element-local effects with existing hover variants.

## What Changes

- Add typed effects, filter, transform, and transition utility families.
- Migrate direct-parity card shadows, media blur behavior, opacity, SVG translations, and transitions.

## Capabilities

### New Capabilities
- `kolo-effects-ownership`: effects utility ownership

### Modified Capabilities
- `kolo-utility-architecture`: Extends the typed utility-family contract for this capability.
- `kolo-css-generation`: Extends deterministic parser/generator endpoint output for this capability.
- `kolo-html-runtime-integration`: Extends typed DSL/runtime token collection for this capability.
- `generated-page-stylesheets`: Transfers migrated declarations from page CSS to Kolo.

## Impact

Affects new effect-family compiler/DSL packages, media renderers, CssController, and state-variant visual coverage.
