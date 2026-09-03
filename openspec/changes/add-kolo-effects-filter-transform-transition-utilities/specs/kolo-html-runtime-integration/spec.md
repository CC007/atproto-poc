## MODIFIED Requirements

### Requirement: Render-time Kolo DSL lives under `kolostyles.dsl`
The system SHALL expose render-time Kolo APIs from the `kolostyles.dsl` namespace, with spacing utility helpers under `kolostyles.dsl.spacing`, layout utility helpers under `kolostyles.dsl.layout`, font utility helpers under `kolostyles.dsl.font`, sizing utility helpers under `kolostyles.dsl.sizing`, and effects utility ownership helpers under their cohesive DSL package, while preserving existing token collection and stylesheet-link emission behavior.

#### Scenario: App module imports Kolo DSL/runtime APIs
- **WHEN** server-rendered pages use `renderKoloHtml`, `kolo { ... }`, and typed helpers from supported utility families
- **THEN** imports resolve from the `dsl` namespace and runtime behavior remains equivalent

#### Scenario: effects utility ownership call sites emit utility tokens
- **WHEN** browse/art render code replaces CSS-owned declarations with effects utility ownership DSL helpers
- **THEN** rendered HTML includes class names and canonicalized `kolo.css` token delivery for those utilities

### Requirement: Kolo DSL calls outside Kolo render context are inert
The system MUST allow `kolo { ... }` usage outside Kolo render scope without introducing runtime failures.

#### Scenario: Kolo call happens outside render context
- **WHEN** no active Kolo render context exists
- **THEN** token collection and stylesheet-link side effects are skipped safely
