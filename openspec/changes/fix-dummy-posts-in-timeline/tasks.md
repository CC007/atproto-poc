## 1. Fixture Data Updates

- [ ] 1.1 Update image-based dummy fixture posts in `DummyAtProtoFixtures.kt` to use stable working HTTPS remote image URLs
- [ ] 1.2 Verify deterministic fixture ordering/cursor behavior remains unchanged after media URL updates

## 2. Timeline Record Rendering

- [ ] 2.1 Add a dedicated `GraphFollow` rendering branch in `PostSummary.kt` record dispatch
- [ ] 2.2 Render follow cards with actor attribution message (e.g., `@dummy followed @target`) instead of unsupported placeholder text

## 3. Contract and Regression Safety

- [ ] 3.1 Confirm `DummyAtProtoTimelineController` request/response shape and pagination contract are unchanged
- [ ] 3.2 Ensure `/browse` timeline and `/art/{cid}` routing behavior is preserved for non-follow and real-network flows

## 4. Test Coverage Updates

- [ ] 4.1 Extend dummy timeline fixture/controller tests to assert image fixture URLs are non-empty direct HTTPS media URLs
- [ ] 4.2 Extend `PostSummary` rendering tests to assert follow records render supported follow activity and no unsupported placeholder text
- [ ] 4.3 Update or add dummy login/browse integration assertions to cover follow-card rendering in timeline output

## 5. Verification

- [ ] 5.1 Run targeted tests for dummy timeline endpoints and timeline card rendering paths
- [ ] 5.2 Resolve any failing assertions introduced by fixture or follow-rendering changes
