package com.example.trash

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_add.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        //val user = intent.getSerializableExtra("key") as User?

        var retrofit: Retrofit = RetrofitClient.getInstance()
        var trashService: TrashService = retrofit.create(TrashService::class.java)






        add_trash.setOnClickListener{

            val trash_name = edit_trash.text.toString()
            var trash_address = trash_address.text.toString()
            Log.d("trash_name", trash_name)


            var kind = 1
            var trash_kind = radioGroup.getCheckedRadioButtonId().toString()

            when (trash_kind){
                "btn_trash" -> kind = 1
                "btn_siga" -> kind = 2
                "btn_recycle" ->kind = 3
            }


            var trash_latitude = 37.498439
            var trash_longitude = 126.951787
            var trash_full_status = radioGroup2.getCheckedRadioButtonId().toString()
            var status = 1
            when (trash_full_status){
                "status1" -> status = 1
                "status2" -> status = 2
                "status3" ->status = 3
            }
            val detail = edit_add.text.toString()
            Log.d("detail", detail)
            trashService.requestAddTrash(trash_name, trash_address, kind, trash_latitude, trash_longitude, status,detail)
                .enqueue(object: Callback<Login> {
                override fun onFailure(call: Call<Login>, t:Throwable) {
                    Toast.makeText(this@AddActivity, "add 실패", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    Log.e("Main: eventHandler", "이벤트 헨들러")
                    Toast.makeText(this@AddActivity, "추가 완료", Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
        }

    }
}