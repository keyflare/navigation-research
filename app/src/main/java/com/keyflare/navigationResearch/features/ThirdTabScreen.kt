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
import com.keyflare.navigationResearch.core.stub.DataLoadingScreenStub
import com.keyflare.navigationResearch.core.stub.DataLoadingViewModelStub
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

///////// API /////////

fun NavGraphBuilder.thirdTabGraph(route: String, navigator: INavigator) {
    composable(route = route) {
        ThirdTabScreen(viewModel = injectViewModelJetpack(navigator))
    }
}

///////// INTERNAL /////////

@HiltViewModel
private class ThirdTabScreenViewModel @Inject constructor() : DataLoadingViewModelStub<Unit>() {
    override fun goNextScreen() {
        navigator?.navigate(RootDestinations.splash)
    }
}

@Composable
private fun ThirdTabScreen(viewModel: ThirdTabScreenViewModel) {
    val state by viewModel.state.collectAsState()

    DataLoadingScreenStub(
        color = Color.Red,
        label = "Third tab",
        isLoading = state.loading,
        onBack = viewModel::onBack,
        error = false,
        onForward = viewModel::goNextScreen,
    )
}
