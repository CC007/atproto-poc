## 1. Fixture Data Updates

- [x] 1.1 Update image-based dummy fixture posts in `DummyAtProtoFixtures.kt` to use stable working HTTPS remote image URLs
- [x] 1.2 Verify deterministic fixture ordering/cursor behavior remains unchanged after media URL updates

## 2. Timeline Record Rendering

- [x] 2.1 Add a dedicated `GraphFollow` rendering branch in `PostSummary.kt` record dispatch
- [x] 2.2 Render follow cards with actor attribution message (e.g., `@dummy followed @target`) instead of unsupported placeholder text

## 3. Contract and Regression Safety

- [x] 3.1 Confirm `DummyAtProtoTimelineController` request/response shape and pagination contract are unchanged
- [x] 3.2 Ensure `/browse` timeline and `/art/{cid}` routing behavior is preserved for non-follow and real-network flows

## 4. Test Coverage Updates

- [x] 4.1 Extend dummy timeline fixture/controller tests to assert image fixture URLs are non-empty direct HTTPS media URLs
- [x] 4.2 Extend `PostSummary` rendering tests to assert follow records render supported follow activity and no unsupported placeholder text
- [x] 4.3 Update or add dummy login/browse integration assertions to cover follow-card rendering in timeline output

## 5. Verification

- [x] 5.1 Run targeted tests for dummy timeline endpoints and timeline card rendering paths
- [x] 5.2 Resolve any failing assertions introduced by fixture or follow-rendering changes
