package com.sivan.famcard.data.remote

import com.sivan.famcard.BuildConfig
import com.sivan.famcard.data.remote.dto.CardGroups
import com.sivan.famcard.util.DataState
import retrofit2.http.GET

interface FamCardApi {

    //Get details of a single investor
    @GET(BuildConfig.ENDPOINTURL)
    suspend fun getFamCards(): CardGroups
}