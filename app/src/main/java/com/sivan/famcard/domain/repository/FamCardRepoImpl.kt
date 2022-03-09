package com.sivan.famcard.domain.repository

import com.sivan.famcard.data.remote.FamCardApi
import com.sivan.famcard.data.remote.dto.CardGroups
import com.sivan.famcard.data.remote.repository.FamCardRepo
import javax.inject.Inject

class FamCardRepoImpl @Inject constructor(
    private val api: FamCardApi
) : FamCardRepo {

    override suspend fun getFamCards(): CardGroups {
        return api.getFamCards()
    }

}