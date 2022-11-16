package com.example.trash

import android.app.Application
import com.google.android.gms.common.internal.service.Common

class App :Application(){
    companion object{
        lateinit var prefs:Prefs
    }
    override fun onCreate() {
        prefs=Prefs(applicationContext)
        super.onCreate()
    }
}

