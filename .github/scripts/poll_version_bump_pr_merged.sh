#!/bin/bash
set -e

# Run version bump dry run to extract the new version
NEW_VERSION_NAME=$(bash .github/scripts/version_bump.sh --dry-run | grep -oE '[0-9]+\.[0-9]+\.[0-9]+' | tr -d '\n')
BRANCH_NAME="version-bump/$NEW_VERSION_NAME"

echo "🔍 Looking for PR from branch: $BRANCH_NAME"

# Get PR number for this branch
PR_NUMBER=$(gh pr list --state open --head "$BRANCH_NAME" --json number -q '.[0].number')

if [ -z "$PR_NUMBER" ]; then
  echo "❌ No open PR found with head branch '$BRANCH_NAME'"
  exit 1
fi

echo "⏳ Waiting for PR #$PR_NUMBER to be merged..."
for i in {1..90}; do
  MERGED_AT=$(gh pr view "$PR_NUMBER" --json mergedAt -q '.mergedAt')
  if [ -n "$MERGED_AT" ]; then
    echo "✅ PR #$PR_NUMBER has been merged at $MERGED_AT"
    exit 0
  fi
  echo "⏳ Still waiting... attempt $i"
  sleep 30
done

echo "❌ Timeout reached. PR #$PR_NUMBER was not merged."
exit 1
