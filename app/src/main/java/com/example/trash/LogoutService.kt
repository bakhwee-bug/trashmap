package com.example.trash

import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

//로그아웃
interface LogoutService{
    @GET("/users/logout")
    fun requestLogout(
    ) : Call<Msg>
}
