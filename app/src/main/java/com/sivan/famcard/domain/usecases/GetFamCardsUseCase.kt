package com.sivan.famcard.domain.usecases

import com.sivan.famcard.domain.repository.FamCardRepoImpl
import com.sivan.famcard.data.remote.dto.CardGroups
import javax.inject.Inject

class GetFamCardsUseCase @Inject constructor(
    private val repo: FamCardRepoImpl
) {

    suspend operator fun invoke(): CardGroups {
        return repo.getFamCards()
    }
}