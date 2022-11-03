package com.example.trash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker

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

        viewDetail = findViewById(R.id.view_main_detail)

        val reviewButton = findViewById<ImageView>(R.id.btn_review)
        reviewButton.setOnClickListener({
            val intent = Intent(this, ReviewActivity::class.java)
            startActivity(intent)
        })

        val deleteButton = findViewById<ImageView>(R.id.btn_delete)
        deleteButton.setOnClickListener({
            Toast.makeText(this, "삭제 요청이 완료되었습니다", Toast.LENGTH_SHORT).show()
        })

        val addButton = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.btn_add)
        addButton.setOnClickListener({
            val intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
        })

        val closeDetailButton = findViewById<ImageView>(R.id.bt_main_close_detail)
        closeDetailButton.setOnClickListener { viewDetail.visibility = View.GONE }





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
            R.id.menu_item1 -> Toast.makeText(this, "menu_item1 실행", Toast.LENGTH_SHORT).show()
            R.id.menu_item2 -> Toast.makeText(this, "menu_item2 실행", Toast.LENGTH_SHORT).show()
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
            true
        }
    }
}