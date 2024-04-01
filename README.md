
## Overview

This app is for demonstration purposes to show good coding practices in android development.
SpaceX connects to the SpaceX API at https://github.com/r-spacex/SpaceX-API

- Data is retrieved from two end points: "Company Info" and "All Launches"
- The data is displayed in a multi view type LazyVerticalGrid using a Kotlin Sealed Class.
- Data in the grid can be filtered by ASC/DESC, Year or whether the launch success status.
- The data is also paginated from Room, loading 30 items at a time.
- A modal bottom sheet is used to present launch media links if available. These are external links to other websites.
- The app will survive process death and restore the state and list position.
- Modularised using api/implementation pattern : We only expose the contract module to other modules so we can avoid recompiles if the implementation changes

## Tech stack

* Clean Architecture
* MVVM architectural pattern
* KSP
* Coroutines
* Datastore
* State Flow
* Splash Screen API
* Dark & Light Mode Support
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