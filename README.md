# CityMapp

CityMapp is an Android app made as a test for a job application at Ualá. 
This app allows users to search for cities, manage their favorite cities and see its location with an embedded map. 
The project is built using Kotlin, Jetpack Compose, MVVM, Coroutines, Retrofit for API calls, Koin for DI and Room for local storage.

## Approach to Solve the Search Problem

Components:

1. **Data Model**: The `CityResponse` class represents the API response and the `CityModel` class represents the project model to manage the Cities information.
2. **Room Database**: The `CitiesDatabase` class is the Room database that contains the `CitiesDao` DAO.
3. **DAO**: The `CitiesDao` interface defines the methods for interacting with the database, such as inserting, deleting, and retrieving the favorite cities.
4. **Dependency Injection**: The app uses `Koin` which is a lightweight and easy-to-use DI framework for Kotlin. In the `AppModule` file is defined all the dependencies for the app.
5. **Repository**: The `CitiesRepository` class is defined to abstract the data operations and provide a clean API call for the rest of the app.
6. **ViewModel**: The `CitiesViewModel` class manage the UI-related data and handle the search logic.

The search functionality is implemented using the following approach:

1. **Fetching Cities**: The `CitiesViewModel` class has a `fetchCities` method that retrieves the cities list from the gist provided.
2. **Processing the List**: Once the cities list is fetched from the API, it's processed in the Repository class (sort and load local favorites).
3. **Searching Cities**: When the user starts typing in the search bar, the `CitiesViewModel` class filters the cities list based on the search query using a simple search prefix algorithm.

If a time complexity of the search algorithm was required I would use another data structure like a Trie to store the cities list, which would allow for a time complexity of O(n) for the search operation. But this is not necessary for the current implementation.

## Getting Started

To run the app just clone the repository, but it would need a SHA-1 fingerprint registration on the Google Cloud Platform to use the Google Maps API.