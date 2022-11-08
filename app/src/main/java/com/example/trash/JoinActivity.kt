package com.example.trash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        var retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-39-194-139.ap-northeast-2.compute.amazonaws.com:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var loginService : LoginService = retrofit.create(LoginService::class.java)



    }
}