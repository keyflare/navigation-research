package com.keyflare.navigationResearch.core.navigation.odyssey

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory
import ru.alexgladkov.odyssey.compose.RenderWithParams
import ru.alexgladkov.odyssey.compose.RootController
import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.helpers.FlowBuilder
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder

/*
 * Hilt integration proposed by Odyssey community (turned out it's a trash).
 * https://github.com/AlexGladkov/Odyssey/pull/47
 */

fun RootComposeBuilder.hiltScreen(
    name: String,
    content: RenderWithParams<Any?>
) {
    screen(
        name = name,
        content = {
            WrapHiltViewModelNavigation(name) {
                content(it)
            }
        }
    )
}

fun FlowBuilder.hiltScreen(name: String, content: RenderWithParams<Any?>) {
    screen(
        name = name,
        content = {
            WrapHiltViewModelNavigation(name) {
                content(it)
            }
        }
    )
}

@Composable
private fun WrapHiltViewModelNavigation(
    key: String,
    content: @Composable () -> Unit
) {
    val hiltViewModelStoreOwnerManager = LocalHiltViewModelStoreOwnerManager.current
    val owner = remember {
        hiltViewModelStoreOwnerManager.getViewModelStoreOwnerByKey(key)
    }
    DisposableEffect(Unit) {
        onDispose {
            hiltViewModelStoreOwnerManager.clearViewModelStoreOwnerByKey(key)
        }
    }
    CompositionLocalProvider(LocalViewModelStoreOwner provides owner) {
        content()
    }
}

val LocalHiltViewModelStoreOwnerManager = compositionLocalOf<HiltViewModelStoreOwnerManager> {
    error("No HiltViewModelStoreOwnerManager")
}

class HiltViewModelStoreOwnerManager(
    private val context: Context,
    private val rootController: RootController
) {
    private val owners = HashMap<String, ViewModelStoreOwner>()
    private val ownersLevels = HashMap<String, Int>()

    init {
        rootController.onScreenRemove = {
            // we can't delete owner here, the screen is still alive
            it.realKey?.let { key ->
                ownersLevels[key]?.let {
                    // mark for deletion
                    ownersLevels[key] = Int.MAX_VALUE
                }
            }
        }
    }

    fun getViewModelStoreOwnerByKey(key: String): ViewModelStoreOwner {
        val currentLevel = rootController.measureLevel()
        ownersLevels[key] = currentLevel
        return owners[key] ?: SimpleViewModelStoreOwner(context).also {
            owners[key] = it
        }
    }

    fun clearViewModelStoreOwnerByKey(key: String) {
        val currentLevel = rootController.measureLevel()
        val ownerLevel = ownersLevels[key] ?: Int.MAX_VALUE
        if (currentLevel < ownerLevel) {
            owners[key]?.viewModelStore?.clear()
            owners.remove(key)
            ownersLevels.remove(key)
        }
    }

    fun dispose() {
        val keys = owners.keys.toList()
        keys.forEach { key ->
            owners[key]?.viewModelStore?.clear()
            owners.remove(key)
            ownersLevels.remove(key)
        }
    }
}

class SimpleViewModelStoreOwner(
    activityContext: Context
) : ViewModelStoreOwner,
    HasDefaultViewModelProviderFactory,
    SavedStateRegistryOwner,
    LifecycleOwner {

    private val activity = activityContext.let {
        var ctx = it
        while (ctx is ContextWrapper) {
            if (ctx is Activity) {
                return@let ctx
            }
            ctx = ctx.baseContext
        }
        throw IllegalStateException()
    }

    private val store = ViewModelStore()

    private val lifecycle = LifecycleRegistry(this).apply {
        this.currentState = Lifecycle.State.INITIALIZED
    }

    private val savedStateRegistryController = SavedStateRegistryController.create(this).apply {
        this.performRestore(null)
    }

    private val defaultFactory = SavedStateViewModelFactory(
        (activityContext.applicationContext as? Application),
        this,
        null
    )

    private val factory: ViewModelProvider.Factory = HiltViewModelFactory.createInternal(
        activity,
        this,
        null,
        defaultFactory
    )

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    override fun getViewModelStore(): ViewModelStore = store

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory = factory

    override fun getLifecycle(): Lifecycle = lifecycle

}