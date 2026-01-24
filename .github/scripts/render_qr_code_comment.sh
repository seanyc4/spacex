#!/bin/bash
set -e

TEMPLATE_FILE=".github/templates/qr_code_comment.md"
RENDERED_FILE=".github/templates/qr_code_rendered_comment.md"

# Inputs (sanitize to remove whitespace or newlines)
ARTIFACT_URL="$(echo "$1" | tr -d '\n\r' | xargs)"
ARTIFACT_URL_ENCODED="$(echo "$2" | tr -d '\n\r' | xargs)"
ARTIFACT_NAME="$(echo "$3" | xargs)"
DATETIME="$(echo "$4" | xargs)"

# Render the markdown with placeholder replacement
sed \
  -e "s|{{artifact-url}}|$ARTIFACT_URL|g" \
  -e "s|{{artifact-url-encoded}}|$ARTIFACT_URL_ENCODED|g" \
  -e "s|{{artifact-name}}|$ARTIFACT_NAME|g" \
  -e "s|{{date-time}}|$DATETIME|g" \
  "$TEMPLATE_FILE" > "$RENDERED_FILE"

# Output rendered file path for use in workflow
echo "comment=$RENDERED_FILE" >> "$GITHUB_OUTPUT"
