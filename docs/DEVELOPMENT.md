# Development

## Layout

- `reinfminecart/` — Reinforced Minecarts source and resources
- `vendor/reinfchest/` — Build 14 Reinforced Chests source snapshot used for compilation
- `vendor/reinfcore/` — Build 14 Reinforced Core source snapshot used for compilation
- `docs/` — release, validation and project documentation
- `tools/build.sh` — clean build helper

The vendor source is included to make the 1.0.0 tag self-contained and reproducible. The published
Minecarts JAR contains the Minecarts mod only; Reinforced Chests/Core remain runtime dependencies.

## Toolchain

- Java 25
- Minecraft 26.2
- Fabric Loader 0.19.5
- Fabric API 0.160.0+26.2
- Fabric Loom 1.17.20
- Gradle 9.5-compatible tooling

## Build

```bash
bash tools/build.sh
```

The final JAR is copied to:

```text
reinfminecart/dist/reinforced-minecarts-1.0.0+26.2.jar
```

## Versioning

Minecarts has its own version history independent of the Reinforced Storage community port.

Examples:

- Minecarts `1.0.0+26.2`
- Minecarts `1.1.0+26.2`
- Storage `4.0.0-beta+26.2-port.14`

A new Storage build does not automatically require a Minecarts version bump unless compatibility or
Minecarts code changes.
