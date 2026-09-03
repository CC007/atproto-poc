## Why

The page CSS owns a repeated BlueArt palette as raw CSS variables, preventing color utilities from using a constrained reusable theme.

## What Changes

- Define semantic BlueArt color tokens compatible with Tailwind-style utility generation.
- Establish token ownership and deterministic use by later color-bearing utility families.

## Capabilities

### New Capabilities
- `kolo-theme-colors`: semantic Kolo theme colors

### Modified Capabilities
- `kolo-utility-architecture`: Extends the typed utility-family contract for this capability.
- `kolo-css-generation`: Extends deterministic parser/generator endpoint output for this capability.
- `kolo-html-runtime-integration`: Extends typed DSL/runtime token collection for this capability.
- `generated-page-stylesheets`: Transfers migrated declarations from page CSS to Kolo.

## Impact

Affects theme/token infrastructure and future background, border, typography, SVG, and effects utilities; does not yet migrate visual declarations.
