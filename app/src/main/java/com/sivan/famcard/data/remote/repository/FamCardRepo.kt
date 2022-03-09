package com.sivan.famcard.data.remote.repository

import com.sivan.famcard.data.remote.dto.CardGroups

interface FamCardRepository {

    suspend fun getFamCards(): CardGroups

}