#!/usr/bin/env bash
# Build ProjectLibre (all modules) with Ant.
# Output: openproj_build/dist/projectlibre.jar plus dist/lib/*.jar
# Usage: scripts/build.sh [ant targets...]   (default: dist)
#        scripts/build.sh clean dist          (full rebuild)
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
command -v ant >/dev/null || { echo "Apache Ant not found. Run scripts/setup-dev.sh first."; exit 1; }
cd "$ROOT/openproj_build"
if [ "$#" -eq 0 ]; then set -- dist; fi
# 'clean' in openproj_build only removes build/ and dist/; also clean contrib jars.
for t in "$@"; do
  if [ "$t" = "clean" ]; then ant -q -f ../openproj_contrib/build.xml clean; fi
done
ant "$@"
echo
echo "Built: $ROOT/openproj_build/dist/projectlibre.jar"
