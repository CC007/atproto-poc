## Why

Continued effort to migrate styles from css to kolo-styles

## What Changes

Look at TailwindCSS docs (https://tailwindcss.com/docs) in the layout section (from aspect-ratio to z-index). Then look at which styles are used in CssController. Any of these TailwindCSS listed layout styles that are adjacent to what is used in CssController should be made available in kolo-styles. Then these layout styles should be migrated from CssController to kolo layout styles

## Capabilities

### New Capabilities
- kolo-layout-ownership

### Modified Capabilities
- kolo-utility-architecture
- kolo-css-generation
- kolo-html-runtime-integration
- generated-page-stylesheets

## Impact

- Affected modules: `:libs:kolo-styles`, `:app`
- Affected contracts: layout utility parser/generator coverage and typed DSL surface (`kolostyles.dsl.layout`)
- Affected endpoint behavior: `/css/generated/kolo.css` adds deterministic generation support for migrated layout token families
- Affected ownership boundary: migrated layout declarations are removed from generated page CSS where Kolo utilities now own behavior
