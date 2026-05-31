# Tasks: Create a dummy account and content

## 1. Add localhost as a recognized network option

- [ ] 1.1 Add `localhost` as a recognized network option in the existing login form network selector.
- [ ] 1.2 When `localhost` is selected, route the login submission to the dummy-account authentication path instead of Bluesky; keep all other networks on the existing Bluesky path.
- [ ] 1.3 Apply any minimal bug fixes to the login process if needed.

## 2. Runtime dummy auth and timeline fixtures

- [x] 2.1 Add a Spring controller that handles login requests targeting `localhost` and authenticates the dummy account (username: `dummy.localhost`, password: `1234`).
- [x] 2.2 Add a Spring controller that serves the deterministic dummy timeline fixture in ATProto response format for authenticated dummy-account requests.
- [ ] 2.3 Ensure both controllers are always available when the application runs and do not require a feature flag or separate deployment mode.

## 3. Dummy content coverage and media stability

- [ ] 3.1 Seed the dummy timeline with one or more posts for every post type currently supported by `PostSummary` rendering.
- [ ] 3.2 Add matching detail-route data for each dummy timeline item so every card can open on the standard detail page.
- [ ] 3.3 Populate dummy image, video, and GIF-style embeds with stable remote URLs that can be verified over time.

## 4. Verification and documentation

- [ ] 4.1 Add focused tests for localhost network detection and the real-vs-dummy authentication branch selection.
- [ ] 4.2 Add controller or integration tests that verify the dummy timeline and detail routes render through the existing browse/detail flow.
- [ ] 4.3 Update the relevant docs pages or task records if implementation details change architecture, testing, or security notes.
