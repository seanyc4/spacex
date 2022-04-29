
## Overview

This app is for demonstration purposes to show good coding practices in android development. The app itself is quiet simple. It displays a list of movies from tmdb using an open api. When a user clicks on a movie they can see more detailed information including the cast, overview etc.


## Tech stack

* Clean Architecture
* MVI architectural pattern
* Dagger2
* Kotlin
* Coroutines
* Flows and channels
* Unit tests (Junit5)
* Instrumentation testing (Espresso and androidx.test)
* Navigation Component
* Room Persistence
* Retrofit2
* Window insets

![](images/clean_architecture.png)


## Installation

Clone the git repository and open in the latest version of android studio.
* Register for an account on tmdb.com and get your free api key
* Open the constants file in the util package.
* Paste your api key into the const val API_KEY = ""
* Compile and run


## Testing

Unit and instrumentation tests can be ran either manually in android studio or else by executing the script ##ui_and_unit_tests.sh in a terminal or gitbash window. The script is located in the /tests directory.


## Future updates

* Add pagination to the movie list recycler view 
* Refactor dagger2 to hilt
* Refactor to modular design pattern
* Change from XML to compose


## Acknowledgements

Mitch Tabian - Awesome course materials to help learn the clean architecture pattern.
