#!/bin/bash
set -e

TEMPLATE_FILE=".github/templates/qr_code_comment.md"
RENDERED_FILE=".github/templates/rendered_comment.md"

# Inputs
ARTIFACT_URL="$1"
ARTIFACT_URL_ENCODED="$2"
VERSION="$3"
DATETIME="$4"

# Render the markdown with simple replacement
sed \
  -e "s|{{artifact_url}}|$ARTIFACT_URL|g" \
  -e "s|{{artifact_url_encoded}}|$ARTIFACT_URL_ENCODED|g" \
  -e "s|{{version}}|$VERSION|g" \
  -e "s|{{datetime}}|$DATETIME|g" \
  "$TEMPLATE_FILE" > "$RENDERED_FILE"

echo "comment_file=$RENDERED_FILE" >> "$GITHUB_OUTPUT"
