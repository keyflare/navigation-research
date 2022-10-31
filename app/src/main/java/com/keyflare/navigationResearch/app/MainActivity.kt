package com.keyflare.navigationResearch.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.keyflare.navigationResearch.core.navigation.jetpack.InitNavigationJetpack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            if (useOdyssey) {
                // TODO InitNavigationOdyssey()
            } else {
                InitNavigationJetpack()
            }
        }
    }
}
