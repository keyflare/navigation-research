package com.keyflare.navigationResearch.features

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.keyflare.navigationResearch.core.base.BaseViewModel
import com.keyflare.navigationResearch.core.navigation.common.INavigator
import com.keyflare.navigationResearch.core.navigation.jetpack.NavInfo
import com.keyflare.navigationResearch.core.navigation.jetpack.screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/////////// API ////////////

object BottomNavDestinations {
    val bottomNav = NavInfo<BottomNavArgs>(screenId = "bottomNav")
}

data class BottomNavArgs(val tabToOpen: BottomNavScreenState.Tab)

fun NavGraphBuilder.bottomNavGraph(navigator: INavigator) {
    screen<BottomNavArgs, Unit, BottomNavScreenViewModel>(
        navAction = Unit,
        screen = BottomNavDestinations.bottomNav,
        composable = { BottomNavScreen(it, navigator) }
    )
}

/////////// INTERNAL ////////////

object BottomNavDestinationsInternal {
    const val tab1 = "tab1"
    const val tab2 = "tab2"
    const val tab3 = "tab3"
}

data class BottomNavScreenState(val selectedTab: Tab) {
    enum class Tab(val route: String) {
        TAB1(BottomNavDestinationsInternal.tab1),
        TAB2(BottomNavDestinationsInternal.tab2),
        TAB3(BottomNavDestinationsInternal.tab3);
    }
}

@HiltViewModel
class BottomNavScreenViewModel @Inject constructor() :
    BaseViewModel<BottomNavArgs, Unit>() {
    private var _state = MutableStateFlow(
        BottomNavScreenState(selectedTab = BottomNavScreenState.Tab.TAB1)
    )
    val state: StateFlow<BottomNavScreenState> = _state.asStateFlow()

    fun onTabSelected(tab: BottomNavScreenState.Tab) {
        _state.update { it.copy(selectedTab = tab) }
    }
}

@Composable
fun BottomNavScreen(
    viewModel: BottomNavScreenViewModel,
    navigator: INavigator,
) {
    val navController = rememberNavController()
    val state by viewModel.state.collectAsState()

    val items = listOf(
        BottomNavScreenState.Tab.TAB1,
        BottomNavScreenState.Tab.TAB2,
        BottomNavScreenState.Tab.TAB3,
    )

    LaunchedEffect(key1 = state.selectedTab) {
        navController.navigate(route = state.selectedTab.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color.Gray,
                contentColor = Color.Black
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null
                            )
                        },
                        label = { Text(text = item.name) },
                        alwaysShowLabel = true,
                        selected = item.route == currentRoute,
                        onClick = { viewModel.onTabSelected(item) }
                    )
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = BottomNavDestinationsInternal.tab1,
            modifier = Modifier.padding(it)
        ) {
            firstTabGraph(route = BottomNavDestinationsInternal.tab1, navigator)
            secondTabGraph(route = BottomNavDestinationsInternal.tab2, navigator)
            thirdTabGraph(route = BottomNavDestinationsInternal.tab3, navigator)
        }
    }
}
