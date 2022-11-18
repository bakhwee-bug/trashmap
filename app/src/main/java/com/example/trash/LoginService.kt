package com.example.trash

import com.example.trash.Login
import retrofit2.Call
import retrofit2.http.*

//로그인
interface LoginService{
    @FormUrlEncoded
    @POST("/users/login")
    fun requestLogin(
        @Field("user_email") user_email:String,
        @Field("user_pw") user_pw:String
    ) : Call<Login>
}








