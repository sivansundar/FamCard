<h1 align="center">Welcome to FamCard 👋</h1>

FamCard is an app that demonstrates the use of nested recycler views and contextual cards. The app makes use of Modern Android Architecture Components with the MVVM Architecture.


## 🛠 Built With

- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android
  development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - A coroutine is a
  concurrency design pattern that you can use on Android to simplify code that executes
  asynchronously.
- [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html) - A flow is conceptually a stream of data that can be computed asynchronously.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) -
  Collection of libraries that help you design robust, testable, and maintainable apps.
    - [Stateflow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow) - StateFlow is
      a state-holder observable flow that emits the current and new state updates to its collectors.
    - [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html) - A flow is an asynchronous
      version of a Sequence, a type of collection whose values are lazily produced.
    - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores
      UI-related data that isn"t destroyed on UI changes.
    - [Navigation](https://developer.android.com/guide/navigation/navigation-getting-started) - The
      Navigation component that helps us navigate to the different destinations in our app.
- [Material You Components for Android](https://github.com/material-components/material-components-android)
    - Modular and customizable Material Design UI components for Android. Material You available from version 1.5.0 onwards.
- [Coil](https://github.com/coil-kt/coil)
    - An image loading library for Android backed by Kotlin Coroutines.


## 📦 Package Structure

 ```
com.sivan.famcard
├── data                  # For data handling
│   ├── remote                # Contains files for setting up the api service, DTOs and repositories
│   │   ├── DTO               # Contains the Response DTOs for network requests.
|   |   ├── Repository        # Contains the repository implementaion.
├── di                        # Hilt DI Modules
├── domain                    # Contains usecases, domain models and the repository interface. 
│   ├── model                 # Holds the domain model classes.
|   ├── Repository            # Contains the repository interface that defines the method signatures for the repository. 
│   ├── Usecases              # Contains the usecase classes that communicates between the viewmodel and the repository. 
├── ui                        # Holds the UI layer of the app including the card group container adapter and the induvidual card adapter.
├── utils                     # Contains Extension functions and result classes.
│   ├── sharedpref            # Shared preference singleton class and its interface functions that help us access shared preferences in our app.
├── Screenshots               # App screenshots
└── MainActivity.kt           # MainActivity 
└── MyApplication.kt          # Application class

```

## 📷 Screenshots

|   Home Screen    
|---	
|  ![](https://github.com/sivansundar/FamCard/blob/master/screenshots/home_screen.png)

<br />

### ✅ Contributing guidelines:

- Open issue regarding proposed change.
- Repo owner will contact you there.
- If your proposed change is approved, Fork this repo and do changes.
- Open a PR and add a nice description to it.
- You're done!

Please refer to the [open issues](https://github.com/sivansundar/FamCard/issues) section before opening new issues.

## 🧰 Build-tool

[Android Studio (BumbleBee) | 2021 1.1 | Patch 2 or above](https://developer.android.com/studio) to build this project.
<br>

- minSDK -> 26
- targetSDK -> 32
- kotlin version -> 1.6.10
<br>

## 🔑 Setup secrets.credentials

To make sure that the API requests go through, do the following.

1. Go to the root of the project and create a new file called `secrets.properties`
2. Paste the following lines and change their values accordingly.
<br>

```
BASEURL="XXXXXX"
ENDPOINTURL="YYYYY"
```

## ❤️ Show your Support

If you learnt something from this repository or it helped you in anyway, feel free to star this project and let the world know what you've learnt.
Thanks! 😄
<br>

## 📩 Contact

Want to chat more about the project, or anything under the sun? Reach out to me at 👇

* Twitter: <a href="https://twitter.com/sivansundar" target="_blank">@sivansundar</a>
* Email: <a href="@mailto:hello@sivansundar.com" target="_blank">hello@sivansundar.com</a>

<br>
