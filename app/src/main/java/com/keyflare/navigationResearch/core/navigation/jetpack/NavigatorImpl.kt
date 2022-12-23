package com.keyflare.navigationResearch.core.navigation.jetpack

import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.keyflare.navigationResearch.core.navigation.common.INavigator
import com.keyflare.navigationResearch.core.navigation.common.INavigator.ClearBackstack

@Stable
class NavigatorImpl(private val navController: NavHostController) : INavigator {

    override fun navigate(
        screen: NavInfo<NoNavArgs>,
        singleTop: Boolean,
        clearBackstack: ClearBackstack,
    ) {
        navigate(
            screen = screen,
            navArgs = NoNavArgs,
            singleTop = singleTop,
            clearBackstack = clearBackstack
        )
    }

    override fun <NavArgs> navigate(
        screen: NavInfo<NavArgs>,
        navArgs: NavArgs,
        singleTop: Boolean,
        clearBackstack: ClearBackstack,
    ) {
        val argsJson = navArgs.toSravniJson()
        navigateInternal(
            route = "${screen.screenId}/$argsJson",
            singleTop = singleTop,
            clearBackstack = clearBackstack,
        )
    }

    override fun navigate(
        screenId: String,
        navArgsJson: String,
        singleTop: Boolean,
        clearBackstack: ClearBackstack,
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
        clearBackstack: ClearBackstack,
    ) {
        navController.navigate(route = route) {
            when (clearBackstack) {
                is ClearBackstack.DoNotClear -> Unit
                is ClearBackstack.All -> {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
                is ClearBackstack.Until -> {
                    popUpTo(route = createRoute(clearBackstack.screenId)) {
                        inclusive = clearBackstack.inclusive
                    }
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