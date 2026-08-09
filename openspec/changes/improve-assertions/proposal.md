## Why

Junit assertions are very limited, especially when looking at the message when tests fail

## What Changes

Look for alternatives that are able to assert more specifically (Kotlin tests, AssertJ, Hamcrest, etc.) also take into account that there are places where things are asserted regarding the contents of a HTML string. Look for something that can more specifically assert (using DOM, XML or XPath assertions or something).

## Capabilities

### New Capabilities
<!-- Capabilities being introduced. Use kebab-case identifiers (e.g., user-auth, data-export). Each creates specs/<name>/spec.md -->

### Modified Capabilities
<!-- Existing capabilities whose REQUIREMENTS are changing. Use existing spec names from openspec/specs/. -->
- testing-verification-practices

## Impact

<!-- Affected code, APIs, dependencies, systems -->
