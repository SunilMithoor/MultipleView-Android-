package com.app.presentation.util

import androidx.navigation.NavDirections
import com.app.domain.model.ErrorType

/**
 * A simple sealed class to handle more properly
 * navigation from a [AndroidViewModel]
 */
sealed class NavigationCommand {
    data class ToDirection(val directions: NavDirections) : NavigationCommand()

    data class ToFailureCallback(
        val errorType: ErrorType?,
        val callback: () -> Unit
    ) : NavigationCommand()

//    data class ToPopup(
//        val popupUiModel: PopupUiModel,
//        val callbackPositive: (() -> Unit)? = null,
//        val callbackNegative: (() -> Unit)? = null
//    ) : NavigationCommand()

    data class ToDirectionActivity(
        val className: Class<*>,
        val flags: Int? = null,
        val isClearTask: Boolean = false
    ) : NavigationCommand()

    data class ToDeepLink(val deepLink: String) : NavigationCommand()
    data class LoadingDialog(val isShow: Boolean) : NavigationCommand()
    object Back : NavigationCommand()
}
