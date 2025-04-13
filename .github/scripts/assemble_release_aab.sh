#!/bin/bash
set -e

# Build Debug APK
./gradlew :app:bundleRelease --stacktrace
