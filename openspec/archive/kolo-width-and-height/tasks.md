## 1. Compiler foundation and shared types

- [x] 1.1 Consolidate `MediaVariant` into one shared compiler definition and update spacing/font imports
- [x] 1.2 Add `compiler/sizing` package scaffolding with token, parser hook, and generator hook entry points

## 2. Sizing token model and parser

- [x] 2.1 Implement `SizingToken` as a single token data class with `LinearDimension` value
- [x] 2.2 Implement `SizingParserHook` for `w-*`, `h-*`, `min-w-*`, `max-w-*`, `min-h-*`, `max-h-*`, and `size-*` prefixes
- [x] 2.3 Add explicit allow-list mappings for named, numeric scale, breakpoint, and fractional sizing tokens
- [x] 2.4 Ensure unsupported sizing tokens return `null` and produce no generated sizing rule

## 3. Sizing CSS generation

- [x] 3.1 Implement `SizingGeneratorHook` prefix-to-property mapping for all six sizing families
- [x] 3.2 Implement `size-*` generation to emit both `width` and `height` declarations
- [x] 3.3 Register parser and generator sizing hooks as Spring components in the existing pipeline

## 4. Typed DSL surface

- [x] 4.1 Add `dsl/sizing` helpers on `KoloScope` for all supported sizing utility families and tokens
- [x] 4.2 Add matching `dsl/sizing` helpers on `KoloVariantScope` for variant-aware sizing utilities
- [x] 4.3 Wire sizing DSL package exports so call sites can use typed helpers without local workarounds

## 5. App migration and exception handling

- [x] 5.1 Inventory current `CssController` and static CSS sizing declarations by selector/value
- [x] 5.2 Migrate Tailwind-mappable sizing declarations to co-located `kolo { ... }` usage
- [x] 5.3 Resolve the two `kolo-exception: max-width responsive` entries using new max-width breakpoint tokens
- [x] 5.4 Keep arbitrary-value and css-var sizing rules in place with explicit `// kolo-exception` reason comments

## 6. Test coverage and final verification

- [x] 6.1 Add parser tests for accepted and rejected tokens per sizing sub-family
- [x] 6.2 Add generator tests for property emission across all sizing prefixes, including `size-*` dual output
- [x] 6.3 Add DSL/runtime tests for class emission and generated stylesheet integration
- [x] 6.4 Run project tests relevant to `:libs:kolo-styles` and migrated rendering paths
