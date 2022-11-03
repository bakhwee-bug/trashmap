package com.example.trash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        val addButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btn_add)
        addButton.setOnClickListener({
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        })

    }



}