# Tasks: Move padding and margin definitions from CssController to kolo styles

> Derived from `design.md`, `specs/kolo-spacing-ownership/spec.md`, and
> `docs/scratchpad-ba019-remaining-margin-padding.md`.
>
> Reference: spacing scale is `N × 0.25 rem` (steps 0–16). Nearest-step rounding
> is acceptable when the visual delta is ≤ 0.10 rem per `design.md` §Decision 3.

---

## 1. Browse stylesheet — PostSummary simple migrations

These four declarations are in `CssController.buildBrowseStyles()`. Each has a
single render site in `PostSummary.kt` where a `kolo { }` call can be added.

- [x] 1.1 **`.post-author` padding** — add `kolo { py(1); px(2) }` to the
  `div(classes = "post-author")` block in `PostSummary.kt` (~line 125);
  remove `padding = Padding(0.35.rem, 0.45.rem)` from `CssController`.
  _(Approx: `py` 0.35→0.25 rem −1.6 px; `px` 0.45→0.50 rem +0.8 px.)_

- [x] 1.2 **`.embed-media-grid` margin-top** — add `kolo { mt(2) }` to the
  `div(classes = "embed-media-grid")` block in `PostSummary.renderImageGallery`;
  remove `marginTop = 0.5.rem` from `CssController`.
  _(Exact step match.)_

- [x] 1.3 **`.embed-media-grid-main/.embed-media-grid-side .embed-blur-clip` margin-top** —
  confirm that `kolo { m(0) }` on `.embed-blur-clip` inside `embedThumbnail` already
  covers the zero-margin intent; then delete the descendant selector block
  `".embed-media-grid-main .embed-blur-clip, .embed-media-grid-side .embed-blur-clip" { marginTop = 0.px; ... }`
  from `CssController` (keep the `height = 100.pct` line in place if needed).

- [x] 1.4 **`.post-stats` padding-top** — add `kolo { pt(2) }` to the
  `div(classes = "post-stats")` block in `PostSummary.kt` (~line 61);
  remove `raw("padding-top", "0.5rem")` from `CssController`.
  _(Exact step match.)_

---

## 2. Art stylesheet — ArtContentController simple migrations

These declarations in `CssController.buildArtStyles()` each map to one or two
named render sites in `ArtContentController.kt`.

- [x] 2.1 **`.art-description` margin-top** — add `kolo { mt(4) }` to the
  `section(classes = "art-description")` block in `ArtContentController.kt`;
  remove `marginTop = 0.9.rem` from `CssController`.
  _(Approx: 0.9→1.0 rem +1.6 px.)_

- [x] 2.2 **`.art-description h2` and `.comments h2` margin** — add
  `kolo { mt(0); mx(0); mb(2) }` to each `h2 { +"Description" }` and
  `h2 { +"Comments" }` call site in `ArtContentController.kt`;
  remove `margin = Margin(0.px, 0.px, 0.6.rem)` from `CssController`.
  _(Approx bottom: 0.6→0.5 rem −1.6 px. Requires inline kolo on each h2 element
  instead of one shared descendant rule.)_

- [x] 2.3 **`.comment` padding** — add `kolo { p(2) }` to the
  `article(classes = "comment depth-...")` block in `ArtContentController.kt`;
  remove `padding = Padding(0.6.rem)` from `CssController`.
  _(Approx: 0.6→0.5 rem −1.6 px.)_

---

## 3. Art stylesheet — comment depth margin-left migrations

The four `.comment.depth-{1..4}` compound-class rules produce `margin-left`
values that are not clean steps but fall within the ≤0.10 rem tolerance.

- [x] 3.1 **Add depth-to-token map in `ArtContentController.kt`** — introduce a
  local mapping and apply `kolo { ml(depthToStep(depth)) }` on each
  `article.comment` element:

  | depth | CssController value | Nearest token | Delta |
  |-------|--------------------|-----------|----|
  | 1 | 0.80 rem | `ml(3)` = 0.75 rem | −0.05 rem |
  | 2 | 1.60 rem | `ml(6)` = 1.50 rem | −0.10 rem |
  | 3 | 2.40 rem | `ml(10)` = 2.50 rem | +0.10 rem |
  | 4 | 3.20 rem | `ml(13)` = 3.25 rem | +0.05 rem |

- [x] 3.2 **Remove depth rules from `CssController`** — delete all four
  `.comment.depth-{1..4}` blocks from `CssController.buildArtStyles()`.

---

## 4. `margin: 0 auto` centering — add `mx-auto` utility

`.art-layout { margin: 0 auto }` cannot be expressed with a numeric step token;
`auto` is a semantic layout value, not a spacing scale step.

- [x] 4.1 **Add `mx-auto` parser entry in `SpacingUtilities.kt`** — extend
  `SpacingParserHook.parse()` to recognise the literal token `"mx-auto"` and
  return `StyleUtilityDefinition(token = "mx-auto", cssDeclaration = "margin-left: auto; margin-right: auto;")`.

- [x] 4.2 **Add `mx-auto` DSL helper in `KoloSpacingDsl.kt`** — add
  `fun KoloScope.mxAuto() = recordBase("mx-auto")` so render code can call
  `kolo { mxAuto() }`.

- [x] 4.3 **Apply `kolo { mxAuto() }` to `.art-layout`** — add the call to
  `main(classes = "art-layout")` in `ArtContentController.kt`;
  remove `margin = Margin(0.px, LinearDimension.auto)` from `CssController`.

- [x] 4.4 **Add `SpacingUtilitiesTest` cases for `mx-auto`** — assert that
  `parser.parse("mx-auto")` returns the correct definition and that
  `compiler.compile("mx-auto")` emits `.k-mx-auto { margin-left: auto; margin-right: auto; }`.

---

## 5. Responsive max-width declarations — document as tracked exceptions

`@media (max-width: 700px)` art rules in `CssController` use a max-width breakpoint;
kolo-styles only supports min-width media variants today. Extending kolo for max-width
variants is outside the non-goals of this change.

The two remaining declarations:
- `.art-layout { padding: 0.75 rem }` (exact `p(3)` but blocked by max-width direction)
- `.art-card, .comments { padding: 0.8 rem }` (approx `p(3)`, also blocked by max-width + multi-selector)

- [x] 5.1 **Add tracking comments in `CssController`** — annotate each of the
  two remaining responsive spacing declarations with
  `// kolo-exception: max-width responsive — migrate when kolo gains max-width variant support`
  so they are easy to locate in a future cleanup pass.

- [x] 5.2 **Record decision in `docs/DECISIONS.md`** — add an entry explaining
  that max-width responsive spacing remains in `CssController` until kolo-styles
  gains max-width breakpoint variant support (a separate future change).

---

## 6. Tests and verification

- [x] 6.1 **`CssControllerTest`** — confirm all existing rule-header coverage
  assertions still pass after each migration batch.

- [x] 6.2 **`SpacingUtilitiesTest`** — add test cases for `mx-auto` token
  parsing and CSS generation (covered by task 4.4).

- [x] 6.3 **Run `./gradlew :app:test :libs:kolo-styles:test`** — confirm
  BUILD SUCCESSFUL after all migrations.

- [ ] 6.4 **Manual visual parity check** — start the application and compare
  `/browse` and `/art/{cid}` before and after each migration group; confirm no
  visible layout regressions on browse cards, art detail, and comment threads.

---

## 7. Docs update

- [x] 7.1 Update `docs/ARCHITECTURE.md` — revise the Styling section to state
  that `CssController` no longer emits margin/padding declarations for migrated
  elements, and note the tracked responsive exceptions.

- [x] 7.2 Update `docs/DECISIONS.md` — record the `mx-auto` utility addition
  and the max-width responsive exception (task 5.2 may already cover this).

---
