package com.example.everywaffle

import android.app.Application
import android.content.SharedPreferences
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application(){
    companion object{
        lateinit var prefs:SharedPreference
    }
    override fun onCreate() {
        prefs= SharedPreference(applicationContext)
        super.onCreate()
        KakaoSdk.init(this,"fe6dad63a3191e6ceead975b205d3386")
    }
}