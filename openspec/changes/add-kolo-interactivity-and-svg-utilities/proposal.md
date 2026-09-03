## Why

Cursor behavior and icon stroke/fill styling are still tied to semantic CSS selectors, even though Kolo can attach utilities directly to the button and SVG elements.

## What Changes

- Add typed cursor and SVG fill/stroke/line-cap/line-join utilities.
- Migrate button cursor and stat icon styling to element-local Kolo utilities.

## Capabilities

### New Capabilities
- `kolo-interactivity-svg-ownership`: interactivity and SVG utility ownership

### Modified Capabilities
- `kolo-utility-architecture`: Extends the typed utility-family contract for this capability.
- `kolo-css-generation`: Extends deterministic parser/generator endpoint output for this capability.
- `kolo-html-runtime-integration`: Extends typed DSL/runtime token collection for this capability.
- `generated-page-stylesheets`: Transfers migrated declarations from page CSS to Kolo.

## Impact

Depends on semantic theme colors; affects interactivity/SVG compiler and DSL packages, PostSummary/Header markup, CssController, and accessibility-preserving tests.
