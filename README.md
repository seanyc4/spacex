# 🚀 Orbital

<p align="center">
  <img src="app/src/main/ic_launcher-playstore.png" alt="Orbital Logo" width="120"/>
</p>

A modern Android application showcasing best practices in Android development. Orbital connects to the [Launch Library 2 API](https://thespacedevs.com/llapi) to display upcoming and past rocket launches from around the world.

## 📱 Features

- **Dual Launch Feeds**: Browse upcoming and past launches in a tabbed interface
- **Paginated Grid Layout**: Efficiently displays launch data with Paging 3 and RemoteMediator
- **Rich Detail Views**: Comprehensive launch information including:
  - Launch countdown/status
  - Mission details and patches
  - Rocket specifications with stage information
  - Launch site with map integration
  - YouTube video integration for launch streams
  - Launch provider details
  - Live updates feed
- **Filter & Search**: Filter launches by status (Success, Failure, In Flight, etc.) and search by name
- **Adaptive Layout**: List-detail layout on tablets, single pane on phones using Material 3 Adaptive
- **Offline Support**: Caches launch data in Room database with automatic refresh
- **Process Death Survival**: Full state restoration using SavedStateHandle
- **Dark & Light Mode**: Material 3 dynamic theming support

## 🏗️ Architecture

This project follows **Clean Architecture** principles with a clear separation of concerns across layers. The architecture is inspired by:
- [Understanding the Data Layer](https://medium.com/asos-techblog/android-clean-architecture-understanding-the-data-layer-a8df03b508c2)
- [Understanding the Domain Layer](https://medium.com/asos-techblog/android-clean-architecture-understanding-the-domain-layer-28bf81d83ef7)

### Architecture Diagram

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                              PRESENTATION LAYER                               │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌────────────────────┐      ┌────────────────────┐                         │
│  │   LaunchesScreen   │      │    LaunchScreen    │                         │
│  │    (Composable)    │      │    (Composable)    │                         │
│  └─────────┬──────────┘      └─────────┬──────────┘                         │
│            │                           │                                     │
│            ▼                           ▼                                     │
│  ┌────────────────────┐      ┌────────────────────┐                         │
│  │  LaunchesViewModel │      │   LaunchViewModel  │                         │
│  │   (HiltViewModel)  │      │ (AssistedInject)   │                         │
│  └─────────┬──────────┘      └─────────┬──────────┘                         │
│            │                           │                                     │
│            └───────────┬───────────────┘                                    │
│                        ▼                                                     │
│            ┌────────────────────┐                                           │
│            │  LaunchesComponent │  ← Facade for all use cases               │
│            │    (Interface)     │                                           │
│            └─────────┬──────────┘                                           │
│                      │                                                       │
└──────────────────────┼───────────────────────────────────────────────────────┘
                       │
┌──────────────────────┼───────────────────────────────────────────────────────┐
│                      ▼              DOMAIN LAYER                             │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │                           USE CASES                                   │   │
│  ├──────────────────┬──────────────────┬────────────────────────────────┤   │
│  │ObserveUpcoming-  │ ObservePast-     │ GetLaunchUseCase               │   │
│  │LaunchesUseCase   │ LaunchesUseCase  │ (filters YouTube videos)       │   │
│  └────────┬─────────┴────────┬─────────┴─────────────┬──────────────────┘   │
│           │                  │                       │                       │
│           └──────────────────┼───────────────────────┘                      │
│                              ▼                                               │
│                 ┌────────────────────┐                                      │
│                 │ LaunchesRepository │  ← Domain Interface                  │
│                 │    (Interface)     │                                      │
│                 └─────────┬──────────┘                                      │
│                           │                                                  │
│  ┌────────────────────────┼─────────────────────────────────────────────┐   │
│  │                        │          DOMAIN MODELS                       │   │
│  │  ┌─────────────┐  ┌────┴────────┐  ┌────────────┐  ┌──────────────┐  │   │
│  │  │   Launch    │  │LaunchSummary│  │   Rocket   │  │    Status    │  │   │
│  │  │  (Detail)   │  │  (List)     │  │   + Pad    │  │   + Agency   │  │   │
│  │  └─────────────┘  └─────────────┘  └────────────┘  └──────────────┘  │   │
│  └──────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│                               DATA LAYER                                      │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│            ┌────────────────────────┐                                        │
│            │LaunchesRepositoryImpl  │                                        │
│            └───────────┬────────────┘                                        │
│                        │                                                     │
│         ┌──────────────┼──────────────┐                                     │
│         ▼              ▼              ▼                                     │
│  ┌─────────────┐ ┌───────────┐ ┌──────────────────┐                         │
│  │PagerFactory │ │RemoteData │ │DetailLocalData   │                         │
│  │             │ │Source     │ │Source            │                         │
│  └──────┬──────┘ └─────┬─────┘ └────────┬─────────┘                         │
│         │              │                │                                   │
│         ▼              │                │                                   │
│  ┌──────────────────┐  │                │                                   │
│  │  RemoteMediator  │◄─┘                │                                   │
│  │ (Upcoming/Past)  │                   │                                   │
│  └────────┬─────────┘                   │                                   │
│           │                             │                                   │
│           ▼                             ▼                                   │
│  ┌────────────────────────────────────────────────────────────┐             │
│  │                    DATA SOURCES                            │             │
│  ├──────────────────────────┬─────────────────────────────────┤             │
│  │    LOCAL (Room Cache)    │         REMOTE (Retrofit)       │             │
│  ├──────────────────────────┼─────────────────────────────────┤             │
│  │  UpcomingLaunchDao       │                                 │             │
│  │  PastLaunchDao           │         LaunchApi               │             │
│  │  UpcomingDetailDao       │    GET /2.3.0/launches/         │             │
│  │  PastDetailDao           │      upcoming/?mode=detailed    │             │
│  │  RemoteKeyDao (x2)       │      previous/?mode=detailed    │             │
│  └──────────────────────────┴─────────────────────────────────┘             │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘
```

### Data Flow

1. **UI Layer**: Composable screens observe `StateFlow`/`PagingData` from ViewModels
2. **ViewModel**: Uses `LaunchesComponent` facade to interact with use cases
3. **Use Cases**: Single-responsibility operations (observe launches, get details)
4. **Repository**: Coordinates between remote and local data sources
5. **RemoteMediator**: Manages pagination, caching strategy, and network requests
6. **Data Sources**: 
   - **Remote**: Retrofit API calls to Launch Library 2
   - **Local**: Room database for offline caching with separate tables for upcoming/past launches

## 📦 Module Structure

The project uses a multi-module architecture with **14 modules** following the API/Implementation pattern:

```
Orbital/
├── app                          # Application entry point, navigation, DI
├── benchmark                    # Macrobenchmark tests & Baseline Profile generation
├── build-logic                  # Shared build configuration
├── buildSrc                     # Custom Gradle plugins
│
├── core/
│   ├── common                   # Shared utilities (Result types, Coroutines, Formatters)
│   ├── data                     # Data layer constants
│   ├── datastore/
│   │   ├── api                  # DataStore contract interfaces
│   │   └── implementation       # DataStore implementations
│   ├── datastore-proto          # Protocol Buffer definitions
│   ├── domain                   # Shared domain types (Order, LaunchesType)
│   ├── hilt-ui-test             # Hilt testing utilities
│   ├── navigation               # Navigation 3 routes and scene strategies
│   ├── test                     # Test utilities and custom test runner
│   └── ui                       # Design system, components, theming
│
├── database                     # Room database, DAOs, entities
│
└── feature/
    └── launch                   # Launch feature (data, domain, presentation)
```

### Module Dependencies

- **`:app`** → `:feature:launch`, `:core:*`, `:database`
- **`:feature:launch`** → `:core:common`, `:core:domain`, `:core:ui`, `:database`
- **`:database`** → Standalone, no feature dependencies

## 🛠️ Tech Stack

### Core
| Technology | Version | Purpose |
|------------|---------|---------|
| Kotlin | 2.3.0 | Programming language |
| Compose BOM | 2025.12.01 | UI toolkit |
| Navigation 3 | 1.0.0 | Type-safe navigation |
| Hilt | 2.57.2 | Dependency injection |
| Coroutines | 1.10.2 | Asynchronous programming |

### Data
| Technology | Version | Purpose |
|------------|---------|---------|
| Room | 2.8.4 | Local database |
| Retrofit | 3.0.0 | Network requests |
| OkHttp | 5.3.2 | HTTP client |
| Paging 3 | 3.4.0-beta01 | Pagination with RemoteMediator |
| DataStore Proto | 1.1.4 | Type-safe preferences |
| Kotlinx Serialization | 1.9.0 | JSON parsing |

### UI
| Technology | Version | Purpose |
|------------|---------|---------|
| Material 3 | Latest | Design system |
| Material 3 Adaptive | 1.3.0-alpha05 | Responsive layouts |
| Coil | 3.3.0 | Image loading |
| YouTube Player | 13.0.0 | Video playback |
| Splash Screen API | 1.2.0 | Launch experience |

### Testing
| Technology | Version | Purpose |
|------------|---------|---------|
| JUnit 4 | 4.13.2 | Unit testing |
| MockK | 1.14.6 | Mocking framework |
| Turbine | 1.2.1 | Flow testing |
| Robolectric | 4.16 | Android unit tests |
| Compose Testing | Latest | UI testing |
| MockWebServer | 5.3.2 | API mocking |

### DevOps
| Technology | Purpose |
|------------|---------|
| GitHub Actions | CI/CD pipelines |
| Firebase Crashlytics | Crash reporting |
| Baseline Profiles | Startup optimization |
| Timber | Logging |

## 🧪 Testing

The project includes comprehensive tests across all layers:

### Unit Tests
Located in `feature/launch/src/test/`:
- **ViewModels**: `LaunchesViewModelTest`, `LaunchViewModelTest`, `FilterBottomSheetViewModelTest`
- **Use Cases**: `LaunchesComponentImplTest`, `GetLaunchUseCaseTest`
- **Repository**: `LaunchesRepositoryImplTest`
- **Mappers**: `LaunchUiMapperTest`, `LaunchesLocalMappingExtensionsTest`
- **Remote Mediator**: `LaunchesRemoteMediatorTest`

### UI Tests (Instrumented)
Located in `feature/launch/src/androidTest/`:
- `LaunchesScreenTest` - List screen interactions
- `LaunchScreenTest` - Detail screen display
- `FilterBottomSheetTest` - Filter functionality
- `LaunchCardTest` - Individual card rendering
- `RocketSectionTest`, `VideoSectionTest`, `UpdatesSectionTest` - Component tests

### Running Tests
```bash
# Unit tests
./gradlew test

# UI tests (requires emulator/device)
./gradlew connectedAndroidTest

# All tests
./gradlew check
```

## 🔄 CI/CD

The project includes **20 GitHub Actions workflows** for comprehensive automation:

### Pull Request Flow
```
PR Opened/Updated
       │
       ├─► Danger (Lint/PR checks)
       ├─► Get App Version
       │         │
       │         ▼
       ├─► Assemble Debug APK
       │         │
       │         ├─► Unit Tests
       │         ├─► UI Tests
       │         └─► APK Size Analysis → PR Comment
       │
       └─► Assemble Signed Dev Release APK → QR Code PR Comment
```

### Release Flow
```
Manual Trigger
       │
       ├─► Unit Tests
       ├─► UI Tests
       │         │
       │         ▼
       ├─► Get Current Version
       │         │
       │         ▼
       ├─► Assemble & Sign Release AAB
       │         │
       │         ▼
       ├─► Deploy to Play Store (Internal Track)
       │         │
       │         ▼
       ├─► Tag Release
       │         │
       │         ▼
       ├─► Version Bump (version code + name)
       │         │
       │         ▼
       └─► Create GitHub Release with Changelog
```

### Available Workflows
| Workflow | Trigger | Purpose |
|----------|---------|---------|
| `pull_request.yml` | PR events | Full PR validation |
| `release.yml` | Manual | Production release |
| `unit_test.yml` | Reusable | Run unit tests |
| `ui_test.yml` | Reusable | Run instrumented tests |
| `apk_size.yml` | Reusable | APK size diff analysis |
| `playstore_deploy.yml` | Reusable | Play Store deployment |
| `generate_baseline_profile.yaml` | Manual | Generate startup profiles |

## 📐 Key Design Patterns

### MVI (Model-View-Intent)
- **State**: Single immutable state class per screen (`LaunchesState`, `LaunchUiState`)
- **Events**: Sealed classes for user actions (`LaunchesEvents`, `LaunchEvent`)
- **ViewModel**: Transforms intents to state updates

### RemoteMediator Pattern
The app uses Paging 3's `RemoteMediator` for seamless offline-first pagination:
- Automatic cache invalidation (1-hour timeout)
- Query change detection triggers refresh
- Separate mediators for upcoming/past launches

### Repository Pattern with Data Source Abstraction
```kotlin
LaunchesRepository
    ├── LaunchesRemoteDataSource (API)
    ├── LaunchesLocalDataSource (Room - List)
    └── DetailLocalDataSource (Room - Detail)
```

### Assisted Injection
The `LaunchViewModel` uses Hilt's `@AssistedInject` for runtime parameters:
```kotlin
@HiltViewModel(assistedFactory = LaunchViewModel.Factory::class)
class LaunchViewModel @AssistedInject constructor(
    @Assisted private val launchId: String,
    @Assisted private val launchType: LaunchesType
)
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug or newer
- JDK 17+
- Android SDK 35

### Setup
1. Clone the repository
```bash
git clone https://github.com/seanyc4/orbital.git
```

2. Open in Android Studio

3. Sync Gradle and run on device/emulator

### Build Variants
- **debug**: Development build with logging
- **release**: Production build with ProGuard/R8

## 📄 API Reference

The app connects to [Launch Library 2 API](https://thespacedevs.com/llapi):

| Endpoint | Description |
|----------|-------------|
| `GET /2.3.0/launches/upcoming/` | Upcoming launches |
| `GET /2.3.0/launches/previous/` | Past launches |
| `GET /2.3.0/launches/upcoming/{id}/` | Single upcoming launch detail |
| `GET /2.3.0/launches/previous/{id}/` | Single past launch detail |

Query parameters: `mode=detailed`, `limit`, `offset`, `search`, `status`

## 📝 License

```
Copyright 2026 Sean Coyle

Licensed under the Apache License, Version 2.0
```

---

Built using modern Android development best practices
