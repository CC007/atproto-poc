## 1. Scope Alignment

- [ ] 1.1 Confirm change scope includes both `:libs:kolo-styles` display capability and migration of existing app display declarations
- [ ] 1.2 Enumerate the supported Tailwind display tokens and freeze the canonical allow-list for parser + DSL mapping
- [ ] 1.3 Define Kotlin helper naming-to-token mapping (e.g., `inlineFlex -> inline-flex`) to lock one-to-one API semantics

## 2. Compiler Display Hook Implementation

- [ ] 2.1 Add `DisplayToken` model under `kolostyles.compiler.display`
- [ ] 2.2 Implement `DisplayParserHook` to accept only the allow-listed display tokens (with existing variant prefix semantics)
- [ ] 2.3 Implement `DisplayGeneratorHook` to emit `display: ...;` declarations for accepted base tokens
- [ ] 2.4 Register display parser/generator hooks as Spring components and wire into existing compiler hook discovery

## 3. DSL Display API Implementation

- [ ] 3.1 Add base-scope display helpers in `kolostyles.dsl.display` for all supported tokens
- [ ] 3.2 Add variant-scope display helpers in `kolostyles.dsl.display` with identical token coverage
- [ ] 3.3 Ensure each helper records the canonical Tailwind token string used by compiler canonicalization

## 4. Variant and Emission Behavior Coverage

- [ ] 4.1 Extend generator behavior to produce correct selectors for state variants (`hover`, `focus`, `focus-visible`, `active`, `visited`)
- [ ] 4.2 Extend generator behavior to produce correct media-wrapped rules for breakpoint variants (`sm`, `md`, `lg`, `xl`, `2xl`)
- [ ] 4.3 Validate mixed spacing + display token compilation preserves deterministic canonical token ordering and URL shape

## 5. Test Suite Updates

- [ ] 5.1 Add parser tests for full display token acceptance and unknown-token diagnostics (`unsupported`/`unparsed`)
- [ ] 5.2 Add generator tests for base, pseudo-state, and media-variant CSS output for display utilities
- [ ] 5.3 Add compiler/controller integration tests for mixed spacing + display request flow through `/css/generated/kolo.css`
- [ ] 5.4 Add runtime render tests for canonicalized href/class emission using new display DSL helpers
- [ ] 5.5 Add mapping tests that assert every Kotlin DSL helper emits the exact expected Tailwind-compatible token string

## 6. App Display Migration

- [ ] 6.1 Inventory current display declarations in app CSS generators/static styles used by browse/art rendering
- [ ] 6.2 Replace migrated display declarations with equivalent display DSL usage at render call sites
- [ ] 6.3 Remove migrated `display` declarations from page/static CSS while preserving non-covered rules as explicit temporary exceptions
- [ ] 6.4 Add/extend browse/art rendering and stylesheet tests to assert display-layout parity after migration

## 7. Documentation and Final Validation

- [ ] 7.1 Update relevant docs to reflect new display utility capability and display ownership migration
- [ ] 7.2 Run project test/validation commands for changed paths and resolve failures
- [ ] 7.3 Run `openspec validate --all` and resolve all reported issues before handoff
