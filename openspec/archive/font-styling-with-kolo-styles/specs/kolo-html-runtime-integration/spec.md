## MODIFIED Requirements

### Requirement: Render-time Kolo DSL lives under `kolostyles.dsl`
The system SHALL expose render-time Kolo APIs from the `kolostyles.dsl` namespace, with spacing utility helpers under `kolostyles.dsl.spacing`, display utility helpers under `kolostyles.dsl.display`, and font utility helpers under `kolostyles.dsl.font`, while preserving existing token collection and stylesheet-link emission behavior.

#### Scenario: App module imports Kolo DSL/runtime APIs
- **WHEN** server-rendered pages use `renderKoloHtml`, `kolo { ... }`, spacing utility helpers, display utility helpers, and font utility helpers
- **THEN** imports resolve from the `dsl` namespace and runtime behavior remains equivalent

#### Scenario: Migrated display call sites emit display utility tokens
- **WHEN** browse/art render code replaces CSS-owned display declarations with display DSL helpers
- **THEN** rendered HTML includes class names and canonicalized `kolo.css` token delivery for those display utilities

#### Scenario: Migrated font call sites emit font utility tokens
- **WHEN** browse/art render code replaces CSS-owned font declarations with font DSL helpers
- **THEN** rendered HTML includes class names and canonicalized `kolo.css` token delivery for those font utilities