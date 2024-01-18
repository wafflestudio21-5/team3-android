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

// pref의 사용 용도 : 처음 실행시 로그인 안해도 홈 화면으로 가도록 하는 경우, viewmodel의 사용 줄이기