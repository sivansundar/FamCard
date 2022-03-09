package com.sivan.famcard.ui

import androidx.lifecycle.ViewModel
import com.sivan.famcard.data.remote.dto.CardGroups
import com.sivan.famcard.domain.usecases.GetFamCardsUseCase
import com.sivan.famcard.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getFamCardsUseCase: GetFamCardsUseCase
) : ViewModel(){

    suspend fun getFamcards(): Flow<DataState<CardGroups>> {
        return getFamCardsUseCase.invoke()

    }
}