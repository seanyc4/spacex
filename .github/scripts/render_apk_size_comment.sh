#!/bin/bash
set -e

TEMPLATE_FILE=".github/templates/apk_size_comment.md"
RENDERED_FILE=".github/templates/apk_size_rendered_comment.md"

# Inputs (sanitize to remove whitespace or newlines)
VERSION="$(echo "$1" | tr -d '\n\r' | xargs)"
BRANCH="$(echo "$2" | tr -d '\n\r' | xargs)"

main_apk="spacex-debug-main.apk"
branch_apk="spacex-debug_$VERSION.apk"

main_kb=$(du -k "$main_apk" | cut -f1)
branch_kb=$(du -k "$branch_apk" | cut -f1)

main_mb=$(awk "BEGIN { printf \"%.2fMB\", $main_kb/1024 }")
branch_mb=$(awk "BEGIN { printf \"%.2fMB\", $branch_kb/1024 }")

if [[ $main_kb == $branch_kb ]]; then
    result="✅ APK size has not changed."
elif [[ $main_kb -gt $branch_kb ]]; then
    diff_kb=$((main_kb - branch_kb))
    diff_mb=$(awk "BEGIN { printf \"%.2fMB\", $diff_kb/1024 }")
    result="📉 New APK is ${diff_mb} smaller."
else
    diff_kb=$((branch_kb - main_kb))
    diff_mb=$(awk "BEGIN { printf \"%.2fMB\", $diff_kb/1024 }")
    result="📈 New APK is ${diff_mb} larger."
fi

# Render the markdown with placeholder replacement
sed \
  -e "s|{{version}}|$VERSION|g" \
  -e "s|{{branch}}|$BRANCH|g" \
  -e "s|{{main_size}}|$main_mb|g" \
  -e "s|{{branch_size}}|$branch_mb|g" \
  -e "s|{{result}}|$result|g" \
  "$TEMPLATE_FILE" > "$RENDERED_FILE"

# Output rendered file path for use in workflow
echo "comment=$RENDERED_FILE" >> "$GITHUB_OUTPUT"
