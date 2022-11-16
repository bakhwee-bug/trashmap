package com.example.trash

import retrofit2.Call
import retrofit2.http.*

interface InfoActivity {
    @GET("/users/info")
    fun requestUser(
        @Header("Authorization") authToken: String
    ): Call<User>
}