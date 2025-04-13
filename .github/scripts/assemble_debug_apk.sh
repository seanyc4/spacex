#!/bin/bash
set -e

APK_NAME=$1

# Build Debug APK
./gradlew :app:assembleDebug

# Rename APK script
./.github/scripts/rename_artifact.sh "app/build/outputs/apk/debug/app-debug.apk" "app/build/outputs/apk/debug/$APK_NAME"
