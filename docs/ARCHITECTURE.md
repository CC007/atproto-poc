# Architecture

## Overview
BlueArt is a Kotlin + Spring Boot proof-of-concept for browsing Bluesky/ATProto content with a server-rendered UI using `kotlinx.html`.

## Runtime Shape
- Server: Spring Boot MVC controllers.
- Rendering: server-side HTML generation (`kotlinx.html`).
- Build: Gradle Kotlin DSL.
- Styling: static CSS under `src/main/resources/static/css/`.

## Main Components
- `BrowseController` renders timeline browsing routes.
- `PostSummary` renders feed-card snippets and navigation links.
- `ArtContentController` renders art detail pages and comments.

## Primary Routes
- `GET /browse` timeline cards.
- `GET /art/{cid}?uri=...` art detail page.

## Data Flow (High Level)
1. Controller fetches Bluesky/ATProto content through `work.socialhub.kbsky` APIs.
2. Domain objects are mapped directly into HTML views.
3. CSS in `static/css/` controls layout and component presentation.

## Current Gaps
- `/art/{cid}` fallback lookup is best-effort when only CID is provided.
- Comment rendering is flattened instead of a fully nested tree.

