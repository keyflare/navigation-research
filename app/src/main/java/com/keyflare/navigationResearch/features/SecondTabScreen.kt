package com.keyflare.navigationResearch.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.keyflare.navigationResearch.core.navigation.common.INavigator
import com.keyflare.navigationResearch.core.navigation.jetpack.CoreDestinations
import com.keyflare.navigationResearch.core.navigation.jetpack.injectViewModel
import com.keyflare.navigationResearch.core.stub.DataLoadingScreenStub
import com.keyflare.navigationResearch.core.stub.DataLoadingViewModelStub
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

///////// API /////////

fun NavGraphBuilder.secondTabGraph(route: String, navigator: INavigator) {
    composable(route = route) {
        SecondTabScreen(viewModel = injectViewModel(navigator))
    }
}

///////// INTERNAL /////////

@HiltViewModel
private class SecondTabScreenViewModel @Inject constructor() : DataLoadingViewModelStub() {
    override fun goNextScreen() {
        navigator?.navigate(CoreDestinations.splash)
    }
}

@Composable
private fun SecondTabScreen(viewModel: SecondTabScreenViewModel) {
    val state by viewModel.state.collectAsState()

    DataLoadingScreenStub(
        color = Color.Red,
        label = "Second tab",
        isLoading = state.loading,
        onBack = viewModel::onBack,
        onForward = viewModel::goNextScreen,
    )
}
