## 1. Scope and Token Baseline

- [ ] 1.1 Confirm migration scope for browse/art typography ownership and list selectors currently using `font-family`, `font-size`, or `font-weight` in `CssController`
- [ ] 1.2 Freeze explicit Tailwind-compatible allow-lists for `font-family` (named families only), `font-size` (`text-xs` through `text-9xl`), and `font-weight` (`font-thin`, `font-extralight`, `font-light`, `font-normal`, `font-medium`, `font-semibold`, `font-bold`, `font-extrabold`, `font-black`)
- [ ] 1.3 Define canonical Kotlin helper naming-to-token mappings for all supported font helpers on base and variant scopes

## 2. Font Utility Compiler Hooks

- [ ] 2.1 Add typed font compiler token models for font-family, font-size, and font-weight metadata under `:libs:kolo-styles`
- [ ] 2.2 Implement a dedicated font parser hook that accepts only allow-listed tokens (including existing variant-prefix semantics), with font-weight acceptance limited to `font-thin` through `font-black`, and preserves permissive diagnostics for unsupported/malformed tokens
- [ ] 2.3 Implement a dedicated font generator hook that emits font CSS declarations through the shared compiler `CssBuilder`, including explicit font-weight mapping to numeric values (100..900)
- [ ] 2.4 Register font parser/generator hooks with existing Spring hook discovery so font utilities flow through `/css/generated/kolo.css`

## 3. Font DSL API

- [ ] 3.1 Add typed `KoloScope` font helpers in `kolostyles.dsl.font` for all supported family/size/weight tokens, including one helper per allow-listed weight token from `font-thin` through `font-black`
- [ ] 3.2 Add typed `KoloVariantScope` font helpers with identical token coverage and chaining behavior, including the full `font-thin` through `font-black` weight set
- [ ] 3.3 Ensure each helper emits canonical Tailwind-style token strings used by compiler canonicalization

## 4. Runtime and Canonicalization Integration

- [ ] 4.1 Verify font tokens participate in existing deterministic token canonicalization and URL generation without changing delimiter/order contracts
- [ ] 4.2 Verify state/media variants (`hover`, `focus`, `focus-visible`, `active`, `visited`, `sm`/`md`/`lg`/`xl`/`2xl`) generate correct selectors and media wrappers for font utilities
- [ ] 4.3 Verify mixed spacing/display/font token requests compile deterministically through the shared parser/generator pipeline

## 5. Browse and Art Font Ownership Migration

- [ ] 5.1 Redefine current default app font baseline to Tailwind sans semantics via `--font-sans`
- [ ] 5.2 Migrate font declarations from `CssController` to co-located `kolo { ... }` font helpers in small selector/component batches
- [ ] 5.3 Remove only migrated `font-family`, `font-size`, and `font-weight` declarations from generated page CSS while keeping non-migrated rules as explicit temporary exceptions
- [ ] 5.4 Keep side-by-side stylesheet delivery (`kolo.css` + page CSS) unchanged during incremental migration

## 6. Test Coverage Updates

- [ ] 6.1 Add parser tests for full font token acceptance and unsupported/malformed token diagnostics behavior, including explicit acceptance/rejection coverage around `font-thin` through `font-black` and disallowing custom weights
- [ ] 6.2 Add generator tests for base, pseudo-state, and media-variant font CSS emission, including assertions for the font-weight numeric mapping (`100`..`900`)
- [ ] 6.3 Add compiler/controller integration tests for `/css/generated/kolo.css` with mixed spacing/display/font tokens
- [ ] 6.4 Add runtime render tests asserting class emission and canonicalized `kolo.css` href generation from font DSL usage
- [ ] 6.5 Add app-level stylesheet/render tests proving migrated selectors no longer receive duplicate font declarations from `CssController` and preserve typography parity

## 7. Docs and OpenSpec Validation

- [ ] 7.1 Update relevant docs (`docs/ARCHITECTURE.md`, `docs/DECISIONS.md`, `docs/TESTING.md`, `docs/GLOSSARY.md`) to reflect new font utilities and migration ownership boundaries
- [ ] 7.2 Run project tests for changed paths and resolve any failures before handoff
- [ ] 7.3 Run `openspec validate --all` and resolve all validation issues for the change artifacts
