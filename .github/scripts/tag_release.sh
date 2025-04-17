#!/bin/bash
set -e

# Clean up existing local tags to avoid conflicts
git tag -l | xargs git tag -d

# Fetch latest tags from remote
git fetch --tags
next_tag=""

# Get the latest tag by commit date
latest_tag="$(git tag --sort=committerdate | tail -1)"
echo "Latest tag: $latest_tag"

# Count how many commits have been made since the last tag
commit_count="$(git rev-list $latest_tag..HEAD --count)"
echo "Commits since last tag: $commit_count"

# If there are new commits, bump the *minor* version
if [[ $commit_count != "0" ]]; then
  major="${latest_tag%%.*}"              # Extract major version (e.g. "1" from "1.33.0")
  minor_patch="${latest_tag#*.}"         # Remove major and dot (e.g. "33.0")
  minor="${minor_patch%%.*}"             # Extract minor version (e.g. "33")
  next_minor=$((minor + 1))              # Increment minor version (e.g. "34")
  next_tag="${major}.${next_minor}.0"    # Construct new tag (e.g. "1.34.0")

  echo "Next release tag: $next_tag"
  echo "RELEASE_TAG=$next_tag" >> $GITHUB_ENV
else
  echo "No new commits. Skipping release."
  exit 1
fi
