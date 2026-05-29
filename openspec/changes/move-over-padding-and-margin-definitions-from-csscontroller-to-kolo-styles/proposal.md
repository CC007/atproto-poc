## Why

kolo-styles was created for co-located styling of components. This makes the styling more reusable. kolo-styling was created and now we want to move over the centralized styling from CssController to the kolo styles (for example `kolo { mt(2); px(0) }` to replace margin-top and horizontal padding)

## What Changes

CssController doesn't contain any padding/margin anymore (including margin-top, padding-left, etc). The padding and margin styles are now configured using kolo-styles (for example `kolo { mt(2); px(0) }`)

## Capabilities

### New Capabilities
<!-- Capabilities being introduced. Use kebab-case identifiers (e.g., user-auth, data-export). Each creates specs/<name>/spec.md -->

### Modified Capabilities
<!-- Existing capabilities whose REQUIREMENTS are changing. Use existing spec names from openspec/specs/. -->

## Impact

<!-- Affected code, APIs, dependencies, systems -->
