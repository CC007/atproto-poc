## Why

Residual page CSS is dominated by Flexbox and Grid declarations that Tailwind treats as coherent layout families, but Kolo currently only owns display and selected layout primitives.

## What Changes

- Add typed Tailwind-compatible flexbox and grid utilities.
- Migrate direction, wrapping, grow/shrink, alignment, gap, and grid-template declarations with direct allow-list parity.

## Capabilities

### New Capabilities
- `kolo-flexbox-grid-ownership`: flexbox and grid utility ownership

### Modified Capabilities
- `kolo-utility-architecture`: Extends the typed utility-family contract for this capability.
- `kolo-css-generation`: Extends deterministic parser/generator endpoint output for this capability.
- `kolo-html-runtime-integration`: Extends typed DSL/runtime token collection for this capability.
- `generated-page-stylesheets`: Transfers migrated declarations from page CSS to Kolo.

## Impact

Affects new `flexboxgrid` compiler/DSL families, browse/art markup, CssController, and visual tests.
