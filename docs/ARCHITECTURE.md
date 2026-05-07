# Architecture

## Overview
BlueArt is a Kotlin + Spring Boot web application for browsing Bluesky/ATProto content with a server-rendered UI using `kotlinx.html`.

## Runtime Shape
- Server: Spring Boot MVC controllers.
- Rendering: server-side HTML generation (`kotlinx.html`).
- Build: Gradle Kotlin DSL with a multi-module layout (`:app` and `:libs`).
- Styling: Kotlin CSS DSL endpoints under `/css/generated/*.css` generate full browse and art stylesheets from Kotlin in `CssController`.

## Module Boundaries
- `:app`: executable Spring Boot web application module. Contains controllers, HTML renderers, routes, and runtime wiring.
- `:libs`: reusable-library group module reserved for future shared libraries (including styling modules such as `kolo-styles`).

## Package Layout (`:app`)
All endpoint controllers live under `com.github.cc007.blueart.endpoints`, preserving their function-specific subdirectory names:

| Package | Contents |
|---|---|
| `endpoints/auth` | `LoginController` |
| `endpoints/browse` | `BrowseController` |
| `endpoints/content/art` | `ArtContentController` |
| `endpoints/error` | `ErrorController` |
| `endpoints/styling` | `CssController` |
| `auth` | `AtProtoAuthentication`, `AtProtoAuthenticationProvider`, `SecurityConfig` (non-controller auth wiring) |
| `components` | `Header`, `PostSummary`, `RichTextFacetRenderer` (shared HTML components) |
| `util` | `Result`, `User` |

## Main Components
- `BrowseController` (`endpoints/browse`) renders timeline browsing routes.
- `PostSummary` renders feed-card snippets and navigation links, including media-aware card behavior (text-only cards clamp overflow; cards with embeds hide body text and use thumbnail/gallery layouts).
- `ArtContentController` (`endpoints/content/art`) renders art detail pages and comments.
- `RichTextFacetRenderer` converts ATProto rich-text facet byte ranges into safe link/tag/mention HTML segments shared by browse and art detail rendering.

## Primary Routes
- `GET /browse` timeline cards.
- `GET /art/{cid}?uri=...` art detail page.

## Data Flow (High Level)
1. Controller fetches Bluesky/ATProto content through `work.socialhub.kbsky` APIs.
2. Domain objects are mapped directly into HTML views.
3. Controllers link `/css/generated/*.css`; styles are emitted from Kotlin CSS DSL in `CssController`.

## Current Gaps
- `/art/{cid}` fallback lookup is best-effort when only CID is provided.
- Comment rendering is flattened instead of a fully nested tree.

