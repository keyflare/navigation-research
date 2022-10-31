package com.keyflare.navigationResearch.core.navigation.jetpack

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.keyflare.navigationResearch.core.navigation.common.INavigator
import com.keyflare.navigationResearch.features.*

object CoreDestinations {
    const val splash = "splash"
    const val bottomNav = "main"
    const val feature1 = "feature1"
    const val feature2 = "feature2"
    const val feature3 = "feature3"
}

@Composable
fun InitNavigationJetpack() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CoreDestinations.splash,
    ) {
        buildMainGraph(JetpackNavigator(navController))
    }
}

private fun NavGraphBuilder.buildMainGraph(navigator: INavigator) {
    splashGraph(route = CoreDestinations.splash, navigator)
    bottomNavGraph(route = CoreDestinations.bottomNav, navigator)
    firstFeatureGraph(route = CoreDestinations.feature1, navigator)
}
