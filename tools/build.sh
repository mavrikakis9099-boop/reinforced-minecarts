#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

java_major() {
  java -version 2>&1 | sed -n '1s/.*version "\([0-9][0-9]*\).*/\1/p'
}

if [[ "$(java_major || true)" != "25" ]]; then
  echo "ERROR: Java 25 is required for Minecraft 26.2 mod compilation." >&2
  exit 25
fi

if [[ -x ./gradlew ]]; then
  GRADLE=(./gradlew)
elif command -v gradle >/dev/null 2>&1; then
  GRADLE=(gradle)
else
  echo "ERROR: Gradle 9.5-compatible tooling is required." >&2
  exit 26
fi

"${GRADLE[@]}" --no-daemon clean :reinfcore:build :reinfchest:build :reinfminecart:build

mkdir -p reinfminecart/dist
find reinfminecart/build/libs -maxdepth 1 -type f -name '*.jar'   ! -name '*-sources.jar' ! -name '*-dev.jar' -exec cp -f {} reinfminecart/dist/ \;

echo "Reinforced Minecarts build output:"
ls -lh reinfminecart/dist/*.jar
