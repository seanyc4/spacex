#!/bin/bash
set -e

# -------------------------------------
# 🔁 Args
# -------------------------------------
ORIGINAL_APK_PATH=$1
RENAMED_APK_PATH=$2

# Ensure target directory exists
mkdir -p "$(dirname "$RENAMED_APK_PATH")"

# Rename the file
mv "$ORIGINAL_APK_PATH" "$RENAMED_APK_PATH"

# Output the renamed APK path
echo "✅ APK renamed to: $RENAMED_APK_PATH"
