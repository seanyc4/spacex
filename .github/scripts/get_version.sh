#!/bin/bash
set -e

VERSION=$(./gradlew -q printAppVersionName)
echo "version=$VERSION" >> "$GITHUB_OUTPUT"
echo "Version Code: $VERSION"
