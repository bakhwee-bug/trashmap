package com.example.trash

import android.content.DialogInterface
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


        val eventHandler = object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                if(p1== DialogInterface.BUTTON_POSITIVE){
                    val intent = Intent(this@JoinActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        var retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-39-194-139.ap-northeast-2.compute.amazonaws.com:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var joinService : JoinService = retrofit.create(JoinService::class.java)

        //중복확인
        btn_check.setOnClickListener{
            val email = user_email.text.toString()
            joinService.requestRegister(email).enqueue(object: Callback<Login>{
                override fun onFailure(call: Call<Login>, t: Throwable) {
                    Log.e("JOIN", t.message.toString())
                    AlertDialog.Builder(this@JoinActivity).run {
                        setTitle("false")
                        setMessage("이미 가입한 이메일입니다.")
                        setNegativeButton("확인", null)
                        show()
                        finish()
                    }
                }


                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    if(response.isSuccessful) {
                        val login = response.body()
                        Log.d("JOIN", "msg : " + login?.message)
                        Log.d("JOIN", "result : " + login?.result)

                        AlertDialog.Builder(this@JoinActivity).run {
                            setTitle(login?.result)
                            setMessage(login?.message)
                            setNegativeButton("확인", null)
                            show()
                        }
                    }
                    else{
                        AlertDialog.Builder(this@JoinActivity).run {
                            setTitle("false")
                            setMessage("이미 가입한 이메일입니다.")
                            setNegativeButton("확인", null)
                            show()
                        }
                    }
                }
            })
        }


        var authNum =""

        //인증번호 전송
        btn_send.setOnClickListener{
            val email = user_email.text.toString()
            joinService.requestEmail(email).enqueue(object: Callback<Msg>{
                override fun onFailure(call: Call<Msg>, t: Throwable) {

                    Log.e("JOIN", t.message.toString())
                    AlertDialog.Builder(this@JoinActivity).run {
                        setTitle("false")
                        setMessage(t.message)
                        setNegativeButton("확인", null)
                        show()
                    }
                }

                override fun onResponse(call: Call<Msg>, response: Response<Msg>) {
                    val login = response.body()
                    Log.d("JOIN","인증번호 : "+login?.message)
                    authNum = login?.message.toString()

                    AlertDialog.Builder(this@JoinActivity).run {
                        setTitle("true")
                        setMessage("인증번호가 전송되었습니다")
                        setNegativeButton("확인", null)
                        show()
                    }
                }
            })
        }

        //인증하기
        btn_cer.setOnClickListener {
            val userAuthNum = user_cernum.text.toString()
            if(userAuthNum==authNum){
                AlertDialog.Builder(this@JoinActivity).run {
                    setTitle("true")
                    setMessage("인증에 성공하였습니다")
                    setNegativeButton("확인", null)
                    show()
                }
            }
            else{
                AlertDialog.Builder(this@JoinActivity).run {
                    setTitle("false")
                    setMessage("인증번호가 틀립니다")
                    setNegativeButton("확인", null)
                    show()
                }
            }
        }



        btn_join.setOnClickListener {
            val email = user_email.text.toString()
            val password = user_pw.text.toString()
            val nickname = user_id.text.toString()

            joinService.requestRP(email, password, nickname).enqueue(object: Callback<Login>{
                override fun onFailure(call: Call<Login>, t: Throwable) {
                    Log.e("JOIN", t.message.toString())
                    AlertDialog.Builder(this@JoinActivity).run {
                        setTitle("false")
                        setMessage("회원가입에 실패하였습니다.")
                        setNegativeButton("확인", null)
                        show()
                    }
                }

                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    val login = response.body()
                    Log.d("JOIN","msg : "+login?.message)
                    Log.d("JOIN","result : "+login?.result)

                    AlertDialog.Builder(this@JoinActivity).run {
                        setTitle(login?.result)
                        setMessage(login?.message)
                        setPositiveButton("확인", eventHandler)
                        show()
                    }
                }
            })
        }

    }
}