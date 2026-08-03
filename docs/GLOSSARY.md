# Glossary

## Application Terms

### ATProto
Decentralized social networking protocol used by Bluesky.

### Bluesky
Social platform built on ATProto.

### CID
Content identifier used to reference immutable data.

### URI (ATProto post URI)
Canonical post identity used for reliable fetch and thread lookup.

### Feed Card
A compact timeline item rendered on `/browse`.

### Localhost Dummy Network
The reserved `localhost` login-network option that routes form login through the in-app dummy ATProto controllers.

### Art Detail Page
Route `/art/{cid}` that renders the primary embed, post text, and comments.

### `kotlinx.html`
Kotlin DSL used in this project to render server-side HTML.

### AI Task
A repository-tracked work item executed by an AI assistant, documented with status, progress notes, and completion details.

### AI Task Index
`docs/AI_TASKS.md`, the status board that links active and completed task records in `docs/ai-tasks/`.

---

## Kolo Styling Library (`:libs:kolo-styles`)

Terms specific to the co-located utility styling infrastructure. See also [docs/DECISIONS.md § D-007](DECISIONS.md).

### `kolo { }`
Element-attached DSL extension (`fun HTMLTag.kolo(block: KoloScope.() -> Unit)`) that records utility tokens into the active request-scoped context and attaches generated class names to the current HTML element. No-ops silently when called outside a `renderKoloHtml` context.

### `KoloScope`
DSL receiver inside a `kolo { }` block. Records base tokens directly into the sink and provides `variant(name)` to enter a variant scope. Does not hold variant state itself.

### `KoloVariantScope`
DSL receiver returned by `KoloScope.variant()` or `KoloVariantScope.variant()`. Owns the accumulated variant chain and prepends it when `recordBase(token)` is called.

### Canonical Token / Canonicalization
The normalization step applied to collected Kolo tokens before the `kolo.css` URL is emitted: trim, drop empties, reject `;` and `[…]` tokens, deduplicate, sort by `(group, variantCount, variantChain, baseUtility, token)`, and join with `;`.

### `renderKoloHtml`
Wrapper around `createHTML()` that installs a request-scoped `KoloRenderContext` (via `ThreadLocal`), runs the HTML block, then replaces the `koloStylesheetLink()` placeholder with the final canonicalized `/css/generated/kolo.css?version=…&kolo=…` URL.

### `koloStylesheetLink()`
`HEAD` extension that emits a `<link rel="stylesheet">` pointing to the current render context's placeholder, which `renderKoloHtml` replaces post-render with the finalized kolo.css href.

### Spacing Utilities
Margin/padding typed DSL helpers (`m()`, `mt()`, `mb()`, `ml()`, `mr()`, `mx()`, `my()`, `p()`, `pt()`, `pb()`, `pl()`, `pr()`, `px()`, `py()`) available on both `KoloScope` and `KoloVariantScope` via `SpacingDsl.kt`. Each function records a Tailwind-style token (e.g., `m(0)` → `"m-0"`, `p(4)` → `"p-4"`) and generates a `k-`-prefixed CSS class (e.g., `.k-m-0 { margin: 0; }`).

### `SpacingParserHook` / `SpacingGeneratorHook`
Spring `@Component` implementations that provide spacing token parsing (`StyleParserHook`) and CSS rule generation (`StyleGeneratorHook`) for the production compiler pipeline.

### `KoloCssCompiler` bean wiring
`KoloCssCompiler` is a Spring `@Service` that receives injected `List<StyleParserHook>` and `List<StyleGeneratorHook>`; Spring discovers hook implementations (including spacing hooks) as beans and supplies them automatically.



