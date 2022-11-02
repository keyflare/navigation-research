package com.keyflare.navigationResearch.features

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.keyflare.navigationResearch.core.base.BaseViewModel
import com.keyflare.navigationResearch.core.navigation.common.INavigator
import com.keyflare.navigationResearch.core.navigation.jetpack.injectViewModelJetpack
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.alexgladkov.odyssey.compose.extensions.bottomNavigation
import ru.alexgladkov.odyssey.compose.extensions.tab
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder
import ru.alexgladkov.odyssey.compose.navigation.bottom_bar_navigation.BottomNavConfiguration
import ru.alexgladkov.odyssey.compose.navigation.bottom_bar_navigation.TabConfiguration
import ru.alexgladkov.odyssey.compose.navigation.bottom_bar_navigation.TabItem
import ru.alexgladkov.odyssey.compose.navigation.bottom_bar_navigation.TabsNavModel
import javax.inject.Inject

/////////// API ////////////

object BottomNavDestinations {
    const val tab1 = "tab1"
    const val tab2 = "tab2"
    const val tab3 = "tab3"
}

fun NavGraphBuilder.bottomNavGraph(route: String, navigator: INavigator) {
    composable(route = route) {
        BottomNavScreen(viewModel = injectViewModelJetpack(navigator), navigator = navigator)
    }
}

fun RootComposeBuilder.bottomNavGraph(name: String) {
    bottomNavigation(
        name = name,
        tabsNavModel = BottomConfiguration(),
    ) {
        tab(BottomNavTab(BottomNavDestinations.tab1)) {
            firstTabGraph(name = BottomNavDestinations.tab1)
        }
        tab(BottomNavTab(BottomNavDestinations.tab2)) {
            secondTabGraph(name = BottomNavDestinations.tab2)
        }
        tab(BottomNavTab(BottomNavDestinations.tab3)) {
            thirdTabGraph(name = BottomNavDestinations.tab3)
        }
    }
}

/////////// INTERNAL ////////////

data class BottomNavScreenState(val selectedTab: Tab) {
    enum class Tab(val route: String) {
        TAB1(BottomNavDestinations.tab1),
        TAB2(BottomNavDestinations.tab2),
        TAB3(BottomNavDestinations.tab3);
    }
}

data class BottomNavArgs(val tabToOpen: BottomNavScreenState.Tab)

@HiltViewModel
class BottomNavScreenViewModel @Inject constructor() :
    BaseViewModel<BottomNavScreenState, BottomNavArgs>() {
    private var _state = MutableStateFlow(
        BottomNavScreenState(selectedTab = BottomNavScreenState.Tab.TAB1)
    )
    override val state: StateFlow<BottomNavScreenState> = _state.asStateFlow()

    fun onTabSelected(tab: BottomNavScreenState.Tab) {
        _state.update { it.copy(selectedTab = tab) }
    }
}

// jetpack navigation

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
            startDestination = BottomNavDestinations.tab1,
            modifier = Modifier.padding(it)
        ) {
            firstTabGraph(route = BottomNavDestinations.tab1, navigator)
            secondTabGraph(route = BottomNavDestinations.tab2, navigator)
            thirdTabGraph(route = BottomNavDestinations.tab3, navigator)
        }
    }
}

// odyssey

class BottomNavTab(private val title: String) : TabItem() {
    override val configuration: TabConfiguration
        @Composable get() = TabConfiguration(
            title = title,
            selectedIcon = rememberVectorPainter(image = Icons.Default.Star),
            unselectedIcon = rememberVectorPainter(image = Icons.Default.Star),
        )
}

class BottomConfiguration : TabsNavModel<BottomNavConfiguration>() {
    override val navConfiguration: BottomNavConfiguration
        @Composable
        get() {
            return BottomNavConfiguration(
                backgroundColor = MaterialTheme.colors.background,
                selectedColor = MaterialTheme.colors.primary,
                unselectedColor = MaterialTheme.colors.onSurface
            )
        }
}
