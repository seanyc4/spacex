#!/bin/bash

location="buildSrc/src/main/kotlin/Android.kt"

# Get current version name and version code
currentVersionName=$(./gradlew -q printAppVersionName)
currentVersionCode=$(./gradlew -q printAppVersionCode)

# Correctly split versionName (e.g., 1.8.0 → major=1, minor=8, patch=0)
IFS='.' read -r major minor patch <<< "$currentVersionName"

# Bump minor version
newMinor=$((minor + 1))
newVersionName="$major.$newMinor.$patch"

# Replace versionName in Android.kt
sed -i -E "s/(versionName = \")[^\"]+(\")/\1${newVersionName}\2/" "$location"
echo "Updated versionName to $newVersionName"

# Extract and bump versionCode
currentVersionCode=$(grep 'versionCode' "$location" | sed -E 's/.*= ([0-9]+)/\1/')
newVersionCode=$((currentVersionCode + 1))

# Replace versionCode in Android.kt
sed -i -E "s/(versionCode = )([0-9]+)/\1$newVersionCode/" "$location"
echo "Updated versionCode to $newVersionCode"

# Export version for subsequent steps
echo "NEW_VERSION_NAME=$newVersionName" >> $GITHUB_OUTPUT
