package com.keyflare.navigationResearch.core.navigation.jetpack

import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.keyflare.navigationResearch.core.navigation.common.INavigator

@Stable
class NavigatorImpl(private val navController: NavHostController) : INavigator {

    override fun navigate(
        screen: NavInfo<NoNavArgs>,
        singleTop: Boolean,
        clearBackstack: Boolean,
    ) {
        navigateInternal(
            route = screen.screenId,
            singleTop = singleTop,
            clearBackstack = clearBackstack,
        )
    }

    override fun <NavArgs> navigate(
        screen: NavInfo<NavArgs>,
        navArgs: NavArgs,
        singleTop: Boolean,
        clearBackstack: Boolean
    ) {
        val argsJson = navArgs.toSravniJson()
        navigateInternal(
            route = "${screen.screenId}/$argsJson",
            singleTop = singleTop,
            clearBackstack = clearBackstack,
        )
    }

    // TODO maybe annotation to OptIn it's usage
    override fun navigate(
        screenId: String,
        navArgsJson: String,
        singleTop: Boolean,
        clearBackstack: Boolean
    ) {
        navigateInternal(
            route = "$screenId/$navArgsJson",
            singleTop = singleTop,
            clearBackstack = clearBackstack,
        )
    }

    private fun navigateInternal(
        route: String,
        singleTop: Boolean,
        clearBackstack: Boolean,
    ) {
        navController.navigate(route = route) {
            if (clearBackstack) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
            launchSingleTop = singleTop
        }
    }

    override fun popBackStack() {
        navController.popBackStack()
    }
}

fun <T> T.toSravniJson() = Gson().toJson(this)
inline fun <reified T> String.restoreObject(): T? = Gson().fromJson(this, T::class.java)