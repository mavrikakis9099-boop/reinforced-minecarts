# Reinforced Minecarts V1 Validation

> This document records what was actually tested for 1.0.0. Unchecked items remain unclaimed; the release should not be described as having passed those cases.

## Target

- Mod version: `1.0.0+26.2`
- Minecraft: 26.2
- Fabric Loader: 0.19.5+
- Fabric API: 0.160.0+26.2
- Java: 25
- Required storage baseline: Reinforced Chests/Core Build 14

## Automated results

- **PASS — Build 14 regression contract:** all recorded legacy JAR hashes, runtime files,
  textures, persistence bridges, menus, statistics and creative-tab registrations matched.
- **PASS — Python test suite:** 31 of 31 tests passed, including all eight reinforced-minecart
  contract tests and the existing GUI, scrolling and legacy visual-contract tests.
- **PASS — Clean Java build:** Reinforced Core, Reinforced Barrels, Reinforced Chests and
  Reinforced Minecarts compiled from a clean state with Java 25 and Gradle 9.5.0.
- **PASS — Packaging:** the distributable JAR contains the new mod only; Reinforced Chests
  and Reinforced Core are declared runtime dependencies rather than bundled copies.
- **PASS — Initial dedicated-server class loading:** Fabric Loader 0.19.5 loaded the exact
  Build 14 Reinforced Chests/Core modules and registered all five minecart tiers without a
  client-class loading error.
- **NOT CLAIMED — Full server/world startup:** the test server stopped at Mojang's EULA
  gate. The EULA was not accepted automatically, so datapack loading and world startup
  remain part of the manual owner test matrix.

## Alpha.1 owner-test findings

- **PASS:** a heavily modded Minecraft 26.2 world loaded successfully with Build 14.
- **PASS:** all five inventory sprites were correct and should remain unchanged.
- **PASS:** crafting recipes worked and appeared correctly in the recipe viewer.
- **PASS:** minecart inventory interaction and ordinary rail movement behaved normally.
- **FAIL:** placed carts rendered without a visible chest. Alpha.2 changes the chest-model
  submission colour from the entity no-outline sentinel (`0`) to opaque white (`-1`), matching
  the vanilla chest renderer. Owner retest is required.
- **CHANGE REQUESTED:** display names shortened from “Copper Reinforced Minecart” style to
  “Copper Minecart” style for consistency with “Copper Chest”.

## Alpha.2 owner-test results

- **PASS:** all five tier chests are visibly mounted in their carts.
- **PASS:** display names use the approved “Copper Minecart” through “Netherite Minecart” style.
- **PASS:** all five item sprites remain correct.
- **PASS:** placed chest textures follow M0nkeyPr0grammer's Reinforced Chests resource-pack
  overrides without minecart-specific compatibility code.
- **PASS:** menus open and expose the expected tier-sized storage layouts.
- **PASS:** ordinary movement and interaction remain vanilla-like.
- **PASS:** hopper insertion and extraction behave as expected.
- **ACCEPTED:** owner considers the capacity-only V1 complete unless further bugs are found.

Clean validation command:

```bash
bash tools/build.sh
```

## Manual owner test matrix

Run these checks with only Fabric API, the two Build 14 JARs and Reinforced Minecarts installed first.
Repeat compatibility-sensitive GUI checks with Mod Menu/Nemo/resource packs afterward.

For every Copper, Iron, Gold, Diamond and Netherite minecart:

### Creation and placement

- [x] Shapeless reinforced chest + minecart recipe appears and crafts the correct item.
- [x] Item icon identifies the correct tier.
- [x] Alpha.2 item and menu names use the shorter “Copper Minecart” style.
- [ ] Item places only where a vanilla minecart may be placed.
- [x] Placed entity is the correct tier and renders as a minecart carrying that tier's chest.

### Inventory and GUI

- [ ] Inventory opens with 45 / 54 / 81 / 108 / 108 slots respectively.
- [ ] Every storage slot is visible/reachable in Single mode.
- [ ] Every storage slot is reachable in Scroll mode at 6, 7, 8 and 9 visible rows.
- [ ] Player inventory and hotbar align correctly.
- [ ] Shift-click works both directions without duplication or loss.
- [ ] Custom entity names appear as the menu title.

### Persistence

- [ ] Contents survive leaving and re-entering the chunk.
- [ ] Contents survive save/quit and world reload.
- [ ] Contents survive a dedicated-server restart.
- [ ] Tier identity, custom name and inventory slot order remain unchanged.

### Automation and redstone

- [x] Hopper inserts into the tested tiers as expected.
- [x] Hopper extracts from the tested tiers as expected.
- [x] Hopper transfer behaviour remains vanilla-like in owner testing.
- [ ] Detector rail/comparator output changes with inventory fullness as expected.
- [ ] Activator rails retain vanilla chest-minecart semantics.

### Rails and movement

- [ ] Normal, powered and detector rails work.
- [ ] Curves and ascending/descending slopes render and move correctly.
- [x] Basic rail movement and interaction match vanilla minecarts.
- [ ] Collisions and pushing match vanilla minecarts.
- [ ] Inventory fullness affects natural slowdown as a vanilla chest minecart does.

### Destruction

- [ ] Breaking an empty cart drops exactly one correct tier minecart item.
- [ ] Breaking a filled cart drops exactly one cart item and all contents once.
- [ ] Explosion/destruction paths do not duplicate or silently delete contents.
- [ ] Pick-block produces the correct tier item.

### Multiplayer and compatibility

- [ ] Dedicated server starts without client-class loading errors.
- [ ] Two players see consistent movement and inventory state.
- [ ] Reinforced Chests still craft, render, open and retain their established capacities.
- [ ] Reinforced Barrels remain unaffected when installed.
- [ ] Missing Reinforced Chests produces a clear Fabric dependency error.
- [ ] Nemo Inventory Sorting does not misplace buttons or corrupt movement.
- [ ] Better GUI / compatibility resource-pack combinations do not double or misalign the GUI.

## Known limitations

- No manual in-game claims should be marked complete until tested by the owner.
- V1 intentionally has no tier-specific speed, transfer, durability or resistance bonuses.
