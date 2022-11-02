package com.keyflare.navigationResearch.features

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.keyflare.navigationResearch.core.base.BaseViewModel
import com.keyflare.navigationResearch.core.navigation.common.RootDestinations
import com.keyflare.navigationResearch.core.navigation.common.INavigator
import com.keyflare.navigationResearch.core.navigation.jetpack.injectViewModelJetpack
import com.keyflare.navigationResearch.core.navigation.odyssey.injectViewModelOdyssey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder
import javax.inject.Inject

///////// API /////////

fun NavGraphBuilder.splashGraph(route: String, navigator: INavigator) {
    composable(route = route) {
        SplashScreen(viewModel = injectViewModelJetpack(navigator = navigator))
    }
}

fun RootComposeBuilder.splashGraph(name: String) {
    screen(name = name) {
        SplashScreen(viewModel = injectViewModelOdyssey())
    }
}

///////// INTERNAL /////////

@HiltViewModel
private class SplashViewModel @Inject constructor() : BaseViewModel<Unit, Unit>() {
    override val state: StateFlow<Unit> = MutableStateFlow(Unit)

    fun startLoading() {
        viewModelScope.launch {
            delay(1000)
            navigator?.navigate(RootDestinations.bottomNav, clearBackstack = true)
        }
    }
}

@Composable
private fun SplashScreen(viewModel: SplashViewModel) {
    LaunchedEffect(key1 = Unit) {
        viewModel.startLoading()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "Splash",
            style = MaterialTheme.typography.h1
        )
    }
}
