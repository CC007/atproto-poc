## Why

It adds type-safety and was also used in the CssController, so it might make it easier to migrate

## What Changes

Kolo-styles makes use of kotlin-css-jvm and its kotlinx.css.CssBuilder to generate the CSS response.

## Capabilities

### New Capabilities
<!-- Capabilities being introduced. Use kebab-case identifiers (e.g., user-auth, data-export). Each creates specs/<name>/spec.md -->

### Modified Capabilities
<!-- Existing capabilities whose REQUIREMENTS are changing. Use existing spec names from openspec/specs/. -->
- `kolo-css-generation`
- `kolo-styles-module-foundation`
- `kolo-utility-architecture`
- `kolo-html-runtime-integration`

## Impact

<!-- Affected code, APIs, dependencies, systems -->
- Runtime CSS generation now uses typed parser/generator token contracts in `:libs:kolo-styles` compiler flow.
- Diagnostic output for unparsed/unsupported tokens is emitted as CSS custom properties on `:root` (not free-form CSS comments).
- Kolo runtime/DSL APIs were reorganized into `kolostyles.dsl` and `kolostyles.dsl.spacing` packages, with app imports updated accordingly.
