# Kolo HTML Runtime Integration — Delta

## MODIFIED Requirements

### Requirement: Render-time Kolo DSL collects tokens and emits stylesheet links
The system SHALL expose render-time Kolo APIs from the `kolostyles.dsl` namespace, with spacing utility helpers under `kolostyles.dsl.spacing`, while preserving existing token collection and stylesheet-link emission behavior.

#### Scenario: App module imports Kolo DSL/runtime APIs
- **WHEN** server-rendered pages use `renderKoloHtml`, `kolo { ... }`, and spacing utility helpers
- **THEN** imports resolve from the `dsl` namespace and runtime behavior remains equivalent
