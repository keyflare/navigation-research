package com.keyflare.navigationResearch.core.stub

import androidx.lifecycle.viewModelScope
import com.keyflare.navigationResearch.core.base.BaseViewModel
import com.keyflare.navigationResearch.core.navigation.common.INavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DataLoadingScreenState(
    val loading: Boolean,
    val error: Boolean,
)

abstract class DataLoadingViewModelStub<NavArgs> : BaseViewModel<NavArgs, INavigator>() {
    protected val _state = MutableStateFlow(DataLoadingScreenState(loading = true, error = false))
    val state: StateFlow<DataLoadingScreenState> by lazy { _state.asStateFlow() }

    open fun goNextScreen() = Unit

    protected open fun onLoaded() {
        _state.update { it.copy(loading = false) }
    }

    fun onBack() {
        navigator?.popBackStack()
    }

    init {
        viewModelScope.launch {
            delay(1000)
            onLoaded()
        }
    }
}
