
## Overview

This app is for demonstration purposes to show good coding practices in android development.
SpaceX connects to the SpaceX API at https://github.com/r-spacex/SpaceX-API

- Data is retrieved from two end points: "Company Info" and "All Launches"
- The data is displayed in a multi view type recyclerview.
- Dates and Strings and converted/formatted in the network mapper class during the network call in Dispatches.IO. This avoids putting any logic into the recyclerview adapter, resulting in much better performance.
- Data in the recyclerview list can be filtered by ASC/DESC, Year or whether the launch was a success or not.
- The data is also paginated from Room, loading 30 items at a time.
- A modal bottom sheet is used to present launch media links if available. These are external links to other websites.
- The app will survive process death and restore the state and list position.

## Tech stack

* Clean Architecture
* Coroutines
* Datastore
* Flows and channels
* Glide
* Hilt Dependency Injection
* Kotlin
* Instrumentation testing (Espresso and androidx.test)
* Leak Canary
* Live Data
* MVI architectural pattern
* Navigation Component
* Retrofit2
* Room Persistence
* Pagination
* Splash Screen
* Timber
* Unit tests (Junit5)

![](images/clean_architecture.png)


## Testing

Unit and instrumentation tests can be ran either manually in android studio or else by executing the script ui_and_unit_tests.sh in a terminal or gitbash window. The script is located in the /tests directory.


## Future updates

* Refactor to modular design pattern
* Change from XML to compose
* Add timestamps to cached data to prevent unnecessary network calls as the SpaceX data doesnt change frequently. Check the timestamps, if more than 7 days old then do a new network call.
* Add a no results found animation
* Design a landscape tablet layout
* Use Paging3 Jetpack library


## Acknowledgements

Mitch Tabian - Awesome course materials to help learn the clean architecture pattern.
