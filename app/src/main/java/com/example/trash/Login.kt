package com.example.trash

import android.provider.ContactsContract
import com.google.gson.annotations.SerializedName

data class Login (
    var result: String,
    var message: String
)

data class Msg(
    @SerializedName("authNum") var message: String
)

data class User(
    var email: String,
    var nickname: String,
    var point: Int,
    var add_point: Int,
    var del_point: Int,
    var review_point: Int
    )