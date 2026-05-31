## Why

That way it's possible to visually check if everything works alright, even when the real timeline doesn't currently contain any of the post types.

## What Changes

You can log in with a dummy account that doesn't actually log into bluesky and instead lets you view a timeline with dummy content of all the different post types (which are also accessible on a detail page)

## Capabilities

### New Capabilities
<!-- Capabilities being introduced. Use kebab-case identifiers (e.g., user-auth, data-export). Each creates specs/<name>/spec.md -->

- `dummy-account-login`
- `dummy-content-preview`

### Modified Capabilities
<!-- Existing capabilities whose REQUIREMENTS are changing. Use existing spec names from openspec/specs/. -->

## Impact

<!-- Affected code, APIs, dependencies, systems -->

- Login/auth selection when the submitted network URL targets the reserved localhost dummy network
- Runtime localhost controllers and fixture-backed responses for dummy authentication and content browsing
- Deterministic dummy browse/detail content coverage for supported post types, including remote media variants

