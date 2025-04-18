
## Overview

This app is for demonstration purposes to show good coding practices in android development.
SpaceX connects to the SpaceX API at https://github.com/r-spacex/SpaceX-API

- Data is retrieved from two end points: "Company Info" and "All Launches"
- The data is displayed in a multi view type LazyVerticalGrid using a Kotlin Sealed Class.
- Data in the grid can be filtered by "ASC/DESC", "Launch Year" or the "Launch Status".
- The data is also paginated from Room, loading 30 items at a time.
- A modal bottom sheet is used to present launch media links if available. These are external links to other websites.
- The app will survive process death and restore the state and list position.
- Modularised using api/implementation pattern : We only expose the contract module to other modules so we can avoid recompiles if the implementation changes

## Tech stack

* Clean Architecture
* MVI
* KSP
* Compose
* Coroutines
* Datastore Preferences
* Datastore Proto
* State Flow
* Splash Screen API
* Material 3 Dark & Light Mode
* Navigation Component
* Hilt Dependency Injection
* Retrofit2
* Room Persistence
* Pagination
* Crashlytics
* Timber
* Glide Compose
* TOML Dependency Catalog

## Testing

* Unit Tests
* UI Tests
* Integration Tests

## CI/CD
A full suite of YML files are provided to build, test and deploy the app to the Play Store.

The Pull Request flow includes:
- Lint checks
- Unit/UI tests
- Assembles a debug APK
- APK Size check: diffs the PR branch to main & posts a PR comment
- Assembles a signed test release APK & posts a QR code as a PR comment for easy downloading/testing.

The Nightly Release flow includes:
- Checks if new commits are available since the last tagged release
- Checks the current app version & tags the release
- Creates a changelog from the commit messages since the last release
- Bumps the version code and version name in the build.gradle file
- Unit/UI tests
- Assembles & signs the release AAB
- Deploys to the Play Store internal track
