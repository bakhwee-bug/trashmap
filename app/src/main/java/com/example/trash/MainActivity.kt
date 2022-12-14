package com.example.trash

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.location.*
import com.google.android.material.navigation.NavigationView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.activity_join.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navi_header.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import ted.gun0912.clustering.naver.TedNaverClustering
import java.io.IOException

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnMapReadyCallback {
    private var alertDialog: AlertDialog? = null
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var viewDetail: ConstraintLayout
    private lateinit var map: NaverMap
    private val PERMISSION_REQUEST_CODE = 100
    private final var FINISH_INTERVAL_TIME: Long = 2000
    private var backPressedTime: Long = 0
    var token : String? = ""

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

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24)
            setDisplayShowTitleEnabled(false)
        }



        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)


        val fm = supportFragmentManager
        var mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        mapFragment.getMapAsync(this)
        
        //?????? ???????????? ??????


        //??????
        viewDetail = findViewById(R.id.view_main_detail)

        

        
        //?????????????????? +??????
        val addButton = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.btn_add)
        addButton.setOnClickListener(){
            val intent = Intent(this, LocationActivity::class.java).apply {
                putExtra("token", token)
            }
            startActivity(intent)
        }

        //?????? ?????? ??????
        val closeDetailButton = findViewById<ImageView>(R.id.bt_main_close_detail)
        closeDetailButton.setOnClickListener {
            viewDetail.visibility = View.GONE
            addButton.show()
        }
        //?????? ????????? ???????????? ?????? ?????????
        val mLocationSource = FusedLocationSource(this@MainActivity, PERMISSION_REQUEST_CODE)


    }


    override fun onStart(){
        super.onStart()
        var header = navigationView.getHeaderView(0)
        val nametext = header.findViewById<TextView>(R.id.user_name)
        val mailtext = header.findViewById<TextView>(R.id.user_email)
        val pointtext = header.findViewById<TextView>(R.id.user_point)


        token = intent.getStringExtra("token")
        val userservice : InfoActivity = retrofit.create(InfoActivity::class.java)

        userservice.requestUser(token).enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    //??????????????? ????????? ??? ??????
                    Log.e("Login:onResponse", "?????????????????? ??????????????????")
                    val user = response.body()
                    nametext.text = user?.nickname.toString()
                    mailtext.text = user?.email.toString()
                    pointtext.text = user?.point.toString()

                }
                else {
                    //?????? ??????
                    try {
                        val body = response.errorBody()!!.string()
                        Log.e("Login:User", "error - body : $body")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                //?????? ??????
                Log.d("MAIN: User","??????: "+t.message.toString())
                Log.d("message: ", t.message.toString())
            }
        })

        val fm = supportFragmentManager
        var mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        mapFragment.getMapAsync(this)

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
                        Toast.makeText(this@MainActivity, "?????? ??????", Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<Msg>, response: Response<Msg>) {
                        p0?.dismiss()
                        Log.e("Main: eventHandler", "????????? ?????????")
                        Toast.makeText(this@MainActivity, "?????? ??????", Toast.LENGTH_SHORT).show()
                        finishAffinity()
                    }
                })
                
            }
        }
    }



    val logoutEventHandler = object : DialogInterface.OnClickListener{
        override fun onClick(p0: DialogInterface?, p1: Int) {
            if(p1== DialogInterface.BUTTON_POSITIVE){
                var logoutService: LogoutService = retrofit.create(LogoutService::class.java)
                logoutService.requestLogout().enqueue(object: Callback<Msg> {
                    override fun onFailure(call: Call<Msg>, t:Throwable) {
                        Toast.makeText(this@MainActivity, "???????????? ??????", Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<Msg>, response: Response<Msg>) {
                        p0?.dismiss()
                        Log.e("Main: eventHandler", "????????? ?????????")
                        val logout = response.body()
                        Toast.makeText(this@MainActivity, "???????????? ??????", Toast.LENGTH_SHORT).show()
                        Log.d("MAIN: LOGOUT", logout?.message.toString())
                        //?????????????????? ????????? ????????? ????????? Main ?????????
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                })

            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //???????????? ?????????
            R.id.menu_item1 ->  {
                val intent = Intent(this, ChangePwActivity::class.java)
                startActivity(intent)
            }
            //????????????
            R.id.menu_item2 -> {

                alertDialog = AlertDialog.Builder(this@MainActivity).run {
                    setTitle("??????")
                    setMessage("???????????? ???????????????????")
                    setPositiveButton("???", logoutEventHandler)
                    setNegativeButton("?????????", null)
                    show()
                }

            }

            //????????????
            R.id.menu_item3 -> {

                alertDialog = AlertDialog.Builder(this@MainActivity).run {
                    setTitle("??????")
                    setMessage("?????? ?????????????????????????")
                    setPositiveButton("???", eventHandler)
                    setNegativeButton("?????????", null)
                    show()
                }

            }
        }
        return false
    }

    @UiThread
    override fun onMapReady(map: NaverMap) {
        val marker = Marker()
        val cameraPosition = CameraPosition(
            LatLng(37.496594, 126.956995),  // ?????? ??????
            16.0 // ??? ??????
        )
        map.cameraPosition = cameraPosition
        this.map = map


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

        fun updateMarker(rows: JsonTrash) {
            Log.d("MAIN: updateMarker", "updateMarker ??????")
            // ?????? ???????????????
            TedNaverClustering.with<Trash>(this@MainActivity, map)
                .customMarker { // ????????? ????????? ???????????? ??????
                    Marker().apply {
                        icon = OverlayImage.fromResource(R.drawable.gps_can)
                        width = 120
                        height = 120
                    }
                }
                .customCluster { // ???????????? View ??? ????????? ???????????? ??????
                    TextView(this).apply {
                        setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.green_200))
                        setTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
                        text = "${it.size}???"
                        setPadding(10, 10, 10, 10)
                    }
                }
                .markerClickListener { //?????? ????????? ???
                    Log.d("????????? ????????????: ", "{$it}")
                    //var TrashData = rows.data
                    Marker().apply {
                        width = 150
                        height = 150
                    }
                    trash_name.text = it.address
                    trash_detail.text = it.detail
                    val inputname = it.address
                    val inputdetail = it.detail
                    val trashId = it.id
                    val userId = it.author
                    var status = it.full_status
                    when(status){
                        1 -> status_bar.setImageResource(R.drawable.img_status)
                        2 -> status_bar.setImageResource(R.drawable.img_status2)
                        3 -> status_bar.setImageResource(R.drawable.img_status3)
                    }

                    viewDetail.visibility = View.VISIBLE
                    val addButton = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.btn_add)
                    addButton.hide()
                    //???????????? ????????????
                    val reviewButton = findViewById<ImageView>(R.id.btn_review)
                    reviewButton.setOnClickListener(){
                        val intent = Intent(this, ReviewActivity::class.java).apply {
                            putExtra("name", inputname)
                            putExtra("detail", inputdetail)
                            putExtra("trash_id", trashId)
                            putExtra("user_id", userId)
                            putExtra("token", token)
                        }
                        startActivity(intent)
                    }
                    //???????????? ????????????
                    val deleteButton = findViewById<ImageView>(R.id.btn_delete)
                    deleteButton.setOnClickListener(){
                        val trashService: TrashService = retrofit.create(TrashService::class.java)
                        trashService.requestDeleteTrash(trashId).enqueue(object: Callback<Login> {
                            override fun onFailure(call: Call<Login>, t:Throwable) {
                                Toast.makeText(this@MainActivity, "requestDeleteTrash ??????", Toast.LENGTH_SHORT).show()
                            }
                            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                                val delete = response.body()
                                Toast.makeText(this@MainActivity, "requestDeleteTrash ??????", Toast.LENGTH_SHORT).show()
                                Log.d("MAIN: DeleteTrash", delete?.message.toString())
                            }

                        })
                        Toast.makeText(this, "?????? ????????? ?????????????????????", Toast.LENGTH_SHORT).show()
                    }
                }
                .items(rows.data)
                .make()

            }


            val trashService: TrashService = retrofit.create(TrashService::class.java)
            trashService.requestAll().enqueue(object: Callback<JsonTrash> {
                override fun onFailure(call: Call<JsonTrash>, t:Throwable) {
                    Toast.makeText(this@MainActivity, "loadTrash ??????", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<JsonTrash>, response: Response<JsonTrash>) {
                    val trash = response.body()
                    Toast.makeText(this@MainActivity, "loadTrash ??????", Toast.LENGTH_SHORT).show()
                    Log.d("MAIN: loadTrash", trash.toString())
                    if (trash != null) {
                        updateMarker(trash)
                    }
                    Log.d("MAIN: requestAll", "requestAll ??????")
                }
            })



    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 0) {
            var tempTime = System.currentTimeMillis();
            var intervalTime = tempTime - backPressedTime;
            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
                super.onBackPressed();
            } else {
                backPressedTime = tempTime;
                Toast.makeText(this, "'??????' ????????? ??? ??? ??? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
                return
            }
        }
        super.onBackPressed();
    }




}