package com.example.trash

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PointF
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.util.*


class LocationActivity : AppCompatActivity(),
    OnMapReadyCallback {
        var retrofit: Retrofit = RetrofitClient.getInstance()
        val permission_request = 99
        private lateinit var naverMap: NaverMap
        var permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        var token : String? = ""
        //권한 가져오기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)


        token = intent.getStringExtra("token")
        val userservice : InfoActivity = retrofit.create(InfoActivity::class.java)

        userservice.requestUser(token).enqueue(object : Callback<User> {
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

        if (isPermitted()) {
            startProcess()
        } else {
            ActivityCompat.requestPermissions(this, permissions, permission_request)
        }//권한 확인

    }

        fun isPermitted(): Boolean {
            for (perm in permissions) {
                if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }//권한을 허락 받아야함

        fun startProcess(){
            val fm = supportFragmentManager
            val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
                ?: MapFragment.newInstance().also {
                    fm.beginTransaction().add(R.id.map_fragment, it).commit()
                } //권한
            mapFragment.getMapAsync(this)
        } //권한이 있다면 onMapReady연결



        @RequiresApi(Build.VERSION_CODES.Q)
        override fun onMapReady(naverMap: NaverMap){

            val cameraPosition = CameraPosition(
                LatLng(37.496594, 126.956995),  // 위치 지정
                16.0 // 줌 레벨
            )



            naverMap.cameraPosition = cameraPosition
            this.naverMap = naverMap

            naverMap.addOnCameraIdleListener {
                val projection = naverMap.projection
                val coord = projection.fromScreenLocation(PointF(marker.left.toFloat(), marker.top.toFloat()))
                setLastLocation(coord)
            }



        }



        fun setLastLocation(location: LatLng) {
            val myLocation = LatLng(location.latitude, location.longitude)
            currentState.text = myLocation.toString()

            var mGeocoder = Geocoder(this, Locale.KOREAN)
            //var mResultList: List<Address>? = null
            var addressString= ""
            try {
                val addressList: List<Address> = mGeocoder.getFromLocation(location.latitude, location.longitude, 1)

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
                    currentState.text = addressString

                }
            } catch (e: IOException) {
                Toast.makeText(applicationContext,"Unable connect to Geocoder", Toast.LENGTH_LONG).show()
            }


            //화면 넘어가기
            val addButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btn_add)
            addButton.setOnClickListener(){
                val intent = Intent(this, AddActivity::class.java).apply {
                    putExtra("latitude", location.latitude)
                    putExtra("longitude", location.longitude)
                    putExtra("token", token)
                }
                startActivity(intent)
                finish()
            }
        }
    //화면 넘어가기




    }