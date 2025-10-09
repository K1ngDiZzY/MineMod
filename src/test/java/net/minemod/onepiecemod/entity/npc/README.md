# NPC Tests Documentation

This directory contains comprehensive tests for the NPC (Non-Player Character) system in the OnePiece mod.

## Test Files

### 1. NPCTest.java
Tests for the `NPC` class that extends `AbstractNPC`.

**Tests included:**
- Class loading and existence validation
- Inheritance structure (extends AbstractNPC)
- `createAttributes()` method existence
- Health attribute configuration (40.0 HP)
- Movement speed attribute configuration (0.25)
- Constructor validation

### 2. AbstractNPCTest.java
Tests for the `AbstractNPC` base class that all NPCs extend.

**Tests included:**
- Class loading and existence validation
- Inheritance structure (extends PathfinderMob)
- Constructor validation
- `registerGoals()` method existence
- `isPushable()` method existence and return type
- Class modifiers (public, non-abstract)

### 3. ModEntitiesTest.java
Tests for the `ModEntities` class that handles entity registration.

**Tests included:**
- Class loading and existence validation
- `ENTITY_TYPES` DeferredRegister existence
- `NPC` RegistryObject existence and initialization
- `register()` method existence
- Field modifiers validation (public, static, final)

## Purpose

These tests serve as:
1. **Validation**: Ensure NPC structure and behavior work as expected
2. **Regression Prevention**: Catch breaking changes early
3. **Documentation**: Provide clear examples of expected behavior
4. **Foundation**: Base tests that can be extended as NPC functionality grows

## Testing Approach

All tests follow these principles:
- **Non-invasive**: Tests don't modify existing NPC functionality
- **Generic**: Tests validate structure and attributes, not complex behavior
- **Reflective**: Uses Java reflection to validate class structure
- **Clear**: Each test has a descriptive name and clear assertions

## Running Tests

Tests can be run using Gradle:
```bash
./gradlew test
```

## Future Enhancements

As NPC functionality grows, consider adding tests for:
- Custom AI goals and behaviors
- NPC interactions with players
- Devil Fruit effects on NPCs
- Navy/Pirate targeting systems
- Custom NPC types extending the base NPC
