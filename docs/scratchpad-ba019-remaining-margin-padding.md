# BA-019 Scratchpad — Remaining `margin`/`padding` in `CssController`

Generated: 2026-05-26
Source file: `app/src/main/kotlin/com/github/cc007/blueart/endpoints/styling/CssController.kt`

Spacing scale: step N = N × 0.25 rem (assuming root font-size 16px)
Approximation policy for this scratchpad: nearest step is acceptable when visual delta is small; rem and px deltas are noted.

---

## Browse stylesheet (`buildBrowseStyles`)

| # | CssController selector | Selector type | Property / value | Nearest kolo token | Clean step? | Migration readiness |
|---|------------------------|---------------|------------------|--------------------|-------------|---------------------|
| 1 | `.post-author` | Simple class | `padding: 0.35rem 0.45rem` | `py(1)` + `px(2)` | **–** (1.4 / 1.8 steps) | ✅ Approx ok — `0.35rem` (5.6px) -> `0.25rem` (4px), delta `-0.10rem` (`-1.6px`); `0.45rem` (7.2px) -> `0.5rem` (8px), delta `+0.05rem` (`+0.8px`); easy to apply on `.post-author` |
| 2 | `.embed-media-grid` | Simple class | `marginTop = 0.5.rem` | `mt(2)` | ✅ | ✅ Ready — single render site in `PostSummary.kt` (`div(classes = "embed-media-grid")`); apply `kolo { mt(2) }` and remove from `CssController` |
| 3 | `.embed-media-grid-main .embed-blur-clip,`<br>`.embed-media-grid-side .embed-blur-clip` | Descendant (nested, multi-selector) | `marginTop = 0.px` | `mt(0)` | ✅ | ⚠️ Likely already redundant — `embedThumbnail` in `PostSummary.kt` already calls `kolo { m(0) }` on `.embed-blur-clip`; verify and delete this CSS rule directly |
| 4 | `.post-stats` | Simple class | `padding-top: 0.5rem` | `pt(2)` | ✅ | ✅ Ready — single render site in `PostSummary.kt` (`div(classes = "post-stats")`); apply `kolo { pt(2) }` and remove `raw("padding-top", "0.5rem")` from `CssController` |

---

## Art stylesheet (`buildArtStyles`)

| # | CssController selector | Selector type | Property / value | Nearest kolo token | Clean step? | Migration readiness |
|---|------------------------|---------------|------------------|--------------------|-------------|---------------------|
| 5 | `.art-layout` | Simple class | `margin: 0 auto` | `mx(auto)` / `my(0)` | **–** (`auto` not a step value) | ❌ `auto` is not representable as a kolo step token; keep in `CssController` or add a dedicated `mx-auto` utility to kolo-styles |
| 6 | `.art-description` | Simple class | `marginTop = 0.9.rem` | `mt(4)` | **–** (3.6 steps) | ✅ Approx ok — `0.9rem` (14.4px) -> `1rem` (16px), delta `+0.1rem` (`+1.6px`); simple class, easy to apply |
| 7 | `.art-description h2, .comments h2` | Descendant (nested), multi-selector | `margin: 0 0 0.6rem` | `mt(0)` + `mx(0)` + `mb(2)` | **–** (bottom = 2.4 steps) | ⚠️ Approx ok but refactor needed — bottom `0.6rem` (9.6px) -> `0.5rem` (8px), delta `-0.1rem` (`-1.6px`); requires class placement for both heading contexts instead of one shared descendant selector |
| 8 | `.comment` | Compound class (`.comment` base block) | `padding = Padding(0.6.rem)` | `p(2)` | **–** (2.4 steps) | ✅ Approx ok — `0.6rem` (9.6px) -> `0.5rem` (8px), delta `-0.1rem` (`-1.6px`); easy on `.comment` |
| 9 | `.comment.depth-1` | Compound class | `margin-left: 0.8rem` | `ml(3)` | **–** (3.2 steps) | ⚠️ Approx ok but mapping needed — `0.8rem` (12.8px) -> `0.75rem` (12px), delta `-0.05rem` (`-0.8px`); depth classes are computed, so add a depth->kolo token map |
| 10 | `.comment.depth-2` | Compound class | `margin-left: 1.6rem` | `ml(6)` | **–** (6.4 steps) | ⚠️ Approx ok with same mapping approach — `1.6rem` (25.6px) -> `1.5rem` (24px), delta `-0.1rem` (`-1.6px`) |
| 11 | `.comment.depth-3` | Compound class | `margin-left: 2.4rem` | `ml(10)` | **–** (9.6 steps) | ⚠️ Approx ok with same mapping approach — `2.4rem` (38.4px) -> `2.5rem` (40px), delta `+0.1rem` (`+1.6px`) |
| 12 | `.comment.depth-4` | Compound class | `margin-left: 3.2rem` | `ml(13)` | **–** (12.8 steps) | ⚠️ Approx ok with same mapping approach — `3.2rem` (51.2px) -> `3.25rem` (52px), delta `+0.05rem` (`+0.8px`) |
| 13 | `@media (max-width: 700px)` → `.art-layout` | Simple class (responsive override) | `padding = Padding(0.75.rem)` | `p(3)` | ✅ | ⚠️ Exact value but responsive blocker remains — kolo media variants are **min-width** while this rule is **max-width** |
| 14 | `@media (max-width: 700px)` → `.art-card, .comments` | Multi-selector (responsive override) | `padding = Padding(0.8.rem)` | `p(3)` | **–** (3.2 steps) | ⚠️ Approx ok (`0.8rem`/12.8px -> `0.75rem`/12px, delta `-0.05rem`/`-0.8px`) but still blocked by max-width + multi-selector structure |

---

## Summary

| Category | Count | Items |
|---|---|---|
| Exact step + simple selector → **ready now** | 2 | #2, #4 |
| Approx step + simple selector → **ready with minor visual delta** | 3 | #1, #6, #8 |
| Likely redundant rule → **delete after quick verify** | 1 | #3 |
| Approx step but selector/refactor work needed | 5 | #7, #9, #10, #11, #12 |
| Responsive strategy blocker (`max-width`) | 2 | #13, #14 |
| Non-step semantic blocker (`auto`) | 1 | #5 |

---

## Actionable next steps (in priority order)

1. **Delete likely redundant rule (#3)**: verify `kolo { m(0) }` in `embedThumbnail` covers `.embed-blur-clip`, then remove `.embed-media-grid-main/.embed-media-grid-side .embed-blur-clip { margin-top: 0; }` from `CssController`.
2. **Migrate exact ready items (#2, #4)**: add `kolo { mt(2) }` to `.embed-media-grid` and `kolo { pt(2) }` to `.post-stats`; remove corresponding CSS declarations.
3. **Migrate low-risk approximations (#1, #6, #8)**: apply `py(1)+px(2)` for `.post-author`, `mt(4)` for `.art-description`, and `p(2)` for `.comment`; visually check browse/art pages for spacing parity.
4. **Refactor nested/depth selectors (#7, #9-#12)**: add explicit heading/comment depth token mapping in markup (or helper functions) before removing CSS descendant/compound rules.
5. **Resolve responsive/max-width blockers (#13, #14)**: either keep these in `CssController`, or extend kolo variant support for max-width patterns and multi-selector responsive wiring.
6. **Handle semantic non-step case (#5)**: keep `margin: 0 auto` in CSS for now, or add an `mx-auto` utility to kolo-styles.


