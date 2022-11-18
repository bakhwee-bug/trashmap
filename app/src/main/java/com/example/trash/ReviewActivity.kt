package com.example.trash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.activity_review.edit_add
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlinx.android.synthetic.main.activity_review.radioGroup2 as radioGroup21

class ReviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        var retrofit: Retrofit = RetrofitClient.getInstance()
        var trashService: TrashService = retrofit.create(TrashService::class.java)
        review_trash.setOnClickListener {


            val trash_id = 1
            val detail = edit_add.text.toString()
            val trash_status = radioGroup2.getCheckedRadioButtonId().toString()
            var status = 1
            when (trash_status){
                "status1" -> status = 1
                "status2" -> status = 2
                "status3" ->status = 3
            }
            Log.d("Review: status= ", status.toString())

            trashService.requestUpdateTrash(trash_id, status, detail).enqueue(object:
                Callback<Login> {
                override fun onFailure(call: Call<Login>, t:Throwable) {
                    Toast.makeText(this@ReviewActivity, "add 실패", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    Log.e("Main: eventHandler", "이벤트 헨들러")
                    Toast.makeText(this@ReviewActivity, "추가 완료", Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
        }


    }
}