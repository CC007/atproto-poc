## Why

Continued effort to move over styling from CssController to the new co-located styling with kolo-styles

## What Changes

Any font-related styling needs to be moved from CssController to new kolo-styles definitions. These definitions don't exist yet, so they need to be added as DSL, parser and generator. After those are implemented, they can be co-located inside the HTML elements. 
The Kolo styles are based on TailwindCSS (https://tailwindcss.com/docs/font-family, https://tailwindcss.com/docs/font-size, https://tailwindcss.com/docs/font-weight) and should behave as such.

## Capabilities

### New Capabilities
<!-- Capabilities being introduced. Use kebab-case identifiers (e.g., user-auth, data-export). Each creates specs/<name>/spec.md -->

### Modified Capabilities
<!-- Existing capabilities whose REQUIREMENTS are changing. Use existing spec names from openspec/specs/. -->

## Impact

<!-- Affected code, APIs, dependencies, systems -->
