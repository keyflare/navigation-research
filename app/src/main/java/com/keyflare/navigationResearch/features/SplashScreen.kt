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
import com.keyflare.navigationResearch.core.base.BaseViewModel
import com.keyflare.navigationResearch.core.navigation.common.INavigator
import com.keyflare.navigationResearch.core.navigation.jetpack.NavInfo
import com.keyflare.navigationResearch.core.navigation.jetpack.NoNavArgs
import com.keyflare.navigationResearch.core.navigation.jetpack.screenNoArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

///////// API /////////

object SplashScreenDestinations {
    val splash = NavInfo<NoNavArgs>("splash")
}

fun NavGraphBuilder.splashGraph(navigator: INavigator) {
    screenNoArgs<INavigator, SplashViewModel>(
        navAction = navigator,
        screen = SplashScreenDestinations.splash,
        composable = { SplashScreen(it) }
    )
}

///////// INTERNAL /////////

@HiltViewModel
private class SplashViewModel @Inject constructor() : BaseViewModel<NoNavArgs, INavigator>() {
    fun startLoading() {
        viewModelScope.launch {
            delay(1000)
            navigator?.navigate(
                screen = BottomNavDestinations.bottomNav,
                navArgs = BottomNavArgs(tabToOpen = BottomNavScreenState.Tab.TAB1),
                clearBackstack = true,
            )
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
