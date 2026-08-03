## Why

To add to the capabilities of kolo-styles

## What Changes

The display layouts from https://tailwindcss.com/docs/display are also available to be used through kolo-styles, so that this doesn't have to live in a separate CSS stylesheet anymore.

## Capabilities

### New Capabilities
None.

### Modified Capabilities
- kolo-utility-architecture
- kolo-html-runtime-integration
- kolo-css-generation
- generated-page-stylesheets

## Impact

- Affected modules: `:libs:kolo-styles`, `:app`
- Affected contracts: parser/generator hook coverage, render-time DSL utility surface, `/css/generated/kolo.css` token handling, generated browse/art stylesheet ownership boundaries for migrated display declarations
