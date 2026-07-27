# Kolo Spacing Ownership

## Purpose
Define spacing-ownership migration so browse/art margin and padding behavior is authored via typed Kolo utilities instead of `CssController`, while preserving layout parity.

## Requirements

### Requirement: Kolo utilities own spacing declarations
The system SHALL define all margin and padding behavior for migrated browse and art elements through `:libs:kolo-styles` utilities, and `CssController` MUST NOT emit margin or padding declarations for those migrated elements.

#### Scenario: Generated page styles omit migrated spacing declarations
- **WHEN** `CssController` generates page CSS for `/css/generated/browse.css` and `/css/generated/art.css`
- **THEN** the generated page CSS contains no `margin*` or `padding*` declarations for elements that have been migrated to Kolo spacing utilities

### Requirement: Rendering expresses spacing through typed Kolo DSL helpers
The system SHALL support spacing authoring through typed `kolo { ... }` helpers (`m`, `mt`, `mx`, `p`, `px`, and related variants), and MUST emit the resulting spacing tokens through the Kolo stylesheet pipeline.

#### Scenario: Spacing helpers produce canonicalized utility delivery
- **WHEN** render code applies spacing helpers such as `kolo { mt(2); px(0) }` on an element
- **THEN** the rendered output includes Kolo utility class names on that element and a `/css/generated/kolo.css` stylesheet reference that includes canonicalized spacing tokens

### Requirement: Spacing migration preserves layout parity
The system MUST preserve effective spacing layout for browse and art pages while moving spacing ownership from `CssController` to Kolo utilities.

#### Scenario: Equivalent spacing after ownership transfer
- **WHEN** a spacing rule is replaced by an equivalent Kolo spacing utility and the old `CssController` declaration is removed
- **THEN** the affected rendered page section keeps equivalent visual spacing behavior for margin and padding

## Provenance
- `BA-019` (`docs/ai-tasks/2026-05-06-BA-019-margin-padding-elements-csscontroller-cleanup-visual-parity.md`)
- `docs/ARCHITECTURE.md`
- `docs/DECISIONS.md` (D-008)