#!/bin/bash
set -e

TEMPLATE_FILE=".github/templates/qr_comment.md"
RENDERED_FILE=".github/templates/rendered_comment.md"

# Strip leading/trailing spaces just in case
ARTIFACT_URL=$(echo "$1" | xargs)
ARTIFACT_URL_ENCODED=$(echo "$2" | xargs)
VERSION=$(echo "$3" | xargs)
DATETIME=$(echo "$4" | xargs)

# Render the template
sed \
  -e "s|{{artifact_url}}|$ARTIFACT_URL|g" \
  -e "s|{{artifact_url_encoded}}|$ARTIFACT_URL_ENCODED|g" \
  -e "s|{{version}}|$VERSION|g" \
  -e "s|{{datetime}}|$DATETIME|g" \
  "$TEMPLATE_FILE" > "$RENDERED_FILE"

echo "comment_file=$RENDERED_FILE" >> "$GITHUB_OUTPUT"
