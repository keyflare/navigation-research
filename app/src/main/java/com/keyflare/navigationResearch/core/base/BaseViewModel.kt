package com.keyflare.navigationResearch.core.base

import androidx.annotation.CallSuper
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel

@Stable
abstract class BaseViewModel<NavArgs, NavAction> : ViewModel() {

    protected var args: NavArgs? = null
        private set

    protected var navigator: NavAction? = null
        private set

    protected open fun onNewArgs(args: NavArgs?) = Unit

    fun init(args: NavArgs?, navigator: NavAction): BaseViewModel<NavArgs, NavAction> {
        this.navigator = navigator
        handleArgs(args)
        return this
    }

    @CallSuper
    fun onExitComposition() {
        navigator = null
    }

    private fun handleArgs(args: NavArgs?) {
        if (this.args != args) {
            this.args = args
            onNewArgs(args)
        }
    }
}
