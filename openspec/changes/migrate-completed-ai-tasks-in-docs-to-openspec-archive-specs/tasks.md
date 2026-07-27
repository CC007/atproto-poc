## 1. Completed Task Inventory and Mapping

- [ ] 1.1 Inventory all `completed` rows in `docs/AI_TASKS.md` and confirm each source file exists under `docs/ai-tasks/`
- [ ] 1.2 Create a deterministic mapping table from completed task ID/title to archive slug (`openspec/archive/<slug>/`) and target capability spec(s)
- [ ] 1.3 Record migration eligibility so only `completed` tasks are included and non-completed states are explicitly excluded

## 2. Archive Artifact Migration per Completed Task

- [ ] 2.1 Create one archive change directory per mapped completed task under `openspec/archive/<slug>/`
- [ ] 2.2 Populate each archive change with `proposal.md`, `design.md`, and `tasks.md` that summarize original task intent, implementation outcome, and completion scope
- [ ] 2.3 Add provenance in each archived artifact with original task ID, title, completion date, and source file path (`docs/ai-tasks/...`)
- [ ] 2.4 Add task-local spec delta files under each archived change when the completed task introduced or changed requirements

## 3. Capability Spec Consolidation

- [ ] 3.1 Extract requirement-level statements from migrated completed tasks and normalize them into `openspec/specs/*` requirement language
- [ ] 3.2 Extract enduring requirement-level behavior from `docs/ARCHITECTURE.md`, `docs/DECISIONS.md`, `docs/GLOSSARY.md`, `docs/SECURITY.md`, and `docs/TESTING.md`
- [ ] 3.3 Merge overlapping requirements into single canonical capability requirements with source provenance notes
- [ ] 3.4 Ensure each mapped capability has complete scenarios covering migrated outcomes needed for future requirement discovery

## 4. Docs Tracking Cleanup and Guidance Updates

- [ ] 4.1 Update `docs/AI_TASKS.md` so active/incomplete and cancelled tracking remains, and Completed entries link to `openspec/archive/<slug>/`
- [ ] 4.2 Delete only the migrated completed task markdown files from `docs/ai-tasks/` after archive and spec artifacts exist
- [ ] 4.3 Update `AGENTS.md`, `.github/copilot-instructions.md`, and `CONTRIBUTING.md` to point requirement discovery to `openspec/specs/*` and `openspec/archive/*`

## 5. Structural Validation and Migration Finalization

- [ ] 5.1 Verify one-to-one coverage: every completed task has exactly one archive change and every Completed table link resolves
- [ ] 5.2 Verify requirement coverage: each new/updated requirement from migration inputs is represented in `openspec/specs/*`
- [ ] 5.3 Verify provenance integrity: archive artifacts and relevant specs reference original task IDs/titles/source paths
- [ ] 5.4 Add final migration summary notes in this change artifacts, including coverage totals and any intentionally deferred items
