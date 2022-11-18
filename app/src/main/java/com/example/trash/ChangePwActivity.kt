package com.example.trash

import android.app.ProgressDialog.show
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_changepw.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChangePwActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changepw)

        val eventHandler = object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                if(p1== DialogInterface.BUTTON_POSITIVE){
                    val intent = Intent(this@ChangePwActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        var retrofit: Retrofit = RetrofitClient.getInstance()
        var passwordService : PasswordService = retrofit.create(PasswordService::class.java)

        //현비번==입력값?
        btn_check.setOnClickListener{
            val curpw = user_curpw.text.toString()
            passwordService.requestCheckPw(curpw).enqueue(object: Callback<Msg>{
                override fun onFailure(call: Call<Msg>, t:Throwable){
                    AlertDialog.Builder(this@ChangePwActivity).run {
                        setTitle("에러")
                        setMessage(t.message)
                        setNegativeButton("확인", null)
                        show()
                    }
                }
                override fun onResponse(call: Call<Msg>, response: Response<Msg>){
                    val changepw = response.body()
                    AlertDialog.Builder(this@ChangePwActivity).run {
                        setTitle("확인")
                        setMessage(changepw?.message)
                        setNegativeButton("확인", null)
                        show()
                    }
                }
            })
        }

        //new 비번
        //서버에 등록
        btn_join.setOnClickListener { 
            if(user_newpw.text.toString()!=user_pw.text.toString()){
                AlertDialog.Builder(this@ChangePwActivity).run {
                    setTitle("에러")
                    setMessage("비밀번호 확인이 틀립니다.")
                    setNegativeButton("확인", null)
                    show()
                }
            } else{
                var pw=user_newpw.text.toString()
                //등록
                passwordService.requestNewPw(pw).enqueue(object: Callback<Msg>{
                    override fun onFailure(call: Call<Msg>, t: Throwable) {
                        AlertDialog.Builder(this@ChangePwActivity).run {
                            setTitle("에러")
                            setMessage(t.message)
                            setNegativeButton("확인", null)
                            show()
                        }
                    }

                    override fun onResponse(call: Call<Msg>, response: Response<Msg>) {
                        if(response.isSuccessful){
                            val changepw = response.body()
                            AlertDialog.Builder(this@ChangePwActivity).run {
                                setTitle("성공")
                                setMessage(changepw?.message)
                                setPositiveButton("확인", eventHandler)
                                show()
                            }
                        }else{
                            val changepw = response.body()
                            AlertDialog.Builder(this@ChangePwActivity).run {
                                setTitle("에러")
                                setMessage(changepw?.message)
                                setNegativeButton("확인", null)
                                show()
                            }
                        }
                    }
                })
            }
        }


    }
}