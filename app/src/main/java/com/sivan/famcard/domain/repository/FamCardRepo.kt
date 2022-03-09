package com.sivan.famcard.domain.repository

import com.sivan.famcard.data.remote.dto.CardGroups


interface FamCardRepo {
    suspend fun getFamCards(): CardGroups

}