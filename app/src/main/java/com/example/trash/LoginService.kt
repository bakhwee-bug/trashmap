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



//회원 정보 조회
interface GetInfo{
    @FormUrlEncoded
    @GET("/users/info")
    fun requestInfo(
        @Field("email") email: String,
        @Field("nickname") nickname: String,
        @Field("point") point: String
        ) : Call<Login>
}

//비밀번호 수정

interface AmendPw{
    @FormUrlEncoded
    //기존 비밀번호를 입력받아 DB에 있는 것과 같은지 검사
    @POST("/users/validate-pw")
    fun requestValPw(
        @Field("user_pw") user_pw: String
    ) : Call<Msg>

    //기존 비밀번호를 새로운 비밀번호로 수정
    @PATCH("/users/update/pw")
    fun requestUpdatePw(
        @Field("user_pw") user_pw: String
    ) : Call<Msg>
}



