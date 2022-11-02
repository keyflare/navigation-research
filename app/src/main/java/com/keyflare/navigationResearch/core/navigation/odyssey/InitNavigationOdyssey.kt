package com.keyflare.navigationResearch.core.navigation.odyssey

import androidx.activity.ComponentActivity
import com.keyflare.navigationResearch.core.navigation.common.RootDestinations
import com.keyflare.navigationResearch.features.bottomNavGraph
import com.keyflare.navigationResearch.features.firstFeatureGraph
import com.keyflare.navigationResearch.features.splashGraph
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder
import ru.alexgladkov.odyssey.compose.setupNavigation

fun ComponentActivity.initOdysseyNavigation() {
    // tried hilt integration, didn't work
//    val rootController = RootComposeBuilder().apply { buildMainGraph() }.build()
//    rootController.setupWithActivity(this)
//
//    val storeOwnerManager = HiltViewModelStoreOwnerManager(
//        context = this,
//        rootController = rootController
//    )
//
//    setContent {
//        CompositionLocalProvider(
//            LocalHiltViewModelStoreOwnerManager provides storeOwnerManager,
//            LocalRootController provides rootController
//        ) {
//            Navigator(startScreen = RootDestinations.splash)
//        }
//    }

    setupNavigation(
        startScreen = RootDestinations.splash,
        navigationGraph = { buildMainGraph() },
    )
}

fun RootComposeBuilder.buildMainGraph() {
    splashGraph(name = RootDestinations.splash)
    bottomNavGraph(name = RootDestinations.bottomNav)
    firstFeatureGraph(name = RootDestinations.feature1)
}
