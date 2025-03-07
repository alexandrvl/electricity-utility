PROMPT-TEST-CODE RULES

GOAL: "Translate ideas into code through strict test-first development"

REQUIRED CONTEXT
Each project MUST maintain:
- product_context.md: Intent and goals
- active_context.md: Current state
- system_architecture.md: Architecture patterns
- tech_context.md: Stack, frameworks
- progress.md: Phase tracking
- requirements.md: Feature specs
- test_scenarios.md: Gherkin scenarios
- technical_assumptions.md: Technical decisions

DIRECTORY STRUCTURE
src/
  main/             # Phase 3 only: Implementation
  test/
    resources/      # Feature files location
      features/     # Gherkin scenarios
    spec/           # Phase 2 only: Feature specifications
      contracts/    # Step interfaces
    impl/           # Phase 3 only: Test implementation
      steps/        # Step definitions

CORE PRINCIPLES

1. Autonomy
   * Default Mode:
     - Work autonomously
     - Make decisions independently
     - Keep moving forward
     - Document while doing
   * Stop Only For:
     - Phase transitions
     - Gate validations
     - Security concerns
     - Explicit USER request

2. Understanding First
   * Before ANY Change:
     - Understand current behavior
     - Know expected outcome
     - Have evidence for both
     - Document findings while proceeding
   * When Viewing Code:
     - Know what you're looking for
     - Know why you need it
     - Stay focused on current task
     - Note but don't chase tangents

WORKFLOW PHASES

1. Analysis Phase (STOP)
   * Required Context:
     - Verify all context files exist
     - Review all requirements
     - Understand system boundaries
     - Document technical assumptions
   * Deliverables:
     - Scope definition
     - System boundaries
     - Quality requirements
     - Technical assumptions
     ✓ USER approval required

2. Specification Phase (STOP)
   * Feature Writing:
     - Write Gherkin scenarios
     - Use ubiquitous language
     - Cover happy paths
     - Cover edge cases
   * Contract Creation:
     - Generate step interfaces
     - Define domain contracts
     - Define repository contracts
     - Define service contracts
     - NO implementation details
     - Document assumptions
   * Files Allowed:
     - test/spec/features/*.feature
     - test/spec/contracts/steps/*Steps.kt
     - test/spec/contracts/domain/*.kt
   * Exit Criteria:
     - ALL scenarios written
     - ALL contracts defined
     - Domain model contracts complete
     - Documentation complete
     ✓ USER approval required

3. Implementation Phase
   * Entry Requirements:
     - Approved feature files
     - Complete step contracts
     - Complete domain contracts
     - Clear understanding
     - USER approval received

   * Setup Gate (STOP)
     - Build configuration complete
     - Dependencies defined
     - Project structure verified
     - Test infrastructure ready
     ✓ Build MUST succeed

   * Planning Gate (STOP)
     - Complete component list documented
     - Implementation sequence defined
     - Integration points identified
     - Test execution plan ready
     ✓ Plan MUST be documented

   * Implementation Sequence (ATOMIC)
     1. Project Infrastructure
        - Build system
        - Project structure
        - Development tools
     2. Core Components
        - Domain implementations
        - Repository implementations
        - Service implementations
     3. Test Components
        - Test infrastructure
        - Step implementations
        - Integration tests
     4. Integration Verification
        - Component wiring
        - Full test execution
     ✓ NO partial implementations

   * Implementation Rules:
     - Step implementations MUST only coordinate between UI and domain
     - Business logic MUST be in domain classes
     - Domain classes MUST implement domain contracts
     - NO partial implementations allowed
     - NO untested code allowed
     - Build MUST stay green
     - Tests MUST run after changes
     - Failures MUST be fixed immediately

   * Critical Violations (IMMEDIATE STOP)
     - Implementation without complete setup
     - Implementation without complete plan
     - Partial component implementations
     - Untested features
     - Broken integration
     - Postponed fixes

   * Recovery Process
     1. STOP all work
     2. Document current state
     3. Revert to last known good state
     4. Complete missing prerequisites
     5. Restart implementation
     6. Verify ALL components
     
   * Files Allowed:
     - src/main/*
     - test/impl/steps/*

PHASE ARTIFACTS PROTECTION

1. Protection Accumulation
   * Phase 1 artifacts PROTECTED in Phase 2+
   * Phase 2 artifacts PROTECTED in Phase 3+
   * Each phase LOCKS previous phase artifacts
   * Protection is CUMULATIVE and PERMANENT

2. Protected Phase 1 Files (LOCKED in Phase 2+)
   * Context Files:
     - product_context.md
     - active_context.md
     - system_architecture.md
     - tech_context.md
     - requirements.md
     - test_scenarios.md
     - technical_assumptions.md
   * Exceptions:
     - progress.md (MUST be updated)

3. Protected Phase 2 Files (LOCKED in Phase 3+)
   * Feature Files:
     - test/resources/features/*.feature
   * Contract Interfaces:
     - test/spec/contracts/steps/*Steps.kt
     - test/spec/contracts/domain/*.kt

4. Absolute Prohibitions
   * In Phase 2:
     - NO changes to Phase 1 files
     - NO new requirements
     - NO boundary changes
   * In Phase 3:
     - ALL Phase 2 prohibitions PLUS
     - NO changes to feature files
     - NO changes to contracts
     - NO new features/behaviors

5. Implementation Constraints
   * Each phase MUST work within:
     - Previous phases' boundaries
     - Locked specifications
     - Frozen contracts
   * NO behavior outside specifications
   * NO alternative implementations
   * NO workarounds or extensions

6. Violation Recovery
   * On ANY modification attempt:
     1. STOP all work
     2. Document violation
     3. Return to EARLIEST affected phase
     4. Get USER approval
     5. Update ALL affected files
     6. Re-approve ALL subsequent phases
     7. Only then resume work
   * Example:
     - If in Phase 3 and need requirements change:
       * Return to Phase 1
       * Update requirements
       * Re-do Phase 2
       * Re-approve both phases
       * Only then resume Phase 3

7. Protection Override
   * NONE ALLOWED
   * Not even by USER request
   * Must follow recovery process
   * Must restart from earliest affected phase

8. Progress Tracking
   * progress.md MUST be updated
   * Document phase transitions
   * Track completion status
   * Record issues and decisions

This protection is ABSOLUTE, CUMULATIVE, and PERMANENT across ALL phasesPHASE ARTIFACTS PROTECTION

1. Protection Accumulation
   * Phase 1 artifacts PROTECTED in Phase 2+
   * Phase 2 artifacts PROTECTED in Phase 3+
   * Each phase LOCKS previous phase artifacts
   * Protection is CUMULATIVE and PERMANENT

2. Protected Phase 1 Files (LOCKED in Phase 2+)
   * Context Files:
     - product_context.md
     - active_context.md
     - system_architecture.md
     - tech_context.md
     - requirements.md
     - test_scenarios.md
     - technical_assumptions.md
   * Exceptions:
     - progress.md (MUST be updated)

3. Protected Phase 2 Files (LOCKED in Phase 3+)
   * Feature Files:
     - test/resources/features/*.feature
   * Contract Interfaces:
     - test/spec/contracts/steps/*Steps.kt
     - test/spec/contracts/domain/*.kt

4. Absolute Prohibitions
   * In Phase 2:
     - NO changes to Phase 1 files
     - NO new requirements
     - NO boundary changes
   * In Phase 3:
     - ALL Phase 2 prohibitions PLUS
     - NO changes to feature files
     - NO changes to contracts
     - NO new features/behaviors

5. Implementation Constraints
   * Each phase MUST work within:
     - Previous phases' boundaries
     - Locked specifications
     - Frozen contracts
   * NO behavior outside specifications
   * NO alternative implementations
   * NO workarounds or extensions

6. Violation Recovery
   * On ANY modification attempt:
     1. STOP all work
     2. Document violation
     3. Return to the EARLIEST affected phase
     4. Get USER approval
     5. Update ALL affected files
     6. Re-approve ALL subsequent phases
     7. Only then resume work
   * Example:
     - If in Phase 3 and need requirements change:
       * Return to Phase 1
       * Update requirements
       * Re-do Phase 2
       * Re-approve both phases
       * Only then resume Phase 3

7. Protection Override
   * NONE ALLOWED
   * Not even by USER request
   * Must follow recovery process
   * Must restart from earliest affected phase

8. Progress Tracking
   * progress.md MUST be updated
   * Document phase transitions
   * Track completion status
   * Record issues and decisions

This protection is ABSOLUTE, CUMULATIVE, and PERMANENT across ALL phases

ERROR HANDLING

1. Scenario Failures
   * Expected (Continue):
     - New scenario failures
     - Documented edge cases
     - Known missing implementations
   * Unexpected (STOP):
     - Previously passing scenarios
     - Integration issues
     - Performance problems
     - Undocumented failures

2. Understanding Gaps
   * When Found:
     - Document the gap
     - Gather evidence needed
     - Continue current task
     - Return to gaps later

3. Architecture Issues
   * When Discovered:
     - STOP current implementation
     - Document discovery and impact
     - Request USER decision:
       * Continue current task
       * Switch to architectural fix
     - NO mixing of concerns

DOCUMENTATION

1. Continuous Documentation
   * Document While Working:
     - Decisions made
     - Problems found
     - Solutions implemented
     - Future considerations

2. Status Updates
   * Always Track in progress.md:
     - Current phase
     - Scenario status
     - Known issues
     - Next steps

VERIFICATION

1. Code Changes
   * Every Change Must:
     - Be complete
     - Be understood
     - Be tested
     - Be documented

2. Scenario Results
   * Every Run Must:
     - Execute full suite
     - Be analyzed fully
     - Drive next action
     - Update status

USER INTERACTION

1. Autonomous Operation
   * Proceed Without Approval For:
     - Step implementations
     - Production code
     - Bug fixes
     - Documentation updates
   * Prohibited Interactions:
     - Asking permission within phase
     - Seeking confirmation for standard decisions
     - Requesting approval outside gates
     - Breaking flow for non-critical issues

2. Required Approvals
   * Stop For USER Input On:
     - Phase transitions
     - Security decisions
     - Architecture changes
     - Feature file modifications
     - Explicit USER request

VIOLATION RECOVERY

1. Critical Violations (IMMEDIATE STOP)
   - Gate skipping
   - Missing approvals
   - Phase mixing
   - Feature file changes
   - Security concerns

2. Recovery Steps
   1. Stop all work
   2. Document violation
   3. Create a recovery plan
   4. Get USER approval
   5. Execute recovery
   6. Verify resolution

UPDATE MEMORY AT EACH GATE