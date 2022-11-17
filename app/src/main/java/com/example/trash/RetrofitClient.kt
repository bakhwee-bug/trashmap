package com.example.trash

import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager

object RetrofitClient {
    private var instance: Retrofit? = null

    fun getInstance() : Retrofit {
        if(instance == null){
            var builder = OkHttpClient().newBuilder()


            var okHttpClient = builder
                .cookieJar(JavaNetCookieJar(CookieManager()))
                .build()

            instance = Retrofit.Builder()
                .baseUrl("http://ec2-3-39-194-139.ap-northeast-2.compute.amazonaws.com:3000/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance!!
    }

}