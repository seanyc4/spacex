#!/bin/bash
set -e

# This script is used to get the list of modules that have androidTest tasks
MODULES=$(./gradlew printAndroidTestModules | grep "Module with androidTest" | cut -d: -f2- | xargs)
test_modules=""
for module in $MODULES
do
  test_modules+="$module:connectedAndroidTest "
done
echo "UI_TEST_COMMANDS=${test_modules}" >> $GITHUB_ENV
