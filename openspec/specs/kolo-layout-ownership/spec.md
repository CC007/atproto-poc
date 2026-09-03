# Kolo Layout Ownership

## Purpose
Define the layout-ownership migration so browse/art layout behavior is authored via typed Kolo utilities instead of `CssController`, while preserving visual parity and keeping non-migratable rules explicit.

## Requirements

### Requirement: Kolo utilities own migrated layout declarations
The system SHALL define layout behavior for migrated browse and art elements through `:libs:kolo-styles` layout utilities, and `CssController` MUST NOT emit duplicate layout declarations for those migrated elements. Migrated layout families MUST include display, box-sizing, overflow, position, inset/top/right/bottom/left offsets, z-index, and object-fit.

#### Scenario: Generated page styles omit migrated layout declarations
- **WHEN** `CssController` generates page CSS for `/css/generated/browse.css` and `/css/generated/art.css`
- **THEN** the generated page CSS contains no `display`, `box-sizing`, `overflow*`, `position`, `top`, `right`, `bottom`, `left`, `z-index`, or `object-fit` declarations for elements that have been migrated to Kolo layout utilities

### Requirement: Rendering expresses layout through typed Kolo layout DSL helpers
The system SHALL support layout authoring through typed `kolo { ... }` layout helpers under `kolostyles.dsl.layout` on both base and variant scopes, and MUST emit the resulting layout tokens through the Kolo stylesheet pipeline.

#### Scenario: Layout helpers produce canonicalized utility delivery
- **WHEN** render code applies layout helpers such as `kolo { relative; overflowHidden }` on an element
- **THEN** the rendered output includes Kolo utility class names on that element and a `/css/generated/kolo.css` stylesheet reference that includes canonicalized layout tokens

### Requirement: Layout migration preserves visual parity with explicit exceptions
The system MUST preserve effective browse and art layout behavior while moving layout ownership from `CssController` to Kolo utilities. Rules that cannot be represented in current Kolo utilities or variants MUST remain in page CSS as explicit `kolo-exception` declarations.

#### Scenario: Non-migratable layout rule remains explicit
- **WHEN** a layout declaration cannot be represented by supported Kolo layout utilities or available variants
- **THEN** that declaration remains in generated page CSS and is marked as a `kolo-exception`
