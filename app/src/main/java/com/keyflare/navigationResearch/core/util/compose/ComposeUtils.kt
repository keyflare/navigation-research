package com.keyflare.navigationResearch.core.util.compose

import androidx.activity.addCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
inline fun UseBackPressedCallback(crossinline body: () -> Unit) {
    val callback = LocalOnBackPressedDispatcherOwner.current
        ?.onBackPressedDispatcher
        ?.addCallback { body() }

    DisposableEffect(key1 = Unit) {
        onDispose { callback?.remove() }
    }
}
