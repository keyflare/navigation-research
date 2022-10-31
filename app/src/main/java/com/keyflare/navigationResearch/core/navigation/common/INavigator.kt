package com.keyflare.navigationResearch.core.navigation.common

interface INavigator {
    fun navigate(
        route: String,
        singleTop: Boolean = false,
        clearBackstack: Boolean = false,
    )
    fun popBackStack()
}
