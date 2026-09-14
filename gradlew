#!/bin/sh
set -eu
GRADLE_VERSION="9.5.1"
BASE="${GRADLE_USER_HOME:-$HOME/.gradle}/universal-template"
DIST="$BASE/gradle-$GRADLE_VERSION"
ZIP="$BASE/gradle-$GRADLE_VERSION-bin.zip"
URL="https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip"

if [ ! -x "$DIST/bin/gradle" ]; then
  mkdir -p "$BASE"
  echo "Bootstrapping Gradle $GRADLE_VERSION..."
  if command -v curl >/dev/null 2>&1; then
    curl -fL "$URL" -o "$ZIP"
  elif command -v wget >/dev/null 2>&1; then
    wget -O "$ZIP" "$URL"
  else
    echo "Neither curl nor wget is available." >&2
    exit 1
  fi
  TMP="$BASE/.extract-$GRADLE_VERSION"
  rm -rf "$TMP" && mkdir -p "$TMP"
  command -v unzip >/dev/null 2>&1 || { echo "unzip is required." >&2; exit 1; }
  unzip -q "$ZIP" -d "$TMP"
  rm -rf "$DIST"
  mv "$TMP/gradle-$GRADLE_VERSION" "$DIST"
  rm -rf "$TMP"
fi
exec "$DIST/bin/gradle" "$@"
