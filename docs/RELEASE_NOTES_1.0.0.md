# Reinforced Minecarts 1.0.0 for Minecraft 26.2

The first complete release of Reinforced Minecarts.

Reinforced Minecarts adds five capacity-tiered chest minecarts built as a companion mod for the
[Reinforced Storage 26.2 Community Port](https://github.com/mavrikakis9099-boop/reinforced-storage-26-2-community-port).

## Included tiers

| Minecart | Capacity |
| --- | ---: |
| Copper Minecart | 45 slots |
| Iron Minecart | 54 slots |
| Gold Minecart | 81 slots |
| Diamond Minecart | 108 slots |
| Netherite Minecart | 108 slots |

Each recipe is shapeless: combine the matching reinforced chest with a vanilla minecart.

## Behaviour

- Vanilla-like chest-minecart rail movement and collisions
- Vanilla hopper insertion and extraction
- Vanilla comparator/detector-rail behaviour through the chest-minecart base implementation
- Vanilla destruction/content-drop paths through the chest-minecart base implementation
- Reinforced Storage large-inventory menus
- Tier chest textures that can inherit compatible Reinforced Chests resource-pack overrides
- No tier-specific speed, transfer, durability or resistance bonuses in V1

## Requirements

- Minecraft 26.2
- Fabric Loader 0.19.5 or newer
- Fabric API 0.160.0+26.2
- Java 25
- Reinforced Chests `4.0.0-beta+26.2-port.14`
- Reinforced Core `4.0.9+26.2-port.14` supplied by the Build 14 Reinforced Chests JAR

Reinforced Barrels and Reinforced Shulker Boxes are not required.

## Validation

The release passed the recorded 31-test automated suite and clean multi-module Gradle build in the
original integration workspace. Owner testing confirmed crafting, recipe discovery, short tier
names, item sprites, all five placed chest renderers, basic inventory interaction, ordinary rail
movement, hopper automation and third-party Reinforced Chests texture-pack compatibility.

Extended manual cases that were not tested remain marked unchecked in `docs/VALIDATION.md`.

## Credits

Thanks to Aton-Kish for the Reinforced Storage foundation and MIT-licensed upstream work.

This is an independent community companion mod, not an official Aton-Kish release or endorsement.
