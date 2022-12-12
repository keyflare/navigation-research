package com.keyflare.navigationResearch.core.navigation.common

import com.keyflare.navigationResearch.core.navigation.jetpack.NavInfo
import com.keyflare.navigationResearch.core.navigation.jetpack.NoNavArgs
import com.keyflare.navigationResearch.features.BottomNavArgs

object RootDestinations {
    val splash = NavInfo<NoNavArgs>(screenId = "splash")
    val bottomNav = NavInfo<BottomNavArgs>(screenId = "bottomNav")
//    val feature1 = NavInfo<NoNavArgs>(screenName = "splash")

//    const val splash = "splash"
//    const val bottomNav = "main"
    const val feature1 = "feature1"
}