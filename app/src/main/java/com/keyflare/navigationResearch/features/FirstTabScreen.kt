package com.keyflare.navigationResearch.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.keyflare.navigationResearch.core.navigation.common.RootDestinations
import com.keyflare.navigationResearch.core.navigation.common.INavigator
import com.keyflare.navigationResearch.core.navigation.jetpack.injectViewModelJetpack
import com.keyflare.navigationResearch.core.navigation.odyssey.injectViewModelOdyssey
import com.keyflare.navigationResearch.core.stub.DataLoadingScreenStub
import com.keyflare.navigationResearch.core.stub.DataLoadingViewModelStub
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.helpers.FlowBuilder
import javax.inject.Inject

///////// API /////////

fun NavGraphBuilder.firstTabGraph(route: String, navigator: INavigator) {
    composable(route = route) {
        FirstTabScreen(viewModel = injectViewModelJetpack(navigator))
    }
}

fun FlowBuilder.firstTabGraph(name: String) {
    screen(name = name) {
        FirstTabScreen(viewModel = injectViewModelOdyssey())
    }
}

///////// INTERNAL /////////

@HiltViewModel
 class FirstTabScreenViewModel @Inject constructor() : DataLoadingViewModelStub() {
    override fun goNextScreen() {
        navigator?.navigate(RootDestinations.feature1, isRootDestination = true)
    }
}

@Composable
 fun FirstTabScreen(viewModel: FirstTabScreenViewModel) {
    val state by viewModel.state.collectAsState()

    DataLoadingScreenStub(
        color = Color.Green,
        label = "First tab",
        isLoading = state.loading,
        onBack = viewModel::onBack,
        onForward = viewModel::goNextScreen,
    )
}
