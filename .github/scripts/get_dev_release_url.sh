#!/bin/bash
set -e

GITHUB_TOKEN="$1"
REPO="$2"
RUN_ID="$3"
ARTIFACT_NAME="$4"

# Get artifact ID
ARTIFACT_ID=$(curl -s \
  -H "Authorization: Bearer $GITHUB_TOKEN" \
  "https://api.github.com/repos/$REPO/actions/runs/$RUN_ID/artifacts" | \
  jq -r ".artifacts[] | select(.name==\"$ARTIFACT_NAME\") | .id")

# Fail fast if not found
if [[ -z "$ARTIFACT_ID" ]]; then
  echo "❌ Artifact '$ARTIFACT_NAME' not found."
  exit 1
fi

# Construct artifact URL
artifact_url="https://github.com/$REPO/actions/runs/$RUN_ID/artifacts/$ARTIFACT_ID"

# URL-encode it
ENCODED_URL=$(jq -rn --arg url "$artifact_url" '$url|@uri')

# Output both original and encoded
echo "artifact-url=$artifact_url" >> "$GITHUB_OUTPUT"
echo "artifact-url-encoded=$ENCODED_URL" >> "$GITHUB_OUTPUT"
