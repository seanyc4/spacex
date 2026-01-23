#!/bin/bash
set -e

# Build Debug APK
./gradlew :app:assembleRelease --stacktrace
