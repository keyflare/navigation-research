package com.keyflare.navigationResearch.core.navigation.jetpack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.keyflare.navigationResearch.core.base.BaseViewModel
import com.keyflare.navigationResearch.core.navigation.common.INavigator

@Composable
inline fun <Args, reified VM : BaseViewModel<*, Args>> injectViewModel(
    navigator: INavigator,
    args: Args? = null,
): VM {
    val viewModel = hiltViewModel<VM>()
    viewModel.init(navigator, args)

    DisposableEffect(key1 = Unit) {
        onDispose { viewModel.onExitComposition() }
    }

    return viewModel
}
