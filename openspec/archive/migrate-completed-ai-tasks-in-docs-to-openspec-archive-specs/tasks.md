## 1. Completed Task Inventory and Mapping

- [x] 1.1 Inventory all `completed` rows in `docs/AI_TASKS.md` and confirm each source file exists under `docs/ai-tasks/`
- [x] 1.2 Create a deterministic mapping table from completed task ID/title to archive slug (`openspec/archive/<slug>/`) and target capability spec(s)
- [x] 1.3 Record migration eligibility so only `completed` tasks are included and non-completed states are explicitly excluded

## 2. Archive Artifact Migration per Completed Task

- [x] 2.1 Create one archive change directory per mapped completed task under `openspec/archive/<slug>/`
- [x] 2.2 Populate each archive change with `proposal.md`, `design.md`, and `tasks.md` that summarize original task intent, implementation outcome, and completion scope
- [x] 2.3 Add provenance in each archived artifact with original task ID, title, completion date, and source file path (`docs/ai-tasks/...`)
- [x] 2.4 Add task-local spec delta files under each archived change when the completed task introduced or changed requirements

## 3. Capability Spec Consolidation

- [x] 3.1 Extract requirement-level statements from migrated completed tasks and normalize them into `openspec/specs/*` requirement language
- [x] 3.2 Extract enduring requirement-level behavior from `docs/ARCHITECTURE.md`, `docs/DECISIONS.md`, `docs/GLOSSARY.md`, `docs/SECURITY.md`, and `docs/TESTING.md`
- [x] 3.3 Merge overlapping requirements into single canonical capability requirements with source provenance notes
- [x] 3.4 Ensure each mapped capability has complete scenarios covering migrated outcomes needed for future requirement discovery

## 4. Docs Tracking Cleanup and Guidance Updates

- [x] 4.1 Update `docs/AI_TASKS.md` so active/incomplete and cancelled tracking remains, and Completed entries link to `openspec/archive/<slug>/`
- [x] 4.2 Delete only the migrated completed task markdown files from `docs/ai-tasks/` after archive and spec artifacts exist
- [x] 4.3 Update `AGENTS.md`, `.github/copilot-instructions.md`, and `CONTRIBUTING.md` to point requirement discovery to `openspec/specs/*` and `openspec/archive/*`

## 5. Structural Validation and Migration Finalization

- [x] 5.1 Verify one-to-one coverage: every completed task has exactly one archive change and every Completed table link resolves
- [x] 5.2 Verify requirement coverage: each new/updated requirement from migration inputs is represented in `openspec/specs/*`
- [x] 5.3 Verify provenance integrity: archive artifacts and relevant specs reference original task IDs/titles/source paths
- [x] 5.4 Add final migration summary notes in this change artifacts, including coverage totals and any intentionally deferred items

## Migration Summary

- Completed tasks migrated: **12/12**
- Archive changes created for migration: **12**
- Completed task files removed from `docs/ai-tasks/`: **12**
- Capability specs added: `ai-task-tracking`, `project-branding-and-identity`, `generated-page-stylesheets`, `atproto-rich-text-rendering`, `browse-card-media-layout`, `modular-gradle-architecture`, `kolo-utility-architecture`, `kolo-styles-module-foundation`, `endpoint-controller-packaging`, `kolo-css-generation`, `kolo-html-runtime-integration`, `server-rendered-web-architecture`, `security-baseline`, `testing-verification-practices`
- Capability specs updated: `kolo-spacing-ownership` (provenance notes)
- Deferred items:
  - None in this migration scope.
