package com.example.trash

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Color
import android.location.Location
import android.location.LocationRequest
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Transformations.map
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import kotlinx.android.synthetic.main.navi_header.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private lateinit var getResultText: ActivityResultLauncher<Intent>



class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnMapReadyCallback {

    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var viewDetail: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)



        getResultText = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result->
            if(result.resultCode == RESULT_OK){
                user_email.text = result.data?.getStringExtra("email")
                user_name.text = result.data?.getStringExtra("nickname")
                user_point.text = result.data?.getStringExtra("point")
                Log.d("MAIN", "유저정보 가져오기")
            }
        }




        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24)
            setDisplayShowTitleEnabled(false)
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        mapFragment.getMapAsync(this)


        //모달
        viewDetail = findViewById(R.id.view_main_detail)

        
        //모달에서 리뷰쓰기
        val reviewButton = findViewById<ImageView>(R.id.btn_review)
        reviewButton.setOnClickListener({
            val intent = Intent(this, ReviewActivity::class.java)
            startActivity(intent)
        })
        
        //모달에서 삭제요청
        val deleteButton = findViewById<ImageView>(R.id.btn_delete)
        deleteButton.setOnClickListener({
            Toast.makeText(this, "삭제 요청이 완료되었습니다", Toast.LENGTH_SHORT).show()
        })
        
        //메인화면에서 +버튼
        val addButton = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.btn_add)
        addButton.setOnClickListener({
            val intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
        })

        //모달 닫기 버튼
        val closeDetailButton = findViewById<ImageView>(R.id.bt_main_close_detail)
        closeDetailButton.setOnClickListener {
            viewDetail.visibility = View.GONE
            addButton.show()
        }

    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {

                drawerLayout.openDrawer(GravityCompat.END)
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //비밀번호 바꾸기
            R.id.menu_item1 ->  {
                val intent = Intent(this, ChangePwActivity::class.java)
                startActivity(intent)
            }
            //로그아웃
            R.id.menu_item2 -> {
               /* var logoutService: LogoutService = retrofit.create(LogoutService::class.java)
                logoutService.requestLogout()*/
                val lintent = Intent(this@MainActivity, LoginActivity::class.java)
                getResultText.launch(lintent)
                Toast.makeText(this, "로그아웃 완료", Toast.LENGTH_SHORT).show()
            }

            R.id.menu_item3 -> Toast.makeText(this, "menu_item3 실행", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    override fun onMapReady(map: NaverMap) {
        val marker = Marker()
        marker.position = LatLng(37.5670135, 126.9783740)
        marker.map = map

        marker.setOnClickListener {
            map.moveCamera(
                CameraUpdate.scrollTo(
                    LatLng(
                        marker.position.latitude,
                        marker.position.longitude
                    )
                ).animate(CameraAnimation.Easing)
            )
            viewDetail.visibility = View.VISIBLE
            val addButton = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.btn_add)
            addButton.hide()
            true
        }
    }



}