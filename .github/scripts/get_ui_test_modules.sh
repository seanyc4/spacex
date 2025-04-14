#!/bin/bash
set -e


MODULES=$(./gradlew printAndroidTestModules | grep "Module with androidTest" | cut -d: -f2- | xargs)
test_modules=""
for module in $MODULES
do
  test_modules+="$module:connectedAndroidTest "
done
echo "UI_TEST_COMMANDS=${test_modules}" >> $GITHUB_ENV
