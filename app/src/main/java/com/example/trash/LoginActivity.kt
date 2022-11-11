package com.example.trash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        var retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-39-194-139.ap-northeast-2.compute.amazonaws.com:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var loginService: LoginService = retrofit.create(LoginService::class.java)

        btn_login.setOnClickListener{
            var email = edittext1.text.toString()
            var pw = edittext2.text.toString()

            loginService.requestLogin(email,pw).enqueue(object: Callback<Login> {
                override fun onFailure(call: Call<Login>, t: Throwable) {
                Log.e("LOGIN", t.message.toString())
                var dialog = AlertDialog.Builder(this@LoginActivity)
                dialog.setTitle("에러")
                dialog.setMessage("호출실패했습니다.")
                dialog.show()
                }

                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    val login = response.body()
                    Log.d("LOGIN","msg : "+login?.message)
                    Log.d("LOGIN","result : "+login?.result)
                    var dialog = AlertDialog.Builder(this@LoginActivity)
                    dialog.setTitle(login?.result)
                    dialog.setMessage(login?.message)
                    dialog.show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            })
        }

        btn_join.setOnClickListener{
            val intent = Intent(this@LoginActivity, JoinActivity::class.java)
            startActivity(intent)
        }
    }
}