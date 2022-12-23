package com.keyflare.navigationResearch.core.navigation.common

import com.keyflare.navigationResearch.core.navigation.jetpack.NavInfo
import com.keyflare.navigationResearch.core.navigation.jetpack.NoNavArgs

interface INavigator {

    fun <NavArgs> navigate(
        screen: NavInfo<NavArgs>,
        navArgs: NavArgs,
        singleTop: Boolean = false,
        clearBackstack: ClearBackstack = ClearBackstack.DoNotClear,
    )

    fun navigate(
        screen: NavInfo<NoNavArgs>,
        singleTop: Boolean = false,
        clearBackstack: ClearBackstack = ClearBackstack.DoNotClear,
    )

    fun navigate(
        screenId: String,
        navArgsJson: String,
        singleTop: Boolean = false,
        clearBackstack: ClearBackstack = ClearBackstack.DoNotClear,
    )

    fun popBackStack()

    sealed interface ClearBackstack {
        object DoNotClear : ClearBackstack
        object All : ClearBackstack
        data class Until(val screenId: String, val inclusive: Boolean) : ClearBackstack
    }
}
