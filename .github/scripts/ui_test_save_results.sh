#!/bin/bash
set -euo pipefail

# Run UI tests and capture the output
echo "▶️ Running UI tests: $UI_TEST_COMMANDS"
./gradlew $UI_TEST_COMMANDS --stacktrace --info > ui-test-results.log || true

echo "📄 ==== Detailed Test Results ===="
grep -E "(PASSED|FAILED|SKIPPED)" ui-test-results.log || echo "No tests were found!"
