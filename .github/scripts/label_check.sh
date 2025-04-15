#!/bin/bash
set -e

# GitHub API token and other environment variables passed from the GitHub Actions workflow
GITHUB_TOKEN="$GITHUB_TOKEN"
REPO_OWNER="$REPO_OWNER"
REPO_NAME="$REPO_NAME"
COMMIT_SHA="$COMMIT_SHA"

# Fetch the PR associated with the commit
response=$(curl -s -H "Authorization: token $GITHUB_TOKEN" \
  "https://api.github.com/repos/$REPO_OWNER/$REPO_NAME/commits/$COMMIT_SHA/pulls")

# Print the response to debug the structure
echo "API Response: $response"

# Check if we got a PR, otherwise fail
if [ $(echo "$response" | jq '. | length') -eq 0 ]; then
  echo "No PR associated with this commit"
  exit 1
fi

# Get PR labels
labels=$(echo "$response" | jq '.[0].labels | map(.name)')

# Check if the "Release Internal" label is present
if [[ "$labels" == *"Release Internal"* ]]; then
  echo "Release Internal label found."
  echo "has_label=true" >> $GITHUB_ENV
else
  echo "Release Internal label not found. Skipping."
  echo "has_label=false" >> $GITHUB_ENV
fi
