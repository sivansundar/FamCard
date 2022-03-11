package com.sivan.famcard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sivan.famcard.data.remote.dto.CardGroups
import com.sivan.famcard.domain.DismissedId
import com.sivan.famcard.domain.usecases.GetFamCardsUseCase
import com.sivan.famcard.util.DataState
import com.sivan.famcard.util.sharedpref.SharedPrefUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharedPrefUtil: SharedPrefUtil,
    private val getFamCardsUseCase: GetFamCardsUseCase
) : ViewModel() {

    private val _requestStatusMessage = MutableSharedFlow<String>()
    val requestStatusMessage = _requestStatusMessage.asSharedFlow()

    private val _famCardList: MutableStateFlow<DataState<CardGroups>> =
        MutableStateFlow(DataState.Loading)

    val famCardList: StateFlow<DataState<CardGroups>> = _famCardList.stateIn(
        initialValue = DataState.Loading,
        started = SharingStarted.WhileSubscribed(5000),
        scope = viewModelScope
    )

    init {
        getFamcards()
    }

    fun getFamcards() {
        viewModelScope.launch {
            getFamCardsUseCase.invoke()
                .catch { _famCardList.emit(DataState.Error(it.message.toString())) }
                .collect {
                    _famCardList.emit(it)
                }
        }
    }

    suspend fun setRequestStatusMessage(message: String) {
        _requestStatusMessage.emit(message)
    }

    fun saveId(id : Int) {
        val dismissedId = DismissedId(id)
        sharedPrefUtil.saveDismissedId(dismissedId)
    }

    fun getDismissedID(): ArrayList<DismissedId> {
        return sharedPrefUtil.getAllDismissedIds()
    }
}