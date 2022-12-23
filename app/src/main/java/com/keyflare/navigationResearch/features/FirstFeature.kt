package com.keyflare.navigationResearch.features

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.keyflare.navigationResearch.core.navigation.common.INavigator
import com.keyflare.navigationResearch.core.navigation.jetpack.NavInfo
import com.keyflare.navigationResearch.core.navigation.jetpack.NoNavArgs
import com.keyflare.navigationResearch.core.navigation.jetpack.screen
import com.keyflare.navigationResearch.core.navigation.jetpack.screenNoArgs
import com.keyflare.navigationResearch.core.stub.DataLoadingScreenStub
import com.keyflare.navigationResearch.core.stub.DataLoadingViewModelStub
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

///////// API /////////

object FirstFeatureDestinations {
    val firstFeature = "firstFeature"
    val screenA = NavInfo<NoNavArgs>(screenId = "screenA")
    val screenB = NavInfo<FirstFeatureArgs>(screenId = "screenB")
    val screenC = NavInfo<FirstFeatureArgs>(screenId = "screenC")
}

fun NavGraphBuilder.firstFeatureGraph(navigator: INavigator) {
    navigation(
        startDestination = FirstFeatureDestinations.screenA.screenId,
        route = FirstFeatureDestinations.firstFeature,
    ) {
        screenNoArgs<INavigator, FirstFeatureAScreenViewModel>(
            navigator = navigator,
            screen = FirstFeatureDestinations.screenA,
            composable = { FirstFeatureAScreen(it) }
        )
        screen<FirstFeatureArgs, INavigator, FirstFeatureBScreenViewModel>(
            navigator = navigator,
            screen = FirstFeatureDestinations.screenB,
            composable = { FirstFeatureBScreen(it) }
        )
        screen<FirstFeatureArgs, INavigator, FirstFeatureCScreenViewModel>(
            navigator = navigator,
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

private open class FirstFeatureScreenBaseViewModel<Args>(
    private val nextScreen: NavInfo<FirstFeatureArgs>?
) : DataLoadingViewModelStub<Args>() {

    val featureState = MutableStateFlow(FirstFeatureState("", ""))

    override fun onLoaded() {
        if (args == null) {
            _state.update { it.copy(loading = false, error = true) }
        } else {
            _state.update { it.copy(loading = false) }
            requireNotNull(args).let { a ->
                if (a is FirstFeatureArgs) {
                    featureState.update {
                        it.copy(
                            title = a.title,
                            subtitle = a.subtitle,
                        )
                    }
                }
            }
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

    data class FirstFeatureState(
        val title: String,
        val subtitle: String,
    )
}

@HiltViewModel
private class FirstFeatureAScreenViewModel @Inject constructor() :
    FirstFeatureScreenBaseViewModel<NoNavArgs>(FirstFeatureDestinations.screenB)

@HiltViewModel
private class FirstFeatureBScreenViewModel @Inject constructor() :
    FirstFeatureScreenBaseViewModel<FirstFeatureArgs>(FirstFeatureDestinations.screenC)

@HiltViewModel
private class FirstFeatureCScreenViewModel @Inject constructor() :
    FirstFeatureScreenBaseViewModel<FirstFeatureArgs>(null) {

    override fun goNextScreen() {
        navigator?.navigate(
            screen = FirstFeatureDestinations.screenC,
            navArgs = FirstFeatureArgs(
                title = "from ${javaClass.simpleName}",
                subtitle = "test",
            ),
//            singleTop = true,
            clearBackstack = INavigator.ClearBackstack.Until(
                screenId = FirstFeatureDestinations.screenB.screenId,
                inclusive = true,
            )
        )
    }
}

@Composable
private fun BaseScreen(
    viewModel: FirstFeatureScreenBaseViewModel<*>,
    label: String,
) {
    val state by viewModel.state.collectAsState()
    val featureState by viewModel.featureState.collectAsState()

    DataLoadingScreenStub(
        color = Color.White,
        label = label,
        isLoading = state.loading,
        error = state.error,
        onBack = viewModel::onBack,
        onForward = viewModel::goNextScreen,
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(text = featureState.title, fontSize = 24.sp)
        Text(text = featureState.subtitle, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun FirstFeatureAScreen(viewModel: FirstFeatureAScreenViewModel) {
    BaseScreen(viewModel = viewModel, label = "A")
}

@Composable
private fun FirstFeatureBScreen(viewModel: FirstFeatureBScreenViewModel) {
    BaseScreen(viewModel = viewModel, label = "B")
}

@Composable
private fun FirstFeatureCScreen(viewModel: FirstFeatureCScreenViewModel) {
    BaseScreen(viewModel = viewModel, label = "C")
}
