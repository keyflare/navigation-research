package com.keyflare.navigationResearch.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.keyflare.navigationResearch.core.navigation.common.INavigator
import com.keyflare.navigationResearch.core.navigation.jetpack.NavInfo
import com.keyflare.navigationResearch.core.navigation.jetpack.screen
import com.keyflare.navigationResearch.core.stub.DataLoadingScreenStub
import com.keyflare.navigationResearch.core.stub.DataLoadingViewModelStub
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

///////// API /////////

object FirstFeatureDestinations {
    val firstFeature = "firstFeature"
    val screenA = NavInfo<FirstFeatureArgs>(screenId = "screenA")
    val screenB = NavInfo<FirstFeatureArgs>(screenId = "screenB")
    val screenC = NavInfo<FirstFeatureArgs>(screenId = "screenC")
}

fun NavGraphBuilder.firstFeatureGraph(navigator: INavigator) {
    navigation(
        startDestination = FirstFeatureDestinations.screenA.screenId,
        route = FirstFeatureDestinations.firstFeature,
    ) {
        screen<FirstFeatureArgs, INavigator, FirstFeatureAScreenViewModel>(
            navAction = navigator,
            screen = FirstFeatureDestinations.screenA,
            composable = { FirstFeatureAScreen(it) }
        )
        screen<FirstFeatureArgs, INavigator, FirstFeatureBScreenViewModel>(
            navAction = navigator,
            screen = FirstFeatureDestinations.screenB,
            composable = { FirstFeatureBScreen(it) }
        )
        screen<FirstFeatureArgs, INavigator, FirstFeatureCScreenViewModel>(
            navAction = navigator,
            screen = FirstFeatureDestinations.screenC,
            composable = { FirstFeatureCScreen(it) },
        )
    }
}

///////// INTERNAL /////////

data class FirstFeatureArgs(
    val title: String,
    val subtitle: String,
)

private open class FirstFeatureScreenBaseViewModel(
    private val nextScreen: NavInfo<FirstFeatureArgs>?
) : DataLoadingViewModelStub<FirstFeatureArgs>() {

    override fun onLoaded() {
        if (args == null) {
            _state.update { it.copy(loading = false, error = true) }
        } else {
            _state.update { it.copy(loading = false) }
        }
    }

    override fun goNextScreen() {
        nextScreen?.let {
            navigator?.navigate(
                nextScreen,
                FirstFeatureArgs(
                    title = "from ${javaClass.simpleName}",
                    subtitle = "test",
                )
            )
        }
    }
}

@HiltViewModel
private class FirstFeatureAScreenViewModel @Inject constructor() :
    FirstFeatureScreenBaseViewModel(FirstFeatureDestinations.screenB)

@HiltViewModel
private class FirstFeatureBScreenViewModel @Inject constructor() :
    FirstFeatureScreenBaseViewModel(FirstFeatureDestinations.screenC)

@HiltViewModel
private class FirstFeatureCScreenViewModel @Inject constructor() :
    FirstFeatureScreenBaseViewModel(null)

@Composable
private fun FirstFeatureAScreen(viewModel: FirstFeatureAScreenViewModel) {
    val state by viewModel.state.collectAsState()

    DataLoadingScreenStub(
        color = Color.White,
        label = "A",
        isLoading = state.loading,
        error = state.error,
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
        error = state.error,
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
        error = state.error,
        onBack = viewModel::onBack,
    )
}
