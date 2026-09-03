## Why

Kolo owns font family, size, and weight, but remaining text color, line height, tracking, decoration, casing, and wrapping remain in selector-based page CSS.

## What Changes

- Extend Kolo typography with Tailwind-compatible refinement utilities.
- Migrate direct-parity text declarations while retaining browser-specific text clamping as an exception.

## Capabilities

### New Capabilities
- `kolo-typography-refinement-ownership`: typography refinement utility ownership

### Modified Capabilities
- `kolo-utility-architecture`: Extends the typed utility-family contract for this capability.
- `kolo-css-generation`: Extends deterministic parser/generator endpoint output for this capability.
- `kolo-html-runtime-integration`: Extends typed DSL/runtime token collection for this capability.
- `generated-page-stylesheets`: Transfers migrated declarations from page CSS to Kolo.

## Impact

Depends on semantic theme colors; affects the font DSL/compiler family, rich-text renderers, CssController, and visual tests.
