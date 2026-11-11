#!/usr/bin/env bash
# One-time setup of a development machine for ProjectLibre.
# Installs: a JDK (11+; 21 recommended), Apache Ant, and (Linux only) Xvfb,
# ImageMagick and xdotool for headless screenshots / UI automation.
set -euo pipefail
OS="$(uname -s)"
if [ "$OS" = "Darwin" ]; then
  command -v brew >/dev/null || { echo "Install Homebrew first: https://brew.sh"; exit 1; }
  brew install openjdk@21 ant
  echo "Add the JDK to your PATH, e.g.: export PATH=\"$(brew --prefix openjdk@21)/bin:\$PATH\""
elif command -v apt-get >/dev/null; then
  SUDO=""; [ "$(id -u)" -ne 0 ] && SUDO="sudo"
  $SUDO apt-get update
  $SUDO apt-get install -y openjdk-21-jdk ant xvfb imagemagick xdotool
elif command -v dnf >/dev/null; then
  sudo dnf install -y java-21-openjdk-devel ant xorg-x11-server-Xvfb ImageMagick xdotool
else
  echo "Unsupported OS. Install manually: JDK 21 (https://adoptium.net) and Apache Ant (https://ant.apache.org)."
  echo "On Windows, see DEVELOPMENT.md (Windows section)."
  exit 1
fi
java -version
ant -version
echo "Done. Next: scripts/build.sh && scripts/run.sh"
