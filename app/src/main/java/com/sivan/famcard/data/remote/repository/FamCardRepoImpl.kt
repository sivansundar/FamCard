package com.sivan.famcard.data.remote.repository

import com.sivan.famcard.data.remote.FamCardApi
import com.sivan.famcard.data.remote.dto.CardGroups
import com.sivan.famcard.domain.repository.FamCardRepo
import com.sivan.famcard.util.DataState
import javax.inject.Inject

class FamCardRepoImpl @Inject constructor(
    private val api: FamCardApi
) : FamCardRepo {

    override suspend fun getFamCards(): CardGroups {
        return api.getFamCards()
    }

}