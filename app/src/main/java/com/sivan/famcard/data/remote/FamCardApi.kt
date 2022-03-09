package com.sivan.famcard.data.remote

import com.sivan.famcard.BuildConfig
import com.sivan.famcard.data.remote.dto.CardGroups
import retrofit2.http.GET

interface FamCardApi {

    @GET(BuildConfig.ENDPOINTURL)
    suspend fun getFamCards(): CardGroups
}