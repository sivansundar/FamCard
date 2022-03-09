package com.sivan.famcard.data.remote

import com.sivan.famcard.data.remote.dto.CardGroups
import retrofit2.http.GET

interface FamCardApi {

    //Get details of a single investor
    @GET("fefcfbeb-5c12-4722-94ad-b8f92caad1ad}")
    suspend fun getFamCards(): CardGroups
}