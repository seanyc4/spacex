#!/bin/bash
set -e

location="buildSrc/src/main/kotlin/Android.kt"

# Inputs (can be empty if not overridden)
manualVersionName=$1
manualVersionCode=$2

# Validate versionName (if provided)
if [[ -n "$manualVersionName" ]]; then
  if [[ ! "$manualVersionName" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    echo "❌ Invalid versionName: '$manualVersionName'. Must follow format X.Y.Z (e.g. 1.2.3)"
    exit 1
  fi
  newVersionName="$manualVersionName"
else
  # Default bump: minor++
  currentVersionName=$(./gradlew -q printAppVersionName)
  IFS='.' read -r major minor patch <<< "$currentVersionName"
  minor=$((minor + 1))
  patch=0
  newVersionName="$major.$minor.$patch"
fi

# Validate versionCode (if provided)
if [[ -n "$manualVersionCode" ]]; then
  if [[ ! "$manualVersionCode" =~ ^[0-9]+$ ]]; then
    echo "❌ Invalid versionCode: '$manualVersionCode'. Must be a positive integer."
    exit 1
  fi
  newVersionCode="$manualVersionCode"
else
  currentVersionCode=$(./gradlew -q printAppVersionCode)
  newVersionCode=$((currentVersionCode + 1))
fi

# Apply changes
sed -i -E "s/(versionName = \")[^\"]+(\")/\1${newVersionName}\2/" "$location"
echo "✅ Updated versionName to $newVersionName"

sed -i -E "s/(versionCode = )([0-9]+)/\1$newVersionCode/" "$location"
echo "✅ Updated versionCode to $newVersionCode"

# Export version for subsequent steps
echo "new-version-name=$newVersionName" >> $GITHUB_OUTPUT
echo "file-path=buildSrc/src/main/kotlin" >> $GITHUB_OUTPUT
echo "artifact-name=version-bump" >> $GITHUB_OUTPUT
