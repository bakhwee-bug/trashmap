package com.example.trash

import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InfoActivity {
    @FormUrlEncoded
    @GET("/users/info")
    fun requestUser(): Call<User>
}