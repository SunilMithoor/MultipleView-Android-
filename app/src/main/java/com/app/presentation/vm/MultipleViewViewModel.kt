package com.app.presentation.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.domain.entity.response.Datas
import com.app.domain.model.ViewState
import com.app.domain.usecase.MultipleViewUseCase
import com.app.domain.usecase.None
import com.app.domain.util.getViewStateFlowForRawCall
import com.app.presentation.base.BaseViewModel
import com.app.presentation.util.CodeSnippet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MultipleViewViewModel @Inject constructor(
    application: Application,
    private val multipleViewUseCase: MultipleViewUseCase,
    private val codeSnippet: CodeSnippet
) : BaseViewModel(application) {

    val observeMultipleViewDataLive: MutableLiveData<ViewState<Datas>> by lazy {
        MutableLiveData<ViewState<Datas>>()
    }

    val observeMultipleViewFinalDataLive: MutableLiveData<ViewState<Datas>> by lazy {
        MutableLiveData<ViewState<Datas>>()
    }

    @ExperimentalCoroutinesApi
    fun fetchMultipleViewData() {
        viewModelScope.launch {
            getViewStateFlowForRawCall {
                multipleViewUseCase.execute(None)!!
            }.collect {
                Timber.d("Data received :: $it")
                observeMultipleViewDataLive.value = it
            }
        }
    }
    
    fun validateData() {

    }

    fun getAnswerData() {
        observeMultipleViewFinalDataLive.value = observeMultipleViewDataLive.value
    }

//    fun validation(
//        surveyData: Data
//    ): Boolean {
//
//
//        return false;
//    }

}