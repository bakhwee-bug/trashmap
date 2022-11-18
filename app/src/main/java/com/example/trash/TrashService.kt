package com.example.trash

import retrofit2.Call
import retrofit2.http.*

interface TrashService {
    //쓰레기통 추가
    @FormUrlEncoded
    @POST("/trash/add")
    fun requestAddTrash(
        @Field("name") name:String,
        @Field("address") address:String,
        @Field("kind") kind:String,
        @Field("altitude") altitude:String,
        @Field("latitude") latitude:String,
        @Field("full_status") full_status:String
    ): Call<Login>

    //쓰레기통 수정
    @FormUrlEncoded
    @PATCH("/trash/update/status")
    fun requestUpdateTrash(
        @Field("trash_id") trash_id:String,
        @Field("full_status") full_status:String
    ):Call<Login>

    //쓰레기통 삭제 요청
    @FormUrlEncoded
    @PATCH("/trash/update/delete")
    fun requestDeleteTrash(
        @Field("trash_id") trash_id:String,
        @Field("user_id") user_id:String
    ):Call<Login>

    //쓰레기통 삭제 요청
    @FormUrlEncoded
    @GET("/trash/select/id")
    fun requestSelectId(
        @Field("trash_id") trash_id:String
    ):Call<Trash>


}