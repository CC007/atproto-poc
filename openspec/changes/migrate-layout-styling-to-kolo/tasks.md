# Tasks: migrate layout styling to kolo

## 1. Scope Inventory and Mapping

- [ ] 1.1 Read the TailwindCSS docs (https://tailwindcss.com/docs), specifically the layout section and its pages (from aspect-ratio to z-index),
- [ ] 1.2 Inventory `CssController` browse/art layout declarations for display, box-sizing, overflow, position, offsets (`inset/top/right/bottom/left`), z-index, and object-fit
- [ ] 1.3 Map each inventory item to its render call site (`Header`, browse controllers/components, art controllers/components) and classify as mappable or explicit exception
- [ ] 1.4 Freeze the usage-driven layout token allow-list and variant coverage for each used family, including positive-integer `z-<n>` support

## 2. Layout Package Structure and Display Relocation

- [ ] 2.1 Create/align `kolostyles` layout package structure using the hybrid model (`layout.display`, `layout.offset`, shared layout-level files for smaller utility groups)
- [ ] 2.2 Move existing display parser/generator/token wiring under layout packages without changing accepted tokens or emitted CSS behavior
- [ ] 2.3 Move existing display DSL helpers to `kolostyles.dsl.layout` for both `KoloScope` and `KoloVariantScope` without changing helper behavior

## 3. Shared Media-Variant Generator Refactor

- [ ] 3.1 Add shared media-variant emission support to `StyleGeneratorHook` (or shared base helper) for reusable selector/media scaffolding
- [ ] 3.2 Migrate spacing, display, font, and sizing generators to the shared media helper without changing CSS output contracts
- [ ] 3.3 Remove duplicated per-generator media wrapping logic after parity migration

## 4. Non-Display Layout Utility Compiler Support

- [ ] 4.1 Add typed layout token models for box-sizing, overflow, position, offset, z-index, and object-fit families
- [ ] 4.2 Implement layout parser hook support for the scoped allow-list, including full family variants and positive-integer parsing for `z-<n>`
- [ ] 4.3 Implement layout generator hook support for all parsed non-display layout tokens and register hooks in Spring compiler wiring

## 5. Typed Layout DSL Helpers

- [ ] 5.1 Add `KoloScope` layout helpers in `kolostyles.dsl.layout` for all migrated non-display families
- [ ] 5.2 Add matching `KoloVariantScope` helpers with identical token coverage and variant-chaining behavior
- [ ] 5.3 Add grouped offset helpers for `inset/top/right/bottom/left` under the dedicated layout offset structure

## 6. App Ownership Migration and CSS Cleanup

- [ ] 6.1 Migrate mappable browse render-site layout declarations from `CssController` ownership to co-located `kolo { ... }` layout helpers
- [ ] 6.2 Migrate mappable art render-site layout declarations from `CssController` ownership to co-located `kolo { ... }` layout helpers
- [ ] 6.3 Remove duplicated migrated layout declarations from generated page CSS in `CssController` selector-by-selector in the same change steps

## 7. Exception Handling and Ownership Assertions

- [ ] 7.1 Keep non-mappable and max-width-dependent layout declarations in `CssController` with explicit `kolo-exception` markers
- [ ] 7.2 Extend `CssControllerTest` ownership assertions to verify migrated layout properties are absent while exception rules remain present
- [ ] 7.3 Add or update docs comments/notes where needed to keep remaining exceptions auditable

## 8. Layout Utility Test Coverage

- [ ] 8.1 Add parser tests covering accepted/rejected tokens for each new non-display layout family, including `z-auto` and representative `z-<n>` values
- [ ] 8.2 Add generator tests for base, state variant, and min-width breakpoint emission for each new layout family
- [ ] 8.3 Add mixed-family compiler/controller tests for deterministic output across spacing + layout + font + sizing tokens

## 9. Runtime Integration and Handoff Validation

- [ ] 9.1 Add runtime render tests asserting layout DSL class emission and canonicalized `/css/generated/kolo.css` token delivery
- [ ] 9.2 Run relevant Gradle test suites for `:libs:kolo-styles` and `:app` migration paths and resolve failures
- [ ] 9.3 Run `openspec validate --all` and resolve all validation issues for the change artifacts
