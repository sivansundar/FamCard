package com.sivan.famcard.domain.usecases

import com.sivan.famcard.data.remote.repository.FamCardRepoImpl
import com.sivan.famcard.util.DataState
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFamCardsUseCase @Inject constructor(
    private val repo: FamCardRepoImpl
) {

    suspend operator fun invoke() = flow {
        emit(DataState.Loading)
        try {
            val result = repo.getFamCards()
            emit(DataState.Success(result))
        } catch (e: Exception) {
            emit(DataState.Error(e.message.toString()))
        }
    }
}