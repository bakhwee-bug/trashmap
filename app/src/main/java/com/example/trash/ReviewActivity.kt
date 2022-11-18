package com.example.trash

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

class ReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        val retrofit: Retrofit = RetrofitClient.getInstance()
        val trashService: TrashService = retrofit.create(TrashService::class.java)

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

        review_trash.setOnClickListener {

            val detail = edit_add.text.toString()
            val trashStatus = statusGroup.getCheckedRadioButtonId().toString()
            Log.d("Review: check_status= ", trashStatus)

            var status = when(trashStatus){
                "2131296366" -> 1
                "2131296372" -> 2
                "2131296365" -> 3
                else -> 1
            }
            Log.d("Review: status= ", status.toString())
            Log.d("Review: detail= ", detail)

            trashService.requestUpdateTrash(trashId, status, detail).enqueue(object:
                Callback<Login> {
                override fun onFailure(call: Call<Login>, t:Throwable) {
                    Toast.makeText(this@ReviewActivity, "add 실패", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    Log.d("넘어간 Review: trashId", trashId.toString())
                    Log.d("넘어간 Review: status", status.toString())
                    Log.d("넘어간 Review: detail", detail)
                    Log.e("Main: eventHandler", "이벤트 헨들러")
                    Toast.makeText(this@ReviewActivity, "추가 완료", Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
        }


    }
}