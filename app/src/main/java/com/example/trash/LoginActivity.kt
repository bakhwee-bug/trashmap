package com.example.trash

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val eventHandler = object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                if(p1==DialogInterface.BUTTON_POSITIVE){
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        var retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-39-194-139.ap-northeast-2.compute.amazonaws.com:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var loginService: LoginService = retrofit.create(LoginService::class.java)

        find_pw.setOnClickListener{
            val intent = Intent(this@LoginActivity, ChangePwActivity::class.java)
            startActivity(intent)
        }

        btn_login.setOnClickListener{
            var email = edittext1.text.toString()
            var pw = edittext2.text.toString()

            loginService.requestLogin(email,pw).enqueue(object: Callback<Login> {
                override fun onFailure(call: Call<Login>, t: Throwable) {
                Log.e("LOGIN", t.message.toString())
                AlertDialog.Builder(this@LoginActivity).run {
                    setTitle("에러")
                    setMessage(t.message)
                    setNegativeButton("확인", null)
                    show()
                    finish()
                    }
                }

                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    val login = response.body()
                    Log.d("LOGIN","msg : "+login?.message)
                    Log.d("LOGIN","result : "+login?.result)
                    AlertDialog.Builder(this@LoginActivity).run {
                        setTitle(login?.result)
                        setMessage(login?.message)
                        setPositiveButton("확인", eventHandler)
                        show()
                    }
                }
            })
        }



        btn_join.setOnClickListener{
            val intent = Intent(this@LoginActivity, JoinActivity::class.java)
            startActivity(intent)
        }



    }


}