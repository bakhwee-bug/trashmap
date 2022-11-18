package com.example.trash

import android.provider.ContactsContract
import com.google.gson.annotations.SerializedName
import ted.gun0912.clustering.clustering.TedClusterItem
import ted.gun0912.clustering.geometry.TedLatLng
import java.io.Serializable

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
    ): Serializable

data class TrashInfo(
    var address: String,
    var status: String,
    var detail: String
)

data class JsonTrash(
    val data: List<Trash>
)


data class Trash(
    var id: Int,
    var address: String?,
    var detail: String?,
    var kind: Int,
    var full_status: Int,
    var latitude: Double,
    var longitude: Double,
    var delete_point: Int,
    var trash_name: String,
    var author: Int
): Serializable, TedClusterItem {
    override fun getTedLatLng(): TedLatLng {
        return TedLatLng(latitude, longitude)
    }
}