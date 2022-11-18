package com.example.trash

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_add.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.util.*


class AddActivity : AppCompatActivity() {
    val permission_request = 99
    var permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        //val user = intent.getSerializableExtra("key") as User?

        var retrofit: Retrofit = RetrofitClient.getInstance()
        var trashService: TrashService = retrofit.create(TrashService::class.java)

        val aLatitude = intent.getDoubleExtra("latitude", 37.492657)
        val aLongitude = intent.getDoubleExtra("longitude", 126.957594)
        Log.d("addressString", aLatitude.toString())
        Log.d("addressString", aLongitude.toString())

        val lat = aLatitude
        val lng = aLongitude
        var mGeocoder = Geocoder(this, Locale.KOREAN)
        //var mResultList: List<Address>? = null
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        var addressString= ""
        try {
            val addressList: List<Address> = mGeocoder.getFromLocation(lat, lng, 1)

            // use your lat, long value here
            if (addressList != null && addressList.isNotEmpty()) {
                val address = addressList.get(0)
                Log.d("address", address.toString())
                var sb = StringBuilder()
                sb.append(address.adminArea).append(" ")
                sb.append(address.thoroughfare).append(" ")
                sb.append(address.featureName).append(" ")

                addressString = sb.toString()
                Log.d("addressString", addressString)
                trash_address.text = addressString

            }
        } catch (e: IOException) {
            Toast.makeText(applicationContext,"Unable connect to Geocoder",Toast.LENGTH_LONG).show()
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, permissions, permission_request)
            return
        }






        /*try {
            mResultList = mGeocoder.getFromLocation(
                lat!!, lng!!, 1
            )
        }catch (e: IOException){
            e.printStackTrace()
        }
        if(mResultList!=null){
            Log.d("CheckCurrentLocation", mResultList[0].getAddressLine(0))
            currentLocation = mResultList[0].getAddressLine(0)
            currentLocation = currentLocation.substring(11)
        }

        trash_address.text = currentLocation*/



        add_trash.setOnClickListener{

            val tName = edit_trash.text.toString()
            var tAddress = trash_address.text.toString()
            Log.d("trash_name", tName)


            var kind = 1
            var tKind = radioGroup.getCheckedRadioButtonId().toString()
            Log.d("Review: check_status= ", tKind)


            when (tKind){
                "2131296373" -> kind = 1
                "2131296839" -> kind = 2
                "2131296369" ->kind = 3
            }


            var tLatitude = aLatitude
            var tLongitude = aLongitude
            var trashFullStatus = radioGroup2.getCheckedRadioButtonId().toString()
            var status = when (trashFullStatus){
                "2131296734" -> 1
                "2131296735" -> 2
                "2131296736" -> 3
                else -> 1
            }
            val detail = edit_add.text.toString()
            Log.d("detail", detail)
            trashService.requestAddTrash(tName, tAddress, kind, tLatitude, tLongitude, status,detail)
                .enqueue(object: Callback<Login> {
                override fun onFailure(call: Call<Login>, t:Throwable) {
                    Toast.makeText(this@AddActivity, "add 실패", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    Log.e("Main: eventHandler", "이벤트 헨들러")
                    Toast.makeText(this@AddActivity, "추가 완료", Toast.LENGTH_SHORT).show()
                    Log.d("Review: AddTrash", "tName: " + tName)
                    Log.d("Review: AddTrash:", "tAddress: " + tAddress)
                    Log.d("Review: AddTrash", "kind: " + kind.toString())
                    Log.d("Review: AddTrash", "tLatitude: " + tLatitude.toString())
                    Log.d("Review: AddTrash", "tLongitude: " + tLongitude.toString())
                    Log.d("Review: AddTrash", "status: " + status.toString())
                    Log.d("Review: AddTrash", "detail: " + detail)
                    finish()
                }
            })
        }

    }

}