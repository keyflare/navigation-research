package com.keyflare.navigationResearch.core.stub

import androidx.lifecycle.viewModelScope
import com.keyflare.navigationResearch.core.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DataLoadingScreenState(
    val loading: Boolean,
)

abstract class DataLoadingViewModelStub : BaseViewModel<DataLoadingScreenState, Unit>() {
    private val _state = MutableStateFlow(DataLoadingScreenState(loading = true))
    override val state: StateFlow<DataLoadingScreenState> = _state.asStateFlow()

    open fun goNextScreen() = Unit

    init {
        viewModelScope.launch {
            delay(1000)
            _state.update { it.copy(loading = false) }
        }
    }
}
