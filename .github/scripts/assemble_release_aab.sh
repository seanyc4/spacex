#!/bin/bash
set -e

# Build Release AAB with Baseline Profile
echo "Generating Baseline Profile and building Release AAB"
./gradlew :app:generateBaselineProfile :app:bundleRelease --stacktrace

# Verify that the Baseline Profile is included in the AAB
echo "🔍 Verifying baseline profile inclusion in the AAB"
unzip -l app/build/outputs/bundle/release/app-release.aab | grep baseline-prof.txt || {
  echo "❌ Baseline profile not found in AAB!"
  exit 1
}

echo "✅ Baseline profile found and included in the AAB."

