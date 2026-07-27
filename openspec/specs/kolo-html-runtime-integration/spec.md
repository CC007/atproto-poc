# Kolo HTML Runtime Integration

## Purpose
Define render-time Kolo token collection, class attachment, and stylesheet-link emission behavior.

## Requirements

### Requirement: Render-time Kolo DSL collects tokens and emits stylesheet links
The system SHALL collect Kolo tokens in render scope and emit a canonical `kolo.css` stylesheet URL for the rendered response.

#### Scenario: Kolo DSL is used during response rendering
- **WHEN** HTML rendering executes `kolo { ... }` in a Kolo render context
- **THEN** collected tokens are canonicalized and included in the emitted `kolo.css` href

### Requirement: Kolo DSL calls outside Kolo render context are inert
The system MUST allow `kolo { ... }` usage outside Kolo render scope without introducing runtime failures.

#### Scenario: Kolo call happens outside render context
- **WHEN** no active Kolo render context exists
- **THEN** token collection and stylesheet-link side effects are skipped safely

## Provenance
- `BA-022` (`docs/ai-tasks/2026-05-09-BA-022-kolo-extension-link-class-generation.md`)
- `docs/TESTING.md`
