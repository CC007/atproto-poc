# Kolo HTML Runtime Integration

## Provenance
- `BA-022` (`docs/ai-tasks/2026-05-09-BA-022-kolo-extension-link-class-generation.md`)
- `docs/TESTING.md`

## Purpose
Define render-time Kolo token collection, class attachment, and stylesheet-link emission behavior.

## Requirements

### Requirement: Render-time Kolo DSL collects tokens and emits stylesheet links
The system SHALL collect Kolo tokens in render scope and emit a canonical `kolo.css` stylesheet URL for the rendered response.

#### Scenario: Kolo DSL is used during response rendering
- **WHEN** HTML rendering executes `kolo { ... }` in a Kolo render context
- **THEN** collected tokens are canonicalized and included in the emitted `kolo.css` href

### Requirement: Render-time Kolo DSL lives under `kolostyles.dsl`
The system SHALL expose render-time Kolo APIs from the `kolostyles.dsl` namespace, with spacing utility helpers under `kolostyles.dsl.spacing` and display utility helpers under `kolostyles.dsl.display`, while preserving existing token collection and stylesheet-link emission behavior.

#### Scenario: App module imports Kolo DSL/runtime APIs
- **WHEN** server-rendered pages use `renderKoloHtml`, `kolo { ... }`, spacing utility helpers, and display utility helpers
- **THEN** imports resolve from the `dsl` namespace and runtime behavior remains equivalent

#### Scenario: Migrated display call sites emit display utility tokens
- **WHEN** browse/art render code replaces CSS-owned display declarations with display DSL helpers
- **THEN** rendered HTML includes class names and canonicalized `kolo.css` token delivery for those display utilities

### Requirement: Kolo DSL calls outside Kolo render context are inert
The system MUST allow `kolo { ... }` usage outside Kolo render scope without introducing runtime failures.

#### Scenario: Kolo call happens outside render context
- **WHEN** no active Kolo render context exists
- **THEN** token collection and stylesheet-link side effects are skipped safely
