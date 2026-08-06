# kolo-width-and-height

## ADDED Requirements

### Requirement: Kolo utilities own migrated sizing declarations
The system SHALL define width, height, min-width, max-width, min-height, and max-height behavior for migrated browse and art elements through `:libs:kolo-styles` utilities, and `CssController` MUST NOT emit those sizing declarations for migrated elements.

#### Scenario: Generated page styles omit migrated sizing declarations
- **WHEN** `CssController` generates page CSS for migrated browse or art selectors
- **THEN** those selectors contain no `width`, `height`, `min-width`, `max-width`, `min-height`, or `max-height` declarations in page CSS

### Requirement: Sizing DSL emits Tailwind-compatible utility names
The system SHALL expose typed sizing helpers in Kolo DSL that emit utility class names aligned with Tailwind sizing naming for `w-*`, `h-*`, `min-w-*`, `max-w-*`, `min-h-*`, `max-h-*`, and `size-*` families.

#### Scenario: Typed helper renders canonical sizing class
- **WHEN** render code applies a typed sizing helper in `kolo { ... }`
- **THEN** the rendered element includes the corresponding Tailwind-compatible Kolo sizing utility class name

### Requirement: Supported sizing tokens follow explicit allow-list behavior
The system MUST accept only defined sizing tokens for the supported sizing utility families, and unsupported tokens MUST NOT produce generated sizing CSS.

#### Scenario: Unsupported sizing token is ignored
- **WHEN** a sizing utility token outside the supported allow-list is encountered
- **THEN** no sizing rule is generated for that token in `/css/generated/kolo.css`
