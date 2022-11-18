package com.example.trash

import retrofit2.Call
import retrofit2.http.*

interface TrashService {
    //쓰레기통 추가
    @FormUrlEncoded
    @POST("/trash/add")
    fun requestAddTrash(
        @Field("trash_name") trash_name:String,
        @Field("address") address:String,
        @Field("kind") kind:Int,
        @Field("latitude") latitude:Double,
        @Field("longitude") longitude:Double,
        @Field("full_status") full_status:Int,
        @Field("detail") detail:String
    ): Call<Login>

    //쓰레기통 수정
    @FormUrlEncoded
    @PATCH("/trash/update/status")
    fun requestUpdateTrash(
        @Field("id") id:Int,
        @Field("full_status") full_status:Int,
        @Field("detail") detail:String
    ):Call<Login>

    //쓰레기통 삭제 요청1
    @FormUrlEncoded
    @PATCH("/trash/update/delete")
    fun requestDeleteTrash(
        @Field("id") id:String
    ):Call<Login>




}