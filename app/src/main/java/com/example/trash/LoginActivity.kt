package com.example.trash

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.navi_header.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import android.webkit.CookieManager


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

            if(edittext1.text.isNullOrBlank()||edittext2.text.isNullOrBlank()){
                AlertDialog.Builder(this@LoginActivity).run {
                    setTitle("에러")
                    setMessage("이메일과 비밀번호를 모두 입력하세요.")
                    setNegativeButton("확인", null)
                    show()
                }
            }else{
                //로그인 요청
                loginService.requestLogin(email,pw).enqueue(object: Callback<Login> {
                override fun onFailure(call: Call<Login>, t: Throwable) {
                    Log.e("LOGIN", t.message.toString())
                    AlertDialog.Builder(this@LoginActivity).run {
                        setTitle("에러")
                        setMessage(t.message)
                        setNegativeButton("확인", null)
                        show()
                    }
                }

                override fun onResponse(
                    call: Call<Login>, response: Response<Login>
                ) {
                    if(response.isSuccessful) {
                        val login = response.body()
                        Log.d("LOGIN", "msg : " + login?.message)
                        Log.d("LOGIN", "result : " + login?.result)


                        val authToken = "토큰값을 여기 작성"

                        //유저 정보 가져오기
                        val userservice : InfoActivity = retrofit.create(InfoActivity::class.java)

                        userservice.requestUser(authToken)?.enqueue(object : Callback<User>{
                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                if(response.isSuccessful){
                                    //정상적으로 통신이 된 경우
                                    val user = response.body()
                                    val mIntent = Intent(this@LoginActivity, MainActivity::class.java).apply{
                                        putExtra("email", user?.email.toString() )
                                        putExtra("nickname", user?.nickname.toString())
                                        putExtra("point", user?.point.toString())
                                        putExtra("add_point", user?.add_point.toString())
                                        putExtra("delete_point", user?.del_point.toString())
                                        putExtra("review_point", user?.review_point.toString())
                                    }
                                    setResult(RESULT_OK, mIntent)
                                    finish()

                                }
                                else {
                                    //통신 실패
                                    try {
                                        val body = response.errorBody()!!.string()
                                        Log.e("Login:User", "error - body : $body")
                                    } catch (e: IOException) {
                                        e.printStackTrace()
                                    }
                                }
                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                //통신 실패
                                Log.d("Login: User","에러: "+t.message.toString())
                                Log.d("message: ", t.message.toString())
                            }
                        })




                        AlertDialog.Builder(this@LoginActivity).run {
                            setTitle(login?.result)
                            setMessage(login?.message)
                            setPositiveButton("확인", null)
                            show()



                        }
                    }else{
                        Log.d("LOGIN", "msg : Hmm.." )
                        Log.d("LOGIN", "result : 아이디 또는 비밀번호가 잘못되었습니다.")
                        AlertDialog.Builder(this@LoginActivity).run {
                            setTitle("error")
                            setMessage("아이디 또는 비밀번호가 잘못되었습니다.")
                            setPositiveButton("확인", null)
                            show()
                        }
                    }

                }
            })
            }
        }



        btn_join.setOnClickListener{
            val intent = Intent(this@LoginActivity, JoinActivity::class.java)
            startActivity(intent)
        }



    }


}