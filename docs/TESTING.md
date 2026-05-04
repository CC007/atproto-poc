# Testing

## Scope
This project currently relies on Gradle test tasks and targeted checks around changed paths.

## Baseline Commands
```bash
./gradlew test
```

## Change-Focused Guidance
- Prefer targeted tests for narrow changes.
- Run `./gradlew test` when edits are broad or touch shared code paths.
- Document any unverified areas in handoff notes when checks cannot run.

## Current Gaps
- No dedicated automated tests currently cover:
  - browse cards containing `Open artwork` links
  - `/art/{cid}` rendering media, description, and comments

## Recent Coverage Additions
- `RichTextFacetRendererTest` validates UTF-8 byte-offset slicing plus defensive handling of malformed/overlapping link, tag, and mention facets.

## Suggested Additions
- Lightweight integration tests for `GET /browse` and `GET /art/{cid}` rendering expectations.

