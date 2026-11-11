#!/usr/bin/env bash
# Run the locally built ProjectLibre.
# Usage: scripts/run.sh [project-file.pod|.mpp|.xml] [-- extra JVM options]
# Examples:
#   scripts/run.sh
#   scripts/run.sh "openproj_build/resources/samples/New Product.pod"
#   scripts/run.sh "" -Dprojectlibre.laf=classic     # old Metal look
#   scripts/run.sh "" -Dprojectlibre.laf=dark        # experimental dark theme
#   scripts/run.sh "" -Dsun.java2d.uiScale=2         # HiDPI test
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
JAR="$ROOT/openproj_build/dist/projectlibre.jar"
[ -f "$JAR" ] || "$ROOT/scripts/build.sh"
FILE="${1:-}"
shift || true
if [ -n "$FILE" ]; then
  exec java -Xms128m -Xmx1g "$@" -jar "$JAR" "$FILE"
else
  exec java -Xms128m -Xmx1g "$@" -jar "$JAR"
fi
