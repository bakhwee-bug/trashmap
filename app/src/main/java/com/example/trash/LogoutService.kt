package com.example.trash

import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

//๋ก๊ทธ์์
interface LogoutService{
    @GET("/users/logout")
    fun requestLogout(
    ) : Call<Msg>
}
