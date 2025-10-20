#!/bin/bash
set -e

# Build Release AAB with Baseline Profile
echo "Generating Baseline Profile and building Release AAB"
./gradlew :app:generateBaselineProfile :app:bundleRelease --stacktrace
