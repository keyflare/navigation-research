package com.keyflare.navigationResearch.core.navigation.jetpack

import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import com.keyflare.navigationResearch.core.navigation.common.INavigator

@Stable
class JetpackNavigator(private val navController: NavHostController) : INavigator {

    override fun navigate(
        route: String,
        singleTop: Boolean,
        clearBackstack: Boolean,
    ) {
        navController.navigate(route) {
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
