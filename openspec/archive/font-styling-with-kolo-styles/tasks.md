## 1. Scope and Token Baseline

- [x] 1.1 Confirm migration scope for browse/art typography ownership and list selectors currently using `font-family`, `font-size`, or `font-weight` in `CssController`
- [x] 1.2 Freeze explicit Tailwind-compatible allow-lists for `font-family` (named families only), `font-size` (`text-xs` through `text-9xl`), and `font-weight` (`font-thin`, `font-extralight`, `font-light`, `font-normal`, `font-medium`, `font-semibold`, `font-bold`, `font-extrabold`, `font-black`)
- [x] 1.3 Define canonical Kotlin helper naming-to-token mappings for all supported font helpers on base and variant scopes

Font selector inventory confirmed from `CssController` (pre-migration):  
`body.browse-body`, `body.art-body`, `.brand h1`, `.sidebar-title`, `.content-top h1`, `.author-name`, `.author-handle`, `.post-stats`, `.post-open-link`, `.art-title`, `.art-byline`, `.art-description h2, .comments h2`, `.comment-author`, `.comment-handle`, `.richtext-mention`, `.richtext-tag`.

## 2. Font Utility Compiler Hooks

- [x] 2.1 Add typed font compiler token models for font-family, font-size, and font-weight metadata under `:libs:kolo-styles`
- [x] 2.2 Implement a dedicated font parser hook that accepts only allow-listed tokens (including existing variant-prefix semantics), with font-weight acceptance limited to `font-thin` through `font-black`, and preserves permissive diagnostics for unsupported/malformed tokens
- [x] 2.3 Implement a dedicated font generator hook that emits font CSS declarations through the shared compiler `CssBuilder`, including explicit font-weight mapping to numeric values (100..900)
- [x] 2.4 Register font parser/generator hooks with existing Spring hook discovery so font utilities flow through `/css/generated/kolo.css`

## 3. Font DSL API

- [x] 3.1 Add typed `KoloScope` font helpers in `kolostyles.dsl.font` for all supported family/size/weight tokens, including one helper per allow-listed weight token from `font-thin` through `font-black`
- [x] 3.2 Add typed `KoloVariantScope` font helpers with identical token coverage and chaining behavior, including the full `font-thin` through `font-black` weight set
- [x] 3.3 Ensure each helper emits canonical Tailwind-style token strings used by compiler canonicalization

## 4. Runtime and Canonicalization Integration

- [x] 4.1 Verify font tokens participate in existing deterministic token canonicalization and URL generation without changing delimiter/order contracts
- [x] 4.2 Verify state/media variants (`hover`, `focus`, `focus-visible`, `active`, `visited`, `sm`/`md`/`lg`/`xl`/`2xl`) generate correct selectors and media wrappers for font utilities
- [x] 4.3 Verify mixed spacing/display/font token requests compile deterministically through the shared parser/generator pipeline

## 5. Browse and Art Font Ownership Migration

- [x] 5.1 Redefine current default app font baseline to Tailwind sans semantics via `--font-sans`
- [x] 5.2 Migrate font declarations from `CssController` to co-located `kolo { ... }` font helpers in small selector/component batches
- [x] 5.3 Remove only migrated `font-family`, `font-size`, and `font-weight` declarations from generated page CSS while keeping non-migrated rules as explicit temporary exceptions
- [x] 5.4 Keep side-by-side stylesheet delivery (`kolo.css` + page CSS) unchanged during incremental migration

## 6. Test Coverage Updates

- [x] 6.1 Add parser tests for full font token acceptance and unsupported/malformed token diagnostics behavior, including explicit acceptance/rejection coverage around `font-thin` through `font-black` and disallowing custom weights
- [x] 6.2 Add generator tests for base, pseudo-state, and media-variant font CSS emission, including assertions for the font-weight numeric mapping (`100`..`900`)
- [x] 6.3 Add compiler/controller integration tests for `/css/generated/kolo.css` with mixed spacing/display/font tokens
- [x] 6.4 Add runtime render tests asserting class emission and canonicalized `kolo.css` href generation from font DSL usage
- [x] 6.5 Add app-level stylesheet/render tests proving migrated selectors no longer receive duplicate font declarations from `CssController` and preserve typography parity

## 7. Docs and OpenSpec Validation

- [x] 7.1 Update relevant docs (`docs/ARCHITECTURE.md`, `docs/DECISIONS.md`, `docs/TESTING.md`, `docs/GLOSSARY.md`) to reflect new font utilities and migration ownership boundaries
- [x] 7.2 Run project tests for changed paths and resolve any failures before handoff
- [x] 7.3 Run `openspec validate --all` and resolve all validation issues for the change artifacts
