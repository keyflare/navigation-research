package com.keyflare.navigationResearch.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.keyflare.navigationResearch.core.navigation.jetpack.InitNavigationJetpack
import com.keyflare.navigationResearch.core.navigation.odyssey.initOdysseyNavigation
import dagger.hilt.android.AndroidEntryPoint

const val useOdyssey = false

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (useOdyssey) {
            initOdysseyNavigation()
        } else {
            setContent {
                InitNavigationJetpack()
            }
        }
    }
}
