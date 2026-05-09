# BA-021: CSS endpoint to generate `kolo.css` from request parameters

## Metadata
- ID: `BA-021`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-09 09:15`
- Updated: `2026-05-09 09:15`
- Related Human Issue: none

## Goal
Implement the server-side CSS endpoint behavior for `/css/generated/kolo.css` so it deterministically generates utility rules from canonical request parameters (`version` and `kolo`).

## Scope
- In scope:
  - Parse and validate `kolo` query values using the canonical token contract.
  - Convert canonical tokens into generated CSS rules for supported utilities/variants.
  - Keep endpoint output deterministic for identical canonical input.
  - Add tests for endpoint parsing, validation, dedupe/sort assumptions, and output stability.
- Out of scope:
  - HTML DSL ergonomics (`kolo { ... }`) and element class application.
  - Render-layer stylesheet link tag generation.

## Locked Decisions from BA-016 / BA-018

### Token model
- Tailwind-style token names.
- Pseudo/media variants are in scope.
- Arbitrary value tokens (`[...]`) are deferred — reject them at the endpoint.

### CSS delivery contract
- Single utilities endpoint: `/css/generated/kolo.css`.
- Query params: `version=<build git sha>`, `kolo=<semicolon-separated canonical token list>`.
- `;` is reserved as delimiter and must not appear inside tokens.
- Keep existing page CSS side-by-side during migration; remove only declarations already covered by migrated Kolo utilities.

### Canonicalization (performed by the caller; endpoint may re-validate)
- split/trim/drop empty tokens
- reject `;` and `[...]` tokens
- dedupe exact tokens
- sort by `(group, variantCount, variantChain, baseUtility, token)`
- join with `;`

### Caching
- Versioned URL caching is the primary strategy.
- `ETag` support is optional.

## Plan
- [ ] Define endpoint contract details for accepted/rejected `kolo` parameter inputs.
- [ ] Implement token parsing: split on `;`, trim, drop empty, reject `[...]` tokens.
- [ ] Map tokens to CSS rule generation for supported utilities and pseudo/media variants.
- [ ] Ensure output is fully deterministic for an identical set of canonical tokens.
- [ ] Add focused tests for successful and invalid requests, and output stability.
- [ ] Verify compatibility with existing generated CSS routes and migration expectations.

## Progress Log
- `2026-05-09 09:15`: Task created by splitting superseded `BA-018` into focused implementation slices.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] `BA-022`: Wire request-scoped token collection and stylesheet link generation from rendered pages.
- [ ] `BA-019`: Apply first margin/padding utilities after endpoint + DSL plumbing is in place.
