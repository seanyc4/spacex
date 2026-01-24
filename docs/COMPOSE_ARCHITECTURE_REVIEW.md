# Compose Architecture Review & Golden Path Pattern

> **Senior Android Architect Analysis**  
> Focus: Jetpack Compose, Clean Architecture, SOLID, Unidirectional Data Flow, Testability

---

## 📋 Executive Summary

After thorough analysis of the Orbital codebase, the overall architecture is **well-designed** and follows many best practices. However, several improvements are needed to achieve full consistency and scalability.

### Overall Assessment: ✅ 8/10

**Strengths:**
- ✅ Clean separation of Route/Screen/Content pattern
- ✅ ViewModels own business logic
- ✅ StateFlow-based state management
- ✅ Good test coverage for ViewModels
- ✅ Consistent use of sealed interfaces for events/state
- ✅ Previews present in most composables

**Areas for Improvement:**
- ⚠️ Naming inconsistencies
- ⚠️ Missing previews in some files
- ⚠️ Event naming inconsistency
- ⚠️ Some logic leaks in composables
- ⚠️ Missing Compose UI tests for some components

---

## 🚨 Issues Found

### 1. Naming Convention Violations

| File | Current Name | Issue | Suggested Name |
|------|--------------|-------|----------------|
| `LaunchesGrid.kt` | `LaunchesGrid()` | ✅ FIXED - Now indicates it's a grid component | - |
| `LaunchesCard.kt` | `LaunchCard()` | Missing "es" for consistency | `LaunchesCard()` or keep as `LaunchCard()` |
| `LaunchesEvents.kt` | `LaunchesEvents` | "Events" is plural but sealed interface | `LaunchesEvent` (singular) |
| `LaunchEvent.kt` | `LaunchEvent` | Correct ✅ | - |
| `LaunchesUiState.kt` | `LaunchesUiState` | Correct ✅ (Renamed) | - |
| `FilterBottomSheetState.kt` | Events defined in State file | Events should be in separate file | Move `FilterBottomSheetEvent` to own file |

### 2. State Hoisting Violations

#### File: `LaunchScreen.kt` (Line 42-44)
```kotlin
// ❌ VIOLATION: Local state for scroll position
val scrollState = rememberSaveable(saver = ScrollState.Saver) {
    ScrollState(0)
}
```

**Problem:** Scroll state is UI-only state, which is acceptable, but inconsistent with `Launches.kt` where scroll position is hoisted to ViewModel.

**Recommendation:** Decide on a consistent pattern - either all scroll states are local (acceptable for UI-only state) or all are hoisted.

#### File: `Launches.kt` (Line 54-56)
```kotlin
// ⚠️ INCONSISTENCY: Scroll position tracked in ViewModel
val gridState = remember(launchesType) {
    LazyGridState()
}
```

**Decision:** Keep scroll position hoisting to ViewModel as it supports restoration across process death. This is the correct approach.

### 3. Architecture Boundary Violations

#### File: `RocketConfigurationCard.kt` (Lines 330-342)
```kotlin
// ❌ VIOLATION: Business logic in composable
val context = LocalContext.current
config.wikiUrl?.let { wikiUrl ->
    LinkButton(
        text = stringResource(R.string.rocket_on_wikipedia),
        icon = Icons.Default.Info,
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, wikiUrl.toUri())
            context.startActivity(intent)  // Logic leak!
        }
    )
}
```

**Problem:** Intent creation and activity starting is business logic that should be handled via events.

**Fix:** Emit an event and let the ViewModel/Route handle navigation.

### 4. Missing Previews

| File | Status |
|------|--------|
| `LaunchesRoute.kt` | ❌ No preview (acceptable - Route has ViewModel) |
| `LaunchRoute.kt` | ❌ No preview (acceptable - Route has ViewModel) |
| `FilterBottomSheet.kt` | ⚠️ Partial - only some composables have previews |
| `Launches.kt` | ❌ No preview |

### 5. Event Naming Inconsistency

```kotlin
// File: LaunchesEvents.kt
sealed interface LaunchesEvents {  // ❌ Plural
    data object DismissFilterBottomSheetEvent : LaunchesEvents  // ❌ "Event" suffix redundant
    data object PullToRefreshEvent : LaunchesEvents
}

// File: LaunchEvent.kt  
sealed interface LaunchEvent {  // ✅ Singular
    data object RetryFetch : LaunchEvent  // ✅ No redundant suffix
    data object PullToRefreshEvent : LaunchEvent  // ❌ Inconsistent with above
}
```

---

## 🏆 Golden Path Pattern

### 1. File Structure

```
feature/
└── featurename/
    ├── FeatureRoute.kt          # Route: ViewModel integration
    ├── FeatureScreen.kt         # Screen: Scaffold, top-level layout
    ├── FeatureViewModel.kt      # ViewModel: All business logic
    ├── components/
    │   ├── FeatureContent.kt    # Main content composable
    │   ├── FeatureItem.kt       # Reusable item components
    │   └── FeatureHeader.kt     # Section components
    ├── model/
    │   └── FeatureUi.kt         # UI models
    └── state/
        ├── FeatureUiState.kt    # State sealed interface
        └── FeatureEvent.kt      # Event sealed interface
```

### 2. Naming Convention Reference

| Type | Pattern | Example |
|------|---------|---------|
| Route | `FeatureRoute()` | `LaunchesRoute()` |
| Screen | `FeatureScreen()` | `LaunchesScreen()` |
| Content | `FeatureContent()` | `LaunchesContent()` |
| List | `FeatureList()` | `LaunchesList()` |
| Card/Item | `FeatureCard()` / `FeatureItem()` | `LaunchCard()` |
| State | `FeatureUiState` | `LaunchesUiState` |
| Event | `FeatureEvent` (singular!) | `LaunchesEvent` |
| ViewModel | `FeatureViewModel` | `LaunchesViewModel` |

### 3. Canonical Example

```kotlin
// ==========================================
// FILE: FeatureRoute.kt
// ==========================================
@Composable
fun FeatureRoute(
    viewModel: FeatureViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // Handle one-time effects
    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is NavigationEvent.ToDetail -> onNavigateToDetail(event.id)
            }
        }
    }
    
    FeatureScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )
}

// ==========================================
// FILE: FeatureScreen.kt
// ==========================================
@Composable
fun FeatureScreen(
    uiState: FeatureUiState,
    onEvent: (FeatureEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { /* ... */ },
        modifier = modifier,
    ) { paddingValues ->
        when (uiState) {
            is FeatureUiState.Loading -> LoadingState()
            is FeatureUiState.Success -> FeatureContent(
                data = uiState.data,
                onEvent = onEvent,
                modifier = Modifier.padding(paddingValues),
            )
            is FeatureUiState.Error -> ErrorState(
                message = uiState.message,
                onRetry = { onEvent(FeatureEvent.Retry) },
            )
        }
    }
}

@PreviewDarkLightMode
@Composable
private fun FeatureScreenSuccessPreview() {
    AppTheme {
        FeatureScreen(
            uiState = FeatureUiState.Success(sampleData()),
            onEvent = {},
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun FeatureScreenLoadingPreview() {
    AppTheme {
        FeatureScreen(
            uiState = FeatureUiState.Loading,
            onEvent = {},
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun FeatureScreenErrorPreview() {
    AppTheme {
        FeatureScreen(
            uiState = FeatureUiState.Error("Unable to load data"),
            onEvent = {},
        )
    }
}

// ==========================================
// FILE: state/FeatureUiState.kt
// ==========================================
sealed interface FeatureUiState {
    data object Loading : FeatureUiState
    data class Success(val data: FeatureData) : FeatureUiState
    data class Error(val message: String) : FeatureUiState
}

// ==========================================
// FILE: state/FeatureEvent.kt
// ==========================================
sealed interface FeatureEvent {
    data object Retry : FeatureEvent
    data class ItemClicked(val id: String) : FeatureEvent
    data object PullToRefresh : FeatureEvent
}

// ==========================================
// FILE: FeatureViewModel.kt
// ==========================================
@HiltViewModel
class FeatureViewModel @Inject constructor(
    private val getFeatureUseCase: GetFeatureUseCase,
    private val uiMapper: FeatureUiMapper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FeatureUiState>(FeatureUiState.Loading)
    val uiState: StateFlow<FeatureUiState> = _uiState.asStateFlow()
    
    private val _navigationEvents = Channel<NavigationEvent>(Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    init {
        loadData()
    }

    fun onEvent(event: FeatureEvent) {
        when (event) {
            is FeatureEvent.Retry -> loadData()
            is FeatureEvent.PullToRefresh -> loadData(forceRefresh = true)
            is FeatureEvent.ItemClicked -> navigateToDetail(event.id)
        }
    }

    private fun loadData(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = FeatureUiState.Loading
            when (val result = getFeatureUseCase(forceRefresh)) {
                is Result.Success -> {
                    _uiState.value = FeatureUiState.Success(
                        uiMapper.mapToUi(result.data)
                    )
                }
                is Result.Error -> {
                    _uiState.value = FeatureUiState.Error(result.message)
                }
            }
        }
    }
    
    private fun navigateToDetail(id: String) {
        viewModelScope.launch {
            _navigationEvents.send(NavigationEvent.ToDetail(id))
        }
    }
}
```

---

## ✅ PR Checklist

### Composable Review Checklist

- [ ] **Naming:** Follows `FeatureComponent()` pattern
- [ ] **Stateless:** No `remember { mutableStateOf() }` for business state
- [ ] **State hoisting:** Receives `uiState` and `onEvent` parameters
- [ ] **No logic:** No data transformation, repository access, or use case calls
- [ ] **No coroutines:** Only `LaunchedEffect` for UI-only effects
- [ ] **Previews:** At least one `@Preview` with fake data
- [ ] **Accessibility:** `semantics`, `contentDescription`, `testTag` where needed

### ViewModel Review Checklist

- [ ] **State:** Exposes `StateFlow<UiState>`
- [ ] **Events:** Has `onEvent(event: Event)` function
- [ ] **Navigation:** Uses `Channel` for one-time navigation events
- [ ] **Mapping:** Contains UI mapper for domain → UI conversion
- [ ] **No Android dependencies:** No Context, View, or Activity references
- [ ] **Testable:** All dependencies injected via constructor

### State/Event Review Checklist

- [ ] **State:** Sealed interface with Loading/Success/Error
- [ ] **Events:** Sealed interface, singular name (not `Events`)
- [ ] **No redundancy:** Event names don't have "Event" suffix
- [ ] **Immutable:** State classes are `data class` or `data object`

### Testing Checklist

- [ ] **ViewModel tests:** 100% coverage of `onEvent` handlers
- [ ] **State tests:** All state transitions verified
- [ ] **Compose tests:** Screen renders correctly for each state
- [ ] **Event tests:** Event callbacks are invoked correctly

---

## 🔧 Completed Refactoring

### ✅ Naming Convention Fixes (Completed)

1. **`LaunchesEvents` → `LaunchesEvent`** - Renamed to singular form
2. **Removed redundant "Event" suffix** from all event names:
   - `DismissFilterBottomSheetEvent` → `DismissFilterBottomSheet`
   - `DisplayFilterBottomSheetEvent` → `DisplayFilterBottomSheet`
   - `PullToRefreshEvent` → `PullToRefresh`
   - `RetryFetchEvent` → `RetryFetch`
   - `UpdateFilterStateEvent` → `UpdateFilterState`
   - `TabSelectedEvent` → `TabSelected`
3. **`LaunchEvent`** - Fixed naming:
   - `RetryFetch` → `Retry`
   - `PullToRefreshEvent` → `PullToRefresh`
4. **Added previews to `Launches.kt`**

### Updated Files
- `LaunchesEvents.kt` - Renamed interface and events
- `LaunchesViewModel.kt` - Updated event handling
- `LaunchesScreen.kt` - Updated event references
- `Launches.kt` - Updated event references and added preview
- `LaunchEvent.kt` - Fixed event names
- `LaunchViewModel.kt` - Updated event handling
- `LaunchRoute.kt` - Updated event references
- `LaunchesViewModelTest.kt` - Updated test event references
- `LaunchViewModelTest.kt` - Updated test event references

---

## 🔧 Remaining Refactoring

### Priority 2: Architecture Fixes

1. **Extract Intent handling from composables** - Move to event-based navigation
2. **Standardize scroll position handling** - Document decision (hoisted vs. local)

### Priority 3: File Rename (Optional)

1. **Rename file `LaunchesEvents.kt` → `LaunchesEvent.kt`** - File rename (content already updated)

---

## 📁 Files Requiring Changes

| File | Changes Required | Priority | Status |
|------|------------------|----------|--------|
| `LaunchesEvents.kt` | Rename to `LaunchesEvent.kt`, remove "Event" suffix | P1 | Pending |
| `LaunchesState.kt` | Rename to `LaunchesUiState.kt` | P1 | ✅ Done |
| `RocketConfigurationCard.kt` | Extract Intent handling to event | P2 | ✅ Done |
| `Launches.kt` | Renamed to `LaunchesGrid.kt`, composable to `LaunchesGrid()` | P3 | ✅ Done |
| `FilterBottomSheetState.kt` | Move events to separate file | P3 | Pending |
| `FilterBottomSheet.kt` | Complete preview coverage | P3 | ✅ Done |

---

## 📊 Test Coverage Analysis

### ViewModel Tests ✅

| ViewModel | Test File | Coverage |
|-----------|-----------|----------|
| `LaunchesViewModel` | `LaunchesViewModelTest.kt` | ✅ Comprehensive |
| `LaunchViewModel` | `LaunchViewModelTest.kt` | ✅ Comprehensive |
| `FilterBottomSheetViewModel` | `FilterBottomSheetViewModelTest.kt` | ✅ Present |

### Compose UI Tests ✅

| Component | Test File | Status |
|-----------|-----------|--------|
| `LaunchesScreen` | `LaunchesScreenTest.kt` | ✅ Comprehensive |
| `LaunchScreen` | `LaunchScreenTest.kt` | ✅ Present |
| `LaunchCard` | `LaunchCardTest.kt` | ✅ Present |
| `FilterBottomSheet` | `FilterBottomSheetTest.kt` | ✅ Present |
| `LaunchesGrid` | `LaunchesScreenTest.kt` | ✅ Renamed and tested |
| `RocketSection` | `RocketSectionTest.kt` | ✅ Present |
| `VideoSection` | `VideoSectionTest.kt` | ✅ Present |
| `UpdatesSection` | `UpdatesSectionTest.kt` | ✅ Present |
| `CommonComponents` | `CommonComponentsTest.kt` | ✅ NEW |
| `MissionPatchesSection` | `MissionPatchesSectionTest.kt` | ✅ NEW |
| `LaunchProviderSection` | `LaunchProviderSectionTest.kt` | ✅ NEW |
| `LaunchHeroSection` | `LaunchHeroSectionTest.kt` | ✅ NEW |
| `LaunchSiteSection` | `LaunchLocationSectionTest.kt` | ✅ NEW |
| `LaunchDetailsSection` | `MissionDetailsSectionTest.kt` | ✅ NEW |

---

## 🎯 Conclusion

The Orbital codebase demonstrates solid architectural foundations with room for naming standardization and minor consistency improvements. The team should:

1. **Adopt the Golden Path pattern** for all new features
2. **Gradually refactor** existing code during feature work
3. **Enforce the PR checklist** in code reviews
4. **Document decisions** about scroll state handling

This analysis provides a clear roadmap for achieving architectural consistency across the codebase.
