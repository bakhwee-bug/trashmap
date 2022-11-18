package com.example.trash

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Color
import android.inputmethodservice.Keyboard
import android.location.Location
import android.location.LocationRequest
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.UiThread
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Transformations
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
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.android.synthetic.main.navi_header.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ted.gun0912.clustering.naver.TedNaverClustering

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnMapReadyCallback {
    private var alertDialog: AlertDialog? = null
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var viewDetail: ConstraintLayout

    var retrofit: Retrofit = RetrofitClient.getInstance()
   /* private lateinit var mTextView: TextView
    private lateinit var mailTextView: TextView
    private lateinit var pointTextView: TextView
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        /*
        mTextView = findViewById<TextView>(R.layout.navi_header)
        mailTextView = findViewById<TextView>(R.id.user_email)
        pointTextView = findViewById<TextView>(R.id.user_point)

        */


        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24)
            setDisplayShowTitleEnabled(false)
        }



        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)


        var header = navigationView.getHeaderView(0)
        val nametext = header.findViewById<TextView>(R.id.user_name)
        val mailtext = header.findViewById<TextView>(R.id.user_email)
        val pointtext = header.findViewById<TextView>(R.id.user_point)


        val user = intent.getSerializableExtra("key") as User?
        val email = intent.getStringExtra("user_email")
        val nickname = intent.getStringExtra("user_name")
        val point = intent.getStringExtra("user_point")


        /* mTextView.text = user?.nickname.toString()
         mailTextView.text = user?.email.toString()
         pointTextView.text = user?.point.toString()
 */

        Log.d("닉네임", user?.nickname.toString())
        nametext.text = nickname
        mailtext.text = email
        pointtext.text = point



        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        mapFragment.getMapAsync(this)
        
        //마커 불러오기 구현


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
            val intent = Intent(this, LocationActivity::class.java).apply {
                putExtra("object", user)
            }
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

    val eventHandler = object : DialogInterface.OnClickListener{
        override fun onClick(p0: DialogInterface?, p1: Int) {
            if(p1== DialogInterface.BUTTON_POSITIVE){
                var leaveService : LeaveService = retrofit.create(LeaveService::class.java)
                leaveService.requestLeave().enqueue(object: Callback<Msg>{
                    override fun onFailure(call: Call<Msg>, t:Throwable) {
                        Toast.makeText(this@MainActivity, "탈퇴 실패", Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<Msg>, response: Response<Msg>) {
                        p0?.dismiss()
                        Log.e("Main: eventHandler", "이벤트 헨들러")
                        Toast.makeText(this@MainActivity, "탈퇴 완료", Toast.LENGTH_SHORT).show()
                    }
                })
                
            }
        }
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
                var logoutService: LogoutService = retrofit.create(LogoutService::class.java)
                logoutService.requestLogout().enqueue(object: Callback<Msg> {
                    override fun onFailure(call: Call<Msg>, t:Throwable) {
                        Toast.makeText(this@MainActivity, "로그아웃 실패", Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<Msg>, response: Response<Msg>) {
                        val logout = response.body()
                        Toast.makeText(this@MainActivity, "로그아웃 완료", Toast.LENGTH_SHORT).show()
                        Log.d("MAIN: LOGOUT", logout?.message.toString())
                        finish()
                    }

                })
            }

            //회원탈퇴
            R.id.menu_item3 -> {

                alertDialog = AlertDialog.Builder(this@MainActivity).run {
                    setTitle("확인")
                    setMessage("정말 탈퇴하시겠습니까?")
                    setPositiveButton("예", eventHandler)
                    setNegativeButton("아니오", null)
                    show()
                }

            }
        }
        return false
    }

    override fun onMapReady(map: NaverMap) {
        val marker = Marker()
        marker.position = LatLng(37.5670135, 126.9783740)
        marker.map = map

        //loadTrash()

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

        fun updateMarker(rows: List<Trash>) {
            Log.d("MAIN: updateMarker", "updateMarker 완료")
            // 마커 클러스터링
            TedNaverClustering.with<Trash>(this@MainActivity, map)
                .customMarker { // 마커를 원하는 모양으로 변경
                    Marker().apply {
                        icon = OverlayImage.fromResource(R.drawable.gps_can)
                        width = 60
                        height = 86
                    }
                }
                .items(rows)
                .make()
        }

            var trashService: TrashService = retrofit.create(TrashService::class.java)
            trashService.requestAll().enqueue(object: Callback<Trash> {
                override fun onFailure(call: Call<Trash>, t:Throwable) {
                    Toast.makeText(this@MainActivity, "loadTrash 실패", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<Trash>, response: Response<Trash>) {
                    val trash = response.body()
                    Toast.makeText(this@MainActivity, "loadTrash 완료", Toast.LENGTH_SHORT).show()
                    Log.d("MAIN: loadTrash", trash.toString())
                    if (trash != null) {
                        //updateMarker()
                    }
                }
            })


        Log.d("MAIN: requestAll", "requestAll 완료")





    }



    //lateinit var tedNaverClustering: TedNaverClustering<Trash>

    // 리스트들 마커 찍기




}