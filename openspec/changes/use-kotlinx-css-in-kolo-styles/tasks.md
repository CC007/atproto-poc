## 1. Dependency and API Setup

- [ ] 1.1 In `gradle/libs.versions.toml`, define a library alias `kotlin-css` that points to the third-party library `org.jetbrains.kotlin-wrappers:kotlin-css` by default.
- [ ] 1.2 Set the `kotlin-css` library version in `gradle/libs.versions.toml` to `2025.7.14`.
- [ ] 1.3 In the Gradle module `app`, update the dependency declaration in `app/build.gradle.kts` to use the `kotlin-css` catalog library alias instead of inline coordinates.
- [ ] 1.4 In the Gradle module `libs/kolo-styles`, add/update the dependency declaration in `libs/kolo-styles/build.gradle.kts` to use the `kotlin-css` catalog library alias.
- [ ] 1.5 If the multiplatform library `org.jetbrains.kotlin-wrappers:kotlin-css` fails to resolve or fails at runtime in this JVM project, update the same catalog library alias to `org.jetbrains.kotlin-wrappers:kotlin-css-jvm` and keep both Gradle module dependency declarations unchanged.
- [ ] 1.6 Update `StyleGeneratorHook` to the new signature `(StyleUtilityDefinition, CssBuilder) -> Boolean` and import `kotlinx.css.CssBuilder`.
- [ ] 1.7 Update direct `StyleGeneratorHook` implementations in `kolo-styles` to compile with the new signature.

## 2. Compiler Pipeline Refactor

- [ ] 2.1 Refactor `KoloCssCompiler.compile()` to create one shared `CssBuilder` per compile run and pass it to each generator hook.
- [ ] 2.2 Replace string concatenation/join behavior in `KoloCssCompiler` with `CssBuilder` accumulation and final `builder.toString()` output.
- [ ] 2.3 Preserve unsupported-token handling by appending comments through `builder.raw(...)` so output structure remains equivalent.

## 3. Spacing Utility Migration to CssBuilder DSL

- [ ] 3.1 Refactor spacing declaration construction in `SpacingUtilities.kt` from string templates to a private `CssBuilder.() -> Unit` typed declaration builder.
- [ ] 3.2 Update spacing rule emission to write selector blocks directly into the shared `CssBuilder`.
- [ ] 3.3 Update media-query-wrapped spacing generation to use `CssBuilder.media(...)` while preserving current selector/media output shape.

## 4. Test Updates for Signature and Output Stability

- [ ] 4.1 Update `KoloCssCompilerTest` inline hook lambdas to `(StyleUtilityDefinition, CssBuilder) -> Boolean` and preserve original test intent.
- [ ] 4.2 Adjust spacing-related expected CSS in tests only where `CssBuilder` canonical serialization differs (for example `0px` vs `0`).
- [ ] 4.3 Keep parser tests that assert `StyleUtilityDefinition.cssDeclaration` passing without removing the field.

## 5. Verification and Regression Checks

- [ ] 5.1 Run `./gradlew :libs:kolo-styles:test` and resolve failures from formatting/signature changes.
- [ ] 5.2 Run `./gradlew :app:test` to confirm integration compatibility with shared classpath usage.
- [ ] 5.3 Run existing visual/regression checks used by the project for stylesheet output to confirm no browser-visible CSS behavior change.
