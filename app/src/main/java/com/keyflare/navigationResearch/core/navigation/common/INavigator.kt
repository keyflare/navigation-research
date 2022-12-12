package com.keyflare.navigationResearch.core.navigation.common

import com.keyflare.navigationResearch.core.navigation.jetpack.NavInfo
import com.keyflare.navigationResearch.core.navigation.jetpack.NoNavArgs

interface INavigator {

    fun <NavArgs> navigate(
        screen: NavInfo<NavArgs>,
        navArgs: NavArgs,
        singleTop: Boolean = false,
        clearBackstack: Boolean = false,
    )

    fun navigate(
        screen: NavInfo<NoNavArgs>,
        singleTop: Boolean = false,
        clearBackstack: Boolean = false,
    )

    fun navigate(
        screenId: String,
        navArgsJson: String,
        singleTop: Boolean = false,
        clearBackstack: Boolean = false,
    )

    fun popBackStack()
}
