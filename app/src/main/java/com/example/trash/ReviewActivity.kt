package com.example.trash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.activity_review.edit_add
import kotlinx.android.synthetic.main.activity_review.trash_address
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

class ReviewActivity : AppCompatActivity() {
    var token : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        val retrofit: Retrofit = RetrofitClient.getInstance()
        val trashService: TrashService = retrofit.create(TrashService::class.java)


        token = intent.getStringExtra("token")
        val userservice : InfoActivity = retrofit.create(InfoActivity::class.java)

        userservice.requestUser(token).enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    //정상적으로 통신이 된 경우
                    Log.e("Login:onResponse", "유저서비스의 리퀘스트유저")
                    val user = response.body()

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
                Log.d("MAIN: User","에러: "+t.message.toString())
                Log.d("message: ", t.message.toString())
            }
        })

        trash_name.text = intent.getStringExtra("name")
        trash_address.text = intent.getStringExtra("detail")

        val mainname = intent.getStringExtra("name")
        val mainaddress = intent.getStringExtra("detail")
        val authorId = intent.getIntExtra("user_id", 1)
        val trashId = intent.getIntExtra("trash_id", 1)

        Log.d("REVIEW", "메인에서 넘어 온 주소: " + mainname +
                "\n메인에서 넘어온 상세주소: " + mainaddress +
                "\n메인에서 넘어 온 작성자 id: " + authorId.toString() +
                "\n메인에서 넘어 온 쓰레기통 id: " + trashId.toString())

        btn_full.setOnClickListener {
            var trashStatus1 = statusGroup.getCheckedRadioButtonId().toString()
            Log.d("버튼1= ", trashStatus1)
        }
        btn_suit.setOnClickListener {
            var trashStatus1 = statusGroup.getCheckedRadioButtonId().toString()
            Log.d("버튼2= ", trashStatus1)
        }
        btn_empty.setOnClickListener {
            var trashStatus1 = statusGroup.getCheckedRadioButtonId().toString()
            Log.d("버튼3= ", trashStatus1)
        }

        review_trash.setOnClickListener {

            val detail = edit_add.text.toString()
            var trashStatus = statusGroup.getCheckedRadioButtonId().toString()

            
            

            val status = when(trashStatus){
                "2131296367" -> 1
                "2131296374" -> 2
                "2131296366" -> 3
                else -> 1
            }
            Log.d("Review: status= ", status.toString())
            Log.d("Review: detail= ", detail)

            trashService.requestUpdateTrash(trashId, status, detail).enqueue(object:
                Callback<Login> {
                override fun onFailure(call: Call<Login>, t:Throwable) {
                    Toast.makeText(this@ReviewActivity, "리뷰 실패", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    Log.d("넘어간 Review: trashId", trashId.toString())
                    Log.d("넘어간 Review: status", status.toString())
                    Log.d("넘어간 Review: detail", detail)
                    Log.e("Main: eventHandler", "이벤트 헨들러")
                    Toast.makeText(this@ReviewActivity, "리뷰 완료", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@ReviewActivity, MainActivity::class.java).apply {
                        putExtra("token", token)
                    }
                    finishAffinity()
                    startActivity(intent)
                }
            })
        }


    }
}