package com.keyflare.navigationResearch.core.navigation.jetpack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.keyflare.navigationResearch.core.base.BaseViewModel

@Immutable
object NoNavArgs

@Suppress("unused")
class NavInfo<NavArgs>(val screenId: String)

inline fun <reified NavArgs, NavAction, reified VM : BaseViewModel<NavArgs, NavAction>>
        NavGraphBuilder.screen(
    navAction: NavAction,
    screen: NavInfo<NavArgs>,
    crossinline composable: @Composable (VM) -> Unit,
) {
    val navArgsKey = "args"

    composable("${screen.screenId}/{$navArgsKey}") {
        val args = (it.arguments?.getString(navArgsKey) ?: "").restoreObject<NavArgs>()
        val viewModel = hiltViewModel<VM>()
        viewModel.init(args, navAction)

        DisposableEffect(key1 = Unit) {
            onDispose { viewModel.onExitComposition() }
        }

        composable(viewModel)
    }
}

inline fun <NavAction, reified VM : BaseViewModel<NoNavArgs, NavAction>> NavGraphBuilder.screenNoArgs(
    navAction: NavAction,
    screen: NavInfo<NoNavArgs>,
    crossinline composable: @Composable (VM) -> Unit,
) {
    composable(screen.screenId) {
        val viewModel = hiltViewModel<VM>()
        viewModel.init(NoNavArgs, navAction)

        DisposableEffect(key1 = Unit) {
            onDispose { viewModel.onExitComposition() }
        }

        composable(viewModel)
    }
}

@Composable
inline fun <reified NavArgs, NavAction, reified VM : BaseViewModel<NavArgs, NavAction>> injectViewModelJetpack(
    navAction: NavAction,
    args: String? = null,
): VM {
    val navArgs = args?.restoreObject<NavArgs>()
    val viewModel = hiltViewModel<VM>()
    viewModel.init(navArgs, navAction)

    DisposableEffect(key1 = Unit) {
        onDispose { viewModel.onExitComposition() }
    }

    return viewModel
}
