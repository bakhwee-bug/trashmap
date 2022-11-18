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

    //쓰레기통 리뷰
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

    //쓰레기통 조회(3이 안될 때 쓰는 거)
    @GET("/trash/select/all")
    fun requestAll(
    ):Call<JsonTrash>

    //쓰레기통 조회2
    @GET("/trash/select/id")
    fun requestIdTrash(
    ):Call<TrashInfo>

    //쓰레기통 조회3

    @GET("/trash/select/display")
    fun requestLocationTrash(
        @Field("x1") x1: Double,
        @Field("x2") x2: Double,
        @Field("y1") y1: Double,
        @Field("y2") y2: Double,
    ):Call<JsonTrash>


}