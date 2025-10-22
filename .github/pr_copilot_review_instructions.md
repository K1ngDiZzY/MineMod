```markdown
## What changed
<!-- Short summary of the change -->

## Why this change
<!-- Reason / context -->

## Copilot review guidance (please keep / edit as needed)
- Focus checks on: security (secrets, injections, auth), performance, correctness of business rules.
- Check any changes to: /src/main, /src/test, /config, code that touches user input or file I/O.
- Look for: missing unit/integration tests, breaking API changes, error handling, logging, edge cases.
- Suggest improvements: code clarity, modularity, adherence to coding standards, potential refactors, optimizations, security hardening.
- Ignore: generated files under /run, /logs.
- Tests: cover new/changed code and run successfully if tests make sense, if tests are missing suggest adding them with examples.
- Document: any assumptions made during the review, and highlight areas needing human attention, especially for complex logic.

## Checklist for humans
- [ ] Run tests locally
- [ ] Update changelog / docs
- [ ] Verify no sensitive info (secrets, tokens) is exposed
- [ ] Ensure compliance with coding standards
- [ ] Confirm all new/changed code is covered by tests
```