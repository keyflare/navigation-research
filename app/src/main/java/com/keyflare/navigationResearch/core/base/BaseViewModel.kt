package com.keyflare.navigationResearch.core.base

import androidx.annotation.CallSuper
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.keyflare.navigationResearch.core.navigation.common.INavigator
import kotlinx.coroutines.flow.StateFlow

@Stable
abstract class BaseViewModel<State, Args> : ViewModel() {
    abstract val state: StateFlow<State>

    protected var args: Args? = null
        private set

    protected var navigator: INavigator? = null
        private set

    // TODO check if leaks possible
    fun init(navigator: INavigator, args: Args?): BaseViewModel<State, Args> {
        this.navigator = navigator
        args?.let { handleArgs(it) }
        return this
    }

    @CallSuper
    open fun onExitComposition() {
        navigator = null
    }

    open fun onBack() {
        navigator?.popBackStack()
    }

    protected open fun onNewArgs() = Unit

    private fun handleArgs(args: Args) {
        if (this.args == null || this.args != args) {
            this.args = args
            onNewArgs()
        }
    }
}
