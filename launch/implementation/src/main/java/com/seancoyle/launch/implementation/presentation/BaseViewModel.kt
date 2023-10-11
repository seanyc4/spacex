package com.seancoyle.launch.implementation.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel: ViewModel() {

    protected val _uiState = MutableStateFlow(LaunchState())
    val uiState: StateFlow<LaunchState> = _uiState

    protected val _scrollPosition = MutableStateFlow(0)
    val scrollPosition: StateFlow<Int> get() = _scrollPosition

    protected val _isDialogFilterDisplayed = MutableStateFlow(false)
    val isDialogFilterDisplayed: StateFlow<Boolean> get() = _isDialogFilterDisplayed

    protected val _launchFilter = MutableStateFlow<Int?>(null)
    val launchFilter: StateFlow<Int?> get() = _launchFilter

    protected val _order = MutableStateFlow<String?>(null)
    val order: StateFlow<String?> get() = _order

    protected val _year = MutableStateFlow("")
    val year: StateFlow<String> get() = _year

    protected val _page = MutableStateFlow(1)
    val page: StateFlow<Int> get() = _page

}