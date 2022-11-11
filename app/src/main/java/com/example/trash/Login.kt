package com.example.trash

import com.google.gson.annotations.SerializedName

data class Login (
    var result: String,
    var message: String
)

data class Msg(
    @SerializedName("authNum") var message: String
)