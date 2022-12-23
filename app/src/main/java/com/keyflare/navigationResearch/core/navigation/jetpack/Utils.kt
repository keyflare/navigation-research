package com.keyflare.navigationResearch.core.navigation.jetpack

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.keyflare.navigationResearch.core.base.BaseViewModel

@Immutable
object NoNavArgs

const val NAV_ARGS_KEY = "args"

@Suppress("unused")
class NavInfo<NavArgs>(val screenId: String) {
    val route get() = createRoute(screenId)
}

internal fun createRoute(screenId: String): String {
    return "${screenId}/{$NAV_ARGS_KEY}"
}

inline fun <reified NavArgs, Navigator, reified VM : BaseViewModel<NavArgs, Navigator>> NavGraphBuilder.screen(
    navigator: Navigator,
    screen: NavInfo<NavArgs>,
    crossinline composable: @Composable (VM) -> Unit,
) {
    composable(route = screen.route) {
        val args = if (NoNavArgs is NavArgs) {
            NoNavArgs
        } else {
            (it.arguments?.getString(NAV_ARGS_KEY) ?: "").restoreObject<NavArgs>()
        }

        val viewModel = hiltViewModel<VM>()
        viewModel.init(args, navigator)

        DisposableEffect(key1 = Unit) {
            onDispose { viewModel.onExitComposition() }
        }

        composable(viewModel)
    }
}

inline fun <Navigator, reified VM : BaseViewModel<NoNavArgs, Navigator>> NavGraphBuilder.screenNoArgs(
    navigator: Navigator,
    screen: NavInfo<NoNavArgs>,
    crossinline composable: @Composable (VM) -> Unit,
) {
    screen(navigator, screen, composable)
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
