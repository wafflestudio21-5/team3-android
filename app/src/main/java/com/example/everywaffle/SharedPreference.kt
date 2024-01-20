package com.example.everywaffle

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context) { // key는 가급적 all 소문자로
    private val prefs:SharedPreferences = context.getSharedPreferences("pref",Context.MODE_PRIVATE)

    fun getString(key:String,defvalue:String = "-1"):String{
        return prefs.getString(key,defvalue).toString()
    }

    fun setString(key:String,str:String){
        prefs.edit().putString(key,str).apply()
    }

    fun reset(){ // 로그아웃
        prefs.edit().remove("userid").apply()
        prefs.edit().remove("token").apply()
        prefs.edit().remove("id").apply()
        prefs.edit().remove("password").apply()
        prefs.edit().remove("realname").apply()
        prefs.edit().remove("nickname").apply()
        prefs.edit().remove("department").apply()
        prefs.edit().remove("studentid").apply()
        prefs.edit().remove("mail").apply()
    }


}