package com.app.data.remote.retrofit.api


import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("/fact")
    suspend fun fetchData(): Response<ResponseBody>


}