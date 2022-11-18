package com.example.trash

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.FormUrlEncoded


interface LeaveService{
    @FormUrlEncoded
    @DELETE("/users/delete")
    fun requestLeave(
    ) : Call<Msg>
}