package com.keyflare.navigationResearch.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.keyflare.navigationResearch.core.navigation.common.INavigator
import com.keyflare.navigationResearch.core.navigation.jetpack.injectViewModelJetpack
import com.keyflare.navigationResearch.core.navigation.odyssey.injectViewModelOdyssey
import com.keyflare.navigationResearch.core.stub.DataLoadingScreenStub
import com.keyflare.navigationResearch.core.stub.DataLoadingViewModelStub
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.alexgladkov.odyssey.compose.extensions.flow
import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder
import javax.inject.Inject

///////// API /////////

object FirstFeatureDestinations {
    const val screenA = "screenA"
    const val screenB = "screenB"
    const val screenC = "screenC"
}

fun NavGraphBuilder.firstFeatureGraph(route: String, navigator: INavigator) {
    navigation(
        startDestination = FirstFeatureDestinations.screenA,
        route = route,
    ) {
        composable(route = FirstFeatureDestinations.screenA) {
            FirstFeatureAScreen(viewModel = injectViewModelJetpack(navigator))
        }
        composable(route = FirstFeatureDestinations.screenB) {
            FirstFeatureBScreen(viewModel = injectViewModelJetpack(navigator))
        }
        composable(route = FirstFeatureDestinations.screenC) {
            FirstFeatureCScreen(viewModel = injectViewModelJetpack(navigator))
        }
    }
}

fun RootComposeBuilder.firstFeatureGraph(name: String) {
    flow(name = name) {
        screen(name = FirstFeatureDestinations.screenA) {
            FirstFeatureAScreen(viewModel = injectViewModelOdyssey())
        }
        screen(name = FirstFeatureDestinations.screenB) {
            FirstFeatureBScreen(viewModel = injectViewModelOdyssey())
        }
        screen(name = FirstFeatureDestinations.screenC) {
            FirstFeatureCScreen(viewModel = injectViewModelOdyssey())
        }
    }
}

///////// INTERNAL /////////

@HiltViewModel
private class FirstFeatureAScreenViewModel @Inject constructor() : DataLoadingViewModelStub() {
    override fun goNextScreen() {
        navigator?.navigate(FirstFeatureDestinations.screenB)
    }
}

@HiltViewModel
private class FirstFeatureBScreenViewModel @Inject constructor() : DataLoadingViewModelStub() {
    override fun goNextScreen() {
        navigator?.navigate(FirstFeatureDestinations.screenC)
    }
}

@HiltViewModel
private class FirstFeatureCScreenViewModel @Inject constructor() : DataLoadingViewModelStub()

@Composable
private fun FirstFeatureAScreen(viewModel: FirstFeatureAScreenViewModel) {
    val state by viewModel.state.collectAsState()

    DataLoadingScreenStub(
        color = Color.White,
        label = "A",
        isLoading = state.loading,
        onBack = viewModel::onBack,
        onForward = viewModel::goNextScreen,
    )
}

@Composable
private fun FirstFeatureBScreen(viewModel: FirstFeatureBScreenViewModel) {
    val state by viewModel.state.collectAsState()

    DataLoadingScreenStub(
        color = Color.White,
        label = "B",
        isLoading = state.loading,
        onBack = viewModel::onBack,
        onForward = viewModel::goNextScreen,
    )
}

@Composable
private fun FirstFeatureCScreen(viewModel: FirstFeatureCScreenViewModel) {
    val state by viewModel.state.collectAsState()

    DataLoadingScreenStub(
        color = Color.White,
        label = "C",
        isLoading = state.loading,
        onBack = viewModel::onBack,
    )
}
