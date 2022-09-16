package com.app.presentation.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.app.domain.model.ErrorType
import com.app.presentation.util.Event
import com.app.presentation.util.NavigationCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    protected val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    protected val bgScope = CoroutineScope(Dispatchers.Default + viewModelJob)

    val baseLiveData = MutableLiveData<String>()

    val errorMessageLiveData = MutableLiveData<Int>()

    val validationLiveData = MutableLiveData<Boolean>()

    private val _failurePopup = MutableLiveData<Event<ErrorType?>>()
    val failurePopup: LiveData<Event<ErrorType?>> = _failurePopup

    private val _navigate = MutableLiveData<Event<NavigationCommand>>()
    val navigate: LiveData<Event<NavigationCommand>> = _navigate


    protected open fun handleFailure(errorType: ErrorType? = null) {
        _failurePopup.postValue(Event(errorType))
    }

    protected open fun handleFailureCallback(
        errorType: ErrorType? = null,
        callback: () -> Unit
    ) {
        _navigate.postValue(Event(NavigationCommand.ToFailureCallback(errorType, callback)))
    }

    protected fun navigateToFragment(navDirections: NavDirections) {
        _navigate.postValue(Event(NavigationCommand.ToDirection(navDirections)))
    }

    protected fun navigateToActivity(
        className: Class<*>,
        flags: Int? = null,
        isClearTask: Boolean = false
    ) {
        _navigate.postValue(
            Event(
                NavigationCommand.ToDirectionActivity(
                    className = className,
                    flags = flags,
                    isClearTask = isClearTask
                )
            )
        )
    }

//    protected fun navigateToPopup(
//        popupUiModel: PopupUiModel,
//        callbackPositive: (() -> Unit)? = null
//    ) {
//        _navigate.postValue(
//            Event(
//                NavigationCommand.ToPopup(
//                    popupUiModel,
//                    callbackPositive = callbackPositive
//                )
//            )
//        )
//    }

    fun navigateToBack() {
        _navigate.postValue(Event(NavigationCommand.Back))
    }

    fun showLoading() {
        _navigate.postValue(Event(NavigationCommand.LoadingDialog(true)))
    }

    fun hideLoading() {
        _navigate.postValue(Event(NavigationCommand.LoadingDialog(false)))
    }

}