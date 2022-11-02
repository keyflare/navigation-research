package com.keyflare.navigationResearch.core.navigation.odyssey

import com.keyflare.navigationResearch.core.navigation.common.INavigator
import ru.alexgladkov.odyssey.compose.RootController
import ru.alexgladkov.odyssey.core.LaunchFlag
import ru.alexgladkov.odyssey.core.animations.AnimationType

class OdysseyNavigator(private val controller: RootController) : INavigator {

    override fun navigate(
        screen: String,
        singleTop: Boolean,
        clearBackstack: Boolean,
        isRootDestination: Boolean,
    ) {
        val rootController = if (isRootDestination) controller.parentRootController else controller

        rootController?.launch(
            screen = screen,
            launchFlag = if (clearBackstack) LaunchFlag.SingleNewTask else null,
            animationType = AnimationType.Fade(500)
        )
    }

    override fun popBackStack() {
        controller.popBackStack()
    }

}