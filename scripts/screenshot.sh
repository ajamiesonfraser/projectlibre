#!/usr/bin/env bash
# Launch the built ProjectLibre jar on a virtual X display (Xvfb), open a
# project file, and save a PNG screenshot. Useful for checking UI changes in
# headless environments (CI, cloud dev boxes, Claude Code sessions).
#
# Usage: scripts/screenshot.sh [output.png] [project-file] [extra java args...]
#   Defaults: build/screenshots/gantt.png and the "Commercial construction" sample.
# Env:    SCREEN=1600x1000  WAIT=12 (seconds to wait after launch)
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
OUT="${1:-$ROOT/build/screenshots/gantt.png}"
FILE="${2:-$ROOT/openproj_build/resources/samples/Commercial construction project plan.pod}"
shift $(( $# > 2 ? 2 : $# )) || true
SCREEN="${SCREEN:-1600x1000}"
WAIT="${WAIT:-12}"
JAR="$ROOT/openproj_build/dist/projectlibre.jar"
DISPLAY_NUM="${DISPLAY_NUM:-:97}"

command -v Xvfb >/dev/null || { echo "Xvfb not installed (apt-get install xvfb)"; exit 1; }
command -v import >/dev/null || { echo "ImageMagick 'import' not installed (apt-get install imagemagick)"; exit 1; }
[ -f "$JAR" ] || { echo "Build first: (cd openproj_build && ant dist)"; exit 1; }
mkdir -p "$(dirname "$OUT")"

Xvfb "$DISPLAY_NUM" -screen 0 "${SCREEN}x24" >/dev/null 2>&1 &
XPID=$!
trap 'kill $JPID $XPID 2>/dev/null || true' EXIT
sleep 1
# A throwaway HOME keeps first-run dialogs and preferences from leaking between runs.
TMPHOME="$(mktemp -d)"
DISPLAY="$DISPLAY_NUM" HOME="$TMPHOME" java -Duser.home="$TMPHOME" "$@" -jar "$JAR" "$FILE" >"$OUT.log" 2>&1 &
JPID=$!
sleep "$WAIT"
DISPLAY="$DISPLAY_NUM" import -window root "$OUT"
echo "Saved $OUT (log: $OUT.log)"
