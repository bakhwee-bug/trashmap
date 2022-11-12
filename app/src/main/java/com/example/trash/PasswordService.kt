package com.example.trash

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.PATCH
import retrofit2.http.POST

interface PasswordService {
    //기존 비밀번호를 입력 받아 DB에 있는 것과 같은 지 검사
    @FormUrlEncoded
    @POST("/users/validate-pw")
    fun requestCheckPw(
        @Field("user_pw") user_pw:String
    ): Call<Msg>

    //기존 비밀번호를 새로운 비밀번호로 수정
    @FormUrlEncoded
    @PATCH("/users/update/pw")
    fun requestNewPw(
        @Field("user_pw") user_pw:String
    ): Call<Msg>
}