package com.keyflare.navigationResearch.core.navigation.jetpack

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.keyflare.navigationResearch.core.navigation.common.INavigator
import com.keyflare.navigationResearch.features.*

@Composable
fun InitNavigationJetpack() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SplashScreenDestinations.splash.route,
    ) {
        buildMainGraph(NavigatorImpl(navController))
    }
}

private fun NavGraphBuilder.buildMainGraph(navigator: INavigator) {
    splashGraph(navigator)
    bottomNavGraph(navigator)
    firstFeatureGraph(navigator)
}
