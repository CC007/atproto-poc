# BA-009: Support blocked post type placeholder in browse timeline

## Metadata
- ID: `BA-009`
- Status: `todo`
- Owner: `ai`
- Created: `2026-05-05 01:21`
- Updated: `2026-05-05 01:21`
- Related Human Issue: none

## Goal
Handle feed entries where the rendered post record type is `app.bsky.feed.defs#blockedPost` without breaking browse rendering. Show the author banner and a clear placeholder message that the post is blocked.

## Scope
- In scope:
  - Detect blocked-post records in browse post rendering.
  - Render a stable fallback card message for blocked posts.
  - Preserve author display when author metadata is present.
  - Add/extend tests for blocked-post rendering behavior.
- Out of scope:
  - Changes to Bluesky API fetch behavior.
  - New moderation workflows or user-specific unblocking features.
  - Client-side dynamic hydration for blocked content.

## Plan
- [ ] Inspect record union handling in `postSummary` and identify blocked-post type representation.
- [ ] Add blocked-post rendering branch with copy like "This post is blocked" while preserving author header.
- [ ] Keep existing rendering behavior unchanged for supported and unknown record types.
- [ ] Add a focused test in `PostSummaryTest` (or a dedicated browse renderer test) for blocked-post output.
- [ ] Run targeted tests, then run `./gradlew test` if changes broaden.
- [ ] Update docs if implementation affects architecture/testing notes.

## Progress Log
- `2026-05-05 01:21`: Task created.

## How Completed
_To be filled in on completion._

## Verification
_To be filled in on completion._

## Follow-ups
- [ ] Decide whether blocked-post cards should include moderation reason text when available.
- [ ] Consider adding browse integration coverage for mixed timelines with blocked records.

