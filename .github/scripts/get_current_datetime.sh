#!/bin/bash
set -e

# Get current date/time in formatted string
DATETIME=$(date +'%Y-%m-%d at %H:%M:%S')

# Export to GitHub environment for downstream use
echo "date-time=$DATETIME" >> "$GITHUB_ENV"
