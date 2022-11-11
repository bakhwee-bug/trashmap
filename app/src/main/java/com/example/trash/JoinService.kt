package com.example.trash

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


//회원가입
interface JoinService{
    //중복회원검사
    @FormUrlEncoded
    @POST("/users/register")
    fun requestRegister(
        @Field("user_email") user_email:String
    ) : Call<Login>

    //해당 이메일로 인증 번호 전송
    @FormUrlEncoded
    @POST("/users/register-validate-email")
    fun requestEmail(
        @Field("user_email") user_email:String
    ) : Call<Msg>

    //DB에 회원정보 insert
    @FormUrlEncoded
    @POST("/users/register-process")
    fun requestRP(
        @Field("user_email") user_email: String,
        @Field("user_pw") user_pw: String,
        @Field("user_nickname") user_nickname: String
    ) : Call<Login>

}