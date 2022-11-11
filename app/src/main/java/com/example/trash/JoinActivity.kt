package com.example.trash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_join.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        var joinService : JoinService = retrofit.create(JoinService::class.java)

        btn_check.setOnClickListener{
            val email = user_email.text.toString()
            joinService.requestRegister(email).enqueue(object: Callback<Login>{
                override fun onFailure(call: Call<Login>, t: Throwable) {

                    Log.e("JOIN", t.message.toString())
                    var dialog = AlertDialog.Builder(this@JoinActivity)
                    dialog.setTitle("false")
                    dialog.setMessage("이미 가입한 이메일입니다.")
                   dialog.show()
                }

                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    val login = response.body()
                    Log.d("JOIN","msg : "+login?.message)
                    Log.d("JOIN","result : "+login?.result)
                    var dialog = AlertDialog.Builder(this@JoinActivity)
                    dialog.setTitle(login?.result)
                    dialog.setMessage(login?.message)
                    dialog.show()
                }
            })
        }


        btn_cer.setOnClickListener{
            val email = user_email.text.toString()
            joinService.requestEmail(email).enqueue(object: Callback<Msg>{
                override fun onFailure(call: Call<Msg>, t: Throwable) {

                    Log.e("JOIN", t.message.toString())
                    var dialog = AlertDialog.Builder(this@JoinActivity)
                    dialog.setTitle("false")
                    dialog.setMessage("이미 가입한 이메일입니다.")
                    dialog.show()
                }

                override fun onResponse(call: Call<Msg>, response: Response<Msg>) {
                    val login = response.body()
                    Log.d("JOIN","인증번호 : "+login?.message)
                    var dialog = AlertDialog.Builder(this@JoinActivity)
                    dialog.setTitle("true")
                    dialog.setMessage("인증번호가 전송되었습니다")
                    dialog.show()
                }
            })
        }

        btn_join.setOnClickListener {
            val email = user_email.text.toString()
            val password = user_pw.text.toString()
            val nickname = user_id.text.toString()

            joinService.requestRP(email, password, nickname).enqueue(object: Callback<Login>{
                override fun onFailure(call: Call<Login>, t: Throwable) {

                    Log.e("JOIN", t.message.toString())
                    var dialog = AlertDialog.Builder(this@JoinActivity)
                    dialog.setTitle("false")
                    dialog.setMessage("회원가입에 실패하였습니다.")
                    dialog.show()
                }

                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    val login = response.body()
                    Log.d("JOIN","msg : "+login?.message)
                    Log.d("JOIN","result : "+login?.result)
                    var dialog = AlertDialog.Builder(this@JoinActivity)
                    dialog.setTitle(login?.result)
                    dialog.setMessage(login?.message)
                    dialog.show()
                    val intent = Intent(this@JoinActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            })
        }

    }
}