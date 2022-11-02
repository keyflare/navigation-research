package com.keyflare.navigationResearch.core.navigation.common

interface INavigator {
    fun navigate(
        screen: String,
        singleTop: Boolean = false,
        clearBackstack: Boolean = false,
        isRootDestination: Boolean = false,
    )
    fun popBackStack()
}
