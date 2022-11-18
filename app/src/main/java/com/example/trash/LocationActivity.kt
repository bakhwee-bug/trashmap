package com.example.trash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Transformations.map
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.google.android.gms.location.*
import java.util.*


class LocationActivity : AppCompatActivity(),
    OnMapReadyCallback {
        val permission_request = 99
        private lateinit var naverMap: NaverMap
        var permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        //권한 가져오기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)



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



        override fun onMapReady(naverMap: NaverMap){

            val cameraPosition = CameraPosition(
                LatLng(37.5666102, 126.9783881),  // 위치 지정
                16.0 // 줌 레벨
            )

            naverMap.cameraPosition = cameraPosition
            this.naverMap = naverMap

            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this) //gps 자동으로 받아오기
            setUpdateLocationListner() //내위치를 가져오는 코드
        }

        //내 위치를 가져오는 코드
        lateinit var fusedLocationProviderClient: FusedLocationProviderClient //자동으로 gps값을 받아온다.
        lateinit var locationCallback: LocationCallback //gps응답 값을 가져온다.
        //lateinit: 나중에 초기화 해주겠다는 의미

        @SuppressLint("MissingPermission")
        fun setUpdateLocationListner() {
            val locationRequest = LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY //높은 정확도
                interval = 1000 //1초에 한번씩 GPS 요청
            }


            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult ?: return
                    for ((i, location) in locationResult.locations.withIndex()) {
                        Log.d("location: ", "${location.latitude}, ${location.longitude}")
                        setLastLocation(location)
                    }
                }
            }
            //location 요청 함수 호출 (locationRequest, locationCallback)

            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        }//좌표계를 주기적으로 갱신

        fun setLastLocation(location: Location) {
            val myLocation = LatLng(location.latitude, location.longitude)
            val marker = Marker()
            marker.position = myLocation
            marker.map = naverMap

            //마커
            val cameraUpdate = CameraUpdate.scrollTo(myLocation)
            naverMap.moveCamera(cameraUpdate)
            naverMap.maxZoom = 18.0
            naverMap.minZoom = 5.0

            //marker.map = null


            //화면 넘어가기
            val addButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btn_add)
            addButton.setOnClickListener(){
                val intent = Intent(this, AddActivity::class.java).apply {
                    putExtra("latitude", location.latitude)
                    putExtra("longitude", location.longitude)
                }
                startActivity(intent)
                finish()
            }
        }
    //화면 넘어가기




    }