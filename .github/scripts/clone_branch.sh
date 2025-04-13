#!/bin/bash
set -euo pipefail

REPO_URL=$1
BRANCH_NAME=${2:-main}
TARGET_DIR=${3:-/tmp/main}

echo "🔄 Cloning branch '$BRANCH_NAME' from $REPO_URL into $TARGET_DIR..."
git clone --depth=1 --branch "$BRANCH_NAME" "$REPO_URL" "$TARGET_DIR"
