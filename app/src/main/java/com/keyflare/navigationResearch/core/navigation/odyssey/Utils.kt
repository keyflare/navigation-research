package com.keyflare.navigationResearch.core.navigation.odyssey

import androidx.compose.runtime.Composable
import com.keyflare.navigationResearch.core.navigation.common.INavigator
import ru.alexgladkov.odyssey.compose.local.LocalRootController
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keyflare.navigationResearch.core.base.BaseViewModel

@Composable
fun findNavigator(): INavigator {
    return OdysseyNavigator(LocalRootController.current)
}

@Composable
inline fun <Args, reified VM : BaseViewModel<*, Args>> injectViewModelOdyssey(
    args: Args? = null,
): VM {
    val viewModel = viewModel<VM>()
    viewModel.init(findNavigator(), args)

    DisposableEffect(key1 = Unit) {
        onDispose { viewModel.onExitComposition() }
    }

    return viewModel
}
