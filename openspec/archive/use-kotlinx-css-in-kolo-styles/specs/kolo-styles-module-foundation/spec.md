# Kolo Styles Module Foundation — Delta

## MODIFIED Requirements

### Requirement: Styling framework contracts live in dedicated library module
The system SHALL provide `:libs:kolo-styles` as the dedicated module for styling utility contracts and integration points. The module SHALL declare `kotlin-css-jvm` as an explicit `implementation` dependency, using the same version as declared in `:app`, to enable type-safe CSS construction in the styling library.

#### Scenario: App module consumes styling contracts
- **WHEN** `:app` compiles with Kolo styling integration
- **THEN** it resolves required contracts from `:libs:kolo-styles`

#### Scenario: kolo-styles compiles with explicit CSS DSL dependency
- **WHEN** `:libs:kolo-styles` compiles
- **THEN** `kotlinx.css.CssBuilder` and related DSL types are available from its own declared dependencies
