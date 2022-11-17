package com.example.trash

import retrofit2.Call
import retrofit2.http.*

interface InfoActivity {
    @GET("/users/info")
    fun requestUser(
        @Header("access_token") accessToken: List<String>
    ): Call<User>
}