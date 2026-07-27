# Design: Migrate completed AI tasks in docs to OpenSpec archive + specs

## Context

This change migrates historical, completed AI work from `docs/AI_TASKS.md` and `docs/ai-tasks/*` into OpenSpec archived changes, then establishes OpenSpec specs as the long-term source of requirements.

Current state:
- Completed AI work is tracked as markdown task records in `docs/ai-tasks/`, indexed by `docs/AI_TASKS.md`.
- Architecture and behavior knowledge is split across `docs/ARCHITECTURE.md`, `docs/DECISIONS.md`, `docs/GLOSSARY.md`, `docs/SECURITY.md`, and `docs/TESTING.md`.
- OpenSpec already exists (`openspec/changes`, `openspec/archive`, `openspec/specs`), but historical AI tasks are not represented there consistently.

Constraints and stakeholders:
- Migration must preserve traceability for completed work while keeping active/incomplete AI tasks in docs.
- Archived changes must be coherent (`proposal.md`, `design.md`, `tasks.md`, and spec deltas where applicable), not just raw file copies.
- The result should reduce dependency on broad docs pages for requirement discovery, with OpenSpec specs becoming primary.
- Stakeholders: maintainers relying on changelog-quality historical context, and contributors implementing future work from spec-driven requirements.

Requirement references:
- Motivation and intended end state are defined in this change's `proposal.md`.
- Existing requirement coverage is currently fragmented across `docs/ARCHITECTURE.md`, `docs/DECISIONS.md`, `docs/GLOSSARY.md`, `docs/SECURITY.md`, and `docs/TESTING.md`; this change consolidates relevant requirement-level content into OpenSpec `specs/*`.

## Goals / Non-Goals

**Goals:**
- Convert each completed AI task into an archived OpenSpec change with complete artifact structure.
- Preserve chronological and semantic traceability from original AI task IDs/records to archived OpenSpec entries.
- Create/expand OpenSpec capability specs from completed-task outcomes and enduring requirements currently documented in core docs pages.
- Keep only non-completed AI tasks in docs-based AI task tracking.
- Delete completed task markdown files from `docs/ai-tasks/` after successful migration.
- Keep a Completed section in `docs/AI_TASKS.md` that links to each migrated archived OpenSpec change.
- Define a repeatable migration pattern that can be rerun safely if partial migration occurs.

**Non-Goals:**
- Re-implementing application code tied to historical tasks.
- Rewriting every documentation page into OpenSpec narrative prose.
- Migrating `todo`, `in_progress`, `partial`, `blocked`, or `cancelled` AI tasks into archive as completed changes.
- Introducing new runtime dependencies or changing Kotlin/Spring application architecture as part of this documentation migration.

## Decisions

### 1) Use one archived OpenSpec change per completed AI task
**Decision:** Map each completed task row to a dedicated directory in `openspec/archive/<slug>/` containing `proposal.md`, `design.md`, `tasks.md`, optional `.openspec.yaml`, and `specs/*` deltas when requirements changed. Archive directory naming drops legacy AI task numbers (e.g., `BA-001`) and uses descriptive slug-only naming after migration.

**Rationale:** This preserves historical granularity and keeps change intent auditable per task.

**Alternatives considered:**
- Aggregate all completed tasks into one large archive change: rejected because it loses per-task traceability and increases review complexity.
- Keep task records only in docs and skip archive conversion: rejected because it fails the proposal objective to make OpenSpec the durable source.

### 2) Treat docs pages as migration input, OpenSpec specs as post-migration requirement source
**Decision:** Extract requirement-level statements from completed task records plus key docs pages and normalize them into capability specs under `openspec/specs/*`.

**Rationale:** Requirements belong in specs, while architecture/security/testing docs should no longer be required as the primary requirement lookup path.

**Alternatives considered:**
- Copy docs content verbatim into specs: rejected because it mixes explanatory prose with normative requirements and creates noise.
- Create archives without spec updates: rejected because it would keep requirement knowledge fragmented.

### 3) Preserve docs-based AI task tracking only for non-completed tasks
**Decision:** Retain active/incomplete task tracking in docs, delete completed task files from `docs/ai-tasks/`, and keep a lightweight Completed section in `docs/AI_TASKS.md` that links to the corresponding `openspec/archive/<slug>/` change directories.

**Rationale:** Keeps operational task management lightweight while moving historical completed work into OpenSpec archive where it belongs.

**Alternatives considered:**
- Keep completed items in both docs and archive indefinitely: rejected because dual ownership causes drift and duplicate maintenance.
- Delete all AI task docs including active tasks: rejected because proposal explicitly keeps non-completed tasks in docs.

### 4) Add explicit provenance links between old task IDs and archive changes/specs
**Decision:** Each migrated archive artifact references original AI task ID/title and source file path; resulting specs reference originating tasks/decisions where relevant.

**Rationale:** Provenance is necessary for reviewers to trust migration fidelity and for future contributors to locate rationale quickly.

**Alternatives considered:**
- Migrate without provenance metadata: rejected due to poor auditability.
- Keep provenance only in commit history: rejected because it is harder to discover and less durable for readers.

### 5) Execute migration in deterministic phases with idempotent checks
**Decision:** Use a phase-based migration order: inventory -> map -> generate artifacts -> update specs -> prune completed docs entries -> validate.

**Rationale:** Ordered phases reduce risk of partial inconsistencies and make reruns predictable when interrupted.

**Alternatives considered:**
- Opportunistic/manual per-file migration without ordering: rejected due to high omission risk.
- Big-bang rewrite with no checkpoint validation: rejected because rollback and troubleshooting become difficult.

### 6) Apply pragmatic spec granularity during migration
**Decision:** Keep requirement groupings that are already cohesive, but split capabilities when boundaries are clearly distinct (different user intent, route/surface area, or lifecycle).

**Rationale:** Avoids churn from unnecessary splitting while preventing over-broad specs that hide meaningful separations.

**Alternatives considered:**
- Always split aggressively into fine-grained specs: rejected because it creates overhead and fragmentation.
- Always merge into broad umbrella specs: rejected because it reduces clarity and weakens maintainability.

## Risks / Trade-offs

- [Task-to-capability mapping ambiguity can produce incorrect spec placement] → Mitigation: define explicit mapping rules (by feature area/outcome) and capture unresolved mapping exceptions in migration notes before finalization.
- [Normative requirements may be lost while compressing rich docs prose] → Mitigation: require a requirement-extraction pass with source citations and a completeness checklist against proposal + docs pages.
- [Archive artifacts may become template-complete but semantically thin] → Mitigation: enforce minimum content quality for proposal/design/tasks during migration (context, decisions, acceptance criteria, execution evidence).
- [Dual source-of-truth period during migration can confuse contributors] → Mitigation: keep migration short-lived, annotate in-progress state, and remove completed-task docs entries only after archive/spec artifacts exist.
- [Over-aggressive cleanup could remove still-useful docs guidance] → Mitigation: scope cleanup to completed-task content and requirement duplication; preserve operational guidance not representable as specs.
- [Dropping numeric task IDs from archive directory names can make old BA-* references harder to follow] → Mitigation: keep explicit BA-* provenance inside archive artifacts and add direct archive links in `docs/AI_TASKS.md` Completed section.

## Migration Plan

1. Inventory completed AI tasks from `docs/AI_TASKS.md` and corresponding files under `docs/ai-tasks/`; build a mapping table (task ID -> archive slug (no task number) -> affected capabilities).
2. For each completed task, generate or normalize archive artifacts under `openspec/archive/<slug>/` with complete change structure and provenance references.
3. Extract requirement-level outcomes from completed tasks and the core docs pages (`ARCHITECTURE`, `DECISIONS`, `GLOSSARY`, `SECURITY`, `TESTING`), then create/update `openspec/specs/*` capability specs.
4. Resolve overlaps by merging duplicate requirements and keeping a single canonical statement per capability requirement.
5. Update `docs/AI_TASKS.md` so active/incomplete tasks remain in docs, and the Completed section links directly to each archived OpenSpec change.
6. Delete migrated completed task files from `docs/ai-tasks/` after confirming archive/spec/provenance completeness.
7. Run structural validation: every migrated completed task has an archive change, every new/modified requirement is represented in specs, docs Completed links resolve, and provenance links resolve.
8. Finalize with a migration summary in change artifacts documenting coverage and any intentionally deferred items.

Rollback strategy:
- If migration quality checks fail, restore deleted completed-task files and docs Completed links, and revert newly generated archive/spec artifacts as one unit.
- If only a subset is problematic, rollback the affected task mappings/capability specs while keeping already-validated migrated items.
- Because this is documentation/spec migration only, rollback is repository-state rollback (no runtime/data migration rollback required).
