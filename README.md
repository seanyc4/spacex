
## Overview

This app is for demonstration purposes to show good coding practices in android development.
SpaceX connects to the SpaceX API at https://github.com/r-spacex/SpaceX-API

- Data is retrieved from two end points: "Company Info" and "All Launches"
- The data is displayed in a multi view type recyclerview in xml or composables depending on which branch you are in.
- Data in the list can be filtered by ASC/DESC, Year or whether the launch was a success or not.
- The data is also paginated from Room, loading 30 items at a time.
- A modal bottom sheet is used to present launch media links if available. These are external links to other websites.
- The app will survive process death and restore the state and list position.
- Modularised using contract / implementation pattern : We only expose the contract module to other modules so we can avoid recompiles if the implementation changes

## Tech stack

* Clean Architecture
* MVI architectural pattern
* Coroutines
* Datastore
* Flows and channels
* State Flow
* Navigation Component
* Hilt Dependency Injection
* Espresso Instrumentation Testing
* Unit tests (Junit5)
* Retrofit2
* Room Persistence
* Pagination
* Splash Screen
* Timber
* Glide


## Testing

Unit and instrumentation tests can be ran either manually in android studio or else by executing the script ui_and_unit_tests.sh in a terminal or gitbash window. The script is located in the /tests directory.


## Acknowledgements

Mitch Tabian - Awesome course materials to help learn the clean architecture pattern.
