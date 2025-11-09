package com.tewelde.rijksmuseum.core.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.tewelde.rijksmuseum.core.common.di.CommonComponent
import com.tewelde.rijksmuseum.core.common.di.ComponentHolder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

data class SnackbarMessage(
    val message: String,
    val actionLabel: String? = null,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val onAction: (() -> Unit) = {}
)

@Inject
@SingleIn(AppScope::class)
class SnackBarState {

    val snackbarHostState: SnackbarHostState = SnackbarHostState()

    private val _messages = MutableSharedFlow<SnackbarMessage>()
    val messages = _messages.asSharedFlow()

    suspend fun showSnackbar(
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Short,
        actionLabel: String? = null,
        onAction: (() -> Unit) = {}
    ) {
        _messages.emit(
            SnackbarMessage(
                message = message,
                actionLabel = actionLabel,
                duration = duration,
                onAction = onAction
            )
        )
    }
}

@Composable
fun SnackBarStateEffect() {
    val snackBarState = remember { ComponentHolder.component<CommonComponent>().snackBarState }
    LaunchedEffect(
        snackBarState
    ) {
        snackBarState.messages.collect { message ->
            with(message) {
                snackBarState.snackbarHostState.currentSnackbarData?.dismiss()
                snackBarState.snackbarHostState.showSnackbar(
                    message = message.message,
                    actionLabel = actionLabel,
                    duration = duration
                ).let { result ->
                    if (result == SnackbarResult.ActionPerformed) {
                        onAction()
                    }
                }
            }
        }
    }
}